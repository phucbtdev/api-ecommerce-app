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
import com.ecommerce_app.repository.PasswordResetTokenRepository;
import com.ecommerce_app.repository.RoleRepository;
import com.ecommerce_app.repository.UserRepository;
import com.ecommerce_app.service.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtDecoder jwtDecoder;
    private final JwtEncoder jwtEncoder;
    private final JavaMailSender mailSender;

    @Override
    @Transactional
    public TokenResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String accessToken = generateJwtToken(authentication, user, 3600L); // 1 hour
        String refreshToken = generateJwtToken(authentication, user, 604800L); // 7 days

        return TokenResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
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

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                signupRequest.getUsername(),
                signupRequest.getPassword(),
                roles.stream().map(role -> (GrantedAuthority) role::getName).toList()
        );

        String accessToken = generateJwtToken(authentication, user, 3600L); // 1 hour
        String refreshToken = generateJwtToken(authentication, user, 604800L); // 7 days

        return TokenResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
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

            // Verify token is a refresh token and not expired
            if (!jwt.getClaimAsString("type").equals("refresh") || jwt.getExpiresAt().isBefore(Instant.now())) {
                throw new RuntimeException("Invalid or expired refresh token");
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    user.getRoles().stream().map(role -> (GrantedAuthority) role::getName).toList()
            );

            String accessToken = generateJwtToken(authentication, user, 3600L); // 1 hour
            String newRefreshToken = generateJwtToken(authentication, user, 604800L); // 7 days

            return TokenResponse.builder()
                    .token(accessToken)
                    .refreshToken(newRefreshToken)
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

    private String generateJwtToken(Authentication authentication, User user, long expirySeconds) {
        Instant now = Instant.now();

        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expirySeconds))
                .subject(user.getUsername())
                .claim("scope", scope)
                .claim("userId", user.getId().toString());

        if (expirySeconds > 3600L) { // Mark as refresh token if expiry is longer than access token
            claimsBuilder.claim("type", "refresh");
        } else {
            claimsBuilder.claim("type", "access");
        }
        JwtClaimsSet claims = claimsBuilder.build();
        JwsHeader jwsHeader = JwsHeader.with(() -> "HS256").build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    @Async
    public void sendResetEmail(User user, String resetUrl) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Password Reset Request");
        mailMessage.setText("To reset your password, click the link below:\n" + resetUrl + "\n\nThis link is valid for 24 hours.");
        mailSender.send(mailMessage);
    }
}