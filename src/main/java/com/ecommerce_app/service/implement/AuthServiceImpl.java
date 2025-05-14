package com.ecommerce_app.service.implement;


import com.ecommerce_app.constant.PredefinedRole;
import com.ecommerce_app.dto.auth.LoginRequest;
import com.ecommerce_app.dto.auth.SignupRequest;
import com.ecommerce_app.dto.auth.TokenResponse;
import com.ecommerce_app.dto.request.ForgotPasswordRequest;
import com.ecommerce_app.dto.request.RefreshTokenRequest;
import com.ecommerce_app.dto.request.ResetPasswordRequest;
import com.ecommerce_app.entity.PasswordResetToken;
import com.ecommerce_app.entity.Role;
import com.ecommerce_app.entity.User;
import com.ecommerce_app.exception.AppException;
import com.ecommerce_app.exception.ErrorCode;
import com.ecommerce_app.repository.PasswordResetTokenRepository;
import com.ecommerce_app.repository.RoleRepository;
import com.ecommerce_app.repository.UserRepository;
import com.ecommerce_app.service.interfaces.AuthService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtDecoder jwtDecoder;
    private final JavaMailSender mailSender;

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Override
    @Transactional
    public TokenResponse login(LoginRequest loginRequest) {
        PasswordEncoder password = new BCryptPasswordEncoder(10);
        User user = userRepository
                .findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean authenticated = password.matches(loginRequest.getPassword(), user.getPassword());
        if (!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);

        TokenInfo accessToken = generateJwtToken( user);

        return TokenResponse.builder()
                .token(accessToken.token)
                .type("Bearer")
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()))
                .build();
    }

    @Override
    @Transactional
    public TokenResponse signup(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new RuntimeException("Email is already in use");
        }

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findByName(PredefinedRole.CUSTOMER_ROLE).ifPresent(roles::add);

        User user = User.builder()
                .username(signupRequest.getUsername())
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .fullName(signupRequest.getFullName())
                .roles(roles)
                .active(true)
                .build();
        userRepository.save(user);

        TokenInfo accessToken = generateJwtToken(user);

        return TokenResponse.builder()
                .token(accessToken.token)
                .type("Bearer")
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roles.stream().map(Role::getName).collect(Collectors.toSet()))
                .build();
    }

    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        User user = userRepository.findByEmail(forgotPasswordRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + forgotPasswordRequest.getEmail()));

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(24)) // 24-hour expiry
                .used(false)
                .build();

        passwordResetTokenRepository.save(resetToken);

        // Send email with reset link
        String resetUrl = "http://localhost:8080/api/auth/reset-password?token=" + token;
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Password Reset Request");
        mailMessage.setText("To reset your password, click the link below:\n" + resetUrl + "\n\nThis link is valid for 24 hours.");
        mailSender.send(mailMessage);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(resetPasswordRequest.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid or expired reset token"));

        if (resetToken.isUsed() || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Invalid or expired reset token");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));
        userRepository.save(user);

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);
    }

    @Override
    @Transactional
    public TokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        try {
            Jwt jwt = jwtDecoder.decode(refreshTokenRequest.getRefreshToken());
            String username = jwt.getSubject();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!jwt.getClaimAsString("type").equals("refresh") || jwt.getExpiresAt().isBefore(Instant.now())) {
                throw new RuntimeException("Invalid or expired refresh token");
            }

            TokenInfo accessToken = generateJwtToken(user); // 1 hour

            return TokenResponse.builder()
                    .token(accessToken.token)
                    .type("Bearer")
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                    .build();
        } catch (JwtException e) {
            throw new RuntimeException("Invalid refresh token");
        }
    }

    private TokenInfo generateJwtToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        Date issueTime = new Date();
        Date expiryTime = new Date(Instant.ofEpochMilli(issueTime.getTime())
                .plus(1, ChronoUnit.HOURS)
                .toEpochMilli());

        JWTClaimsSet.Builder jwtClaimsSet = new JWTClaimsSet.Builder()
                .issuer("phucdev")
                .issueTime(issueTime)
                .expirationTime(expiryTime)
                .subject(user.getUsername())
                .claim("scope", buildScope(user))
                .claim("userId", user.getId());

        Payload payload = new Payload(jwtClaimsSet.toString());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(jwtSecret.getBytes()));
            return new TokenInfo(jwsObject.serialize(), expiryTime);
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    @Async
    public void sendResetEmail(User user, String resetUrl) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Password Reset Request");
        mailMessage.setText("To reset your password, click the link below:\n" + resetUrl + "\n\nThis link is valid for 24 hours.");
        mailSender.send(mailMessage);
    }

    private String buildScope(User users) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(users.getRoles()))
            users.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
            });

        return stringJoiner.toString();
    }

    private record TokenInfo(String token, Date expiryDate) {}
}