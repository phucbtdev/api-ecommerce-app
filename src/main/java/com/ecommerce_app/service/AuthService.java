package com.ecommerce_app.service;

import com.ecommerce_app.dto.auth.LoginRequest;
import com.ecommerce_app.dto.auth.SignupRequest;
import com.ecommerce_app.dto.auth.TokenResponse;
import com.ecommerce_app.entity.Role;
import com.ecommerce_app.entity.User;
import com.ecommerce_app.entity.UserProfile;
import com.ecommerce_app.exception.ResourceAlreadyExistsException;
import com.ecommerce_app.exception.ResourceNotFoundException;
import com.ecommerce_app.repository.RoleRepository;
import com.ecommerce_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public TokenResponse authenticateUser(LoginRequest loginRequest) {

        return TokenResponse.builder()
                .type("Bearer")
                .id(Long.valueOf("1"))
                .username("Phúc")
                .email("phuc@gmail.com")
                .build();
    }

    @Transactional
    public User registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new ResourceAlreadyExistsException("Username is already taken");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new ResourceAlreadyExistsException("Email is already in use");
        }

        // Create new user's account
        User user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .active(true)
                .build();

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findByName(Role.ERole.ROLE_USER)
                    .orElseThrow(() -> new ResourceNotFoundException("Error: Role USER is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(Role.ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new ResourceNotFoundException("Error: Role ADMIN is not found."));
                        roles.add(adminRole);
                        break;
                    case "staff":
                        Role staffRole = roleRepository.findByName(Role.ERole.ROLE_STAFF)
                                .orElseThrow(() -> new ResourceNotFoundException("Error: Role STAFF is not found."));
                        roles.add(staffRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(Role.ERole.ROLE_USER)
                                .orElseThrow(() -> new ResourceNotFoundException("Error: Role USER is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);

        // Create empty profile
        UserProfile profile = new UserProfile();
        profile.setUser(user);
        user.setProfile(profile);

        return userRepository.save(user);
    }
}
