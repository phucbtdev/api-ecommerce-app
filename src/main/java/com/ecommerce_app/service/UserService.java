package com.ecommerce_app.service;

import com.ecommerce_app.dto.request.UserCreationRequest;
import com.ecommerce_app.dto.request.UserProfileUpdateRequest;
import com.ecommerce_app.dto.request.UserUpdateRequest;
import com.ecommerce_app.dto.response.UserResponse;
import com.ecommerce_app.entity.Role;
import com.ecommerce_app.entity.User;
import com.ecommerce_app.entity.UserProfile;
import com.ecommerce_app.exception.ResourceAlreadyExistsException;
import com.ecommerce_app.exception.ResourceNotFoundException;
import com.ecommerce_app.mapper.UserMapper;
import com.ecommerce_app.repository.RoleRepository;
import com.ecommerce_app.repository.UserProfileRepository;
import com.ecommerce_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository profileRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::userToUserResponse);
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        return userMapper.userToUserResponse(user);
    }

    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        return userMapper.userToUserResponse(user);
    }

    @Transactional
    public UserResponse createUser(UserCreationRequest userRequest) {
        // Check if username exists
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new ResourceAlreadyExistsException("Username is already taken");
        }

        // Check if email exists
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new ResourceAlreadyExistsException("Email is already in use");
        }

        // Create user
        User user = User.builder()
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .password(userRequest.getPassword())
                .active(userRequest.isActive())
                .build();

        // Set roles
        Set<String> strRoles = userRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findByName(Role.ERole.ROLE_USER)
                    .orElseThrow(() -> new ResourceNotFoundException("Role USER not found"));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(Role.ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new ResourceNotFoundException("Role ADMIN not found"));
                        roles.add(adminRole);
                        break;
                    case "staff":
                        Role staffRole = roleRepository.findByName(Role.ERole.ROLE_STAFF)
                                .orElseThrow(() -> new ResourceNotFoundException("Role STAFF not found"));
                        roles.add(staffRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(Role.ERole.ROLE_USER)
                                .orElseThrow(() -> new ResourceNotFoundException("Role USER not found"));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);

        // Create empty profile
        UserProfile profile = new UserProfile();
        profile.setUser(user);
        user.setProfile(profile);

        User savedUser = userRepository.save(user);
        return userMapper.userToUserResponse(savedUser);
    }

    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Update fields if provided
        if (userUpdateRequest.getFirstName() != null) {
            user.setFirstName(userUpdateRequest.getFirstName());
        }

        if (userUpdateRequest.getLastName() != null) {
            user.setLastName(userUpdateRequest.getLastName());
        }

        if (userUpdateRequest.getEmail() != null && !user.getEmail().equals(userUpdateRequest.getEmail())) {
            if (userRepository.existsByEmail(userUpdateRequest.getEmail())) {
                throw new ResourceAlreadyExistsException("Email is already in use");
            }
            user.setEmail(userUpdateRequest.getEmail());
        }

        // Update roles if provided
        if (userUpdateRequest.getRoles() != null && !userUpdateRequest.getRoles().isEmpty()) {
            Set<Role> roles = new HashSet<>();

            userUpdateRequest.getRoles().forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(Role.ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new ResourceNotFoundException("Role ADMIN not found"));
                        roles.add(adminRole);
                        break;
                    case "staff":
                        Role staffRole = roleRepository.findByName(Role.ERole.ROLE_STAFF)
                                .orElseThrow(() -> new ResourceNotFoundException("Role STAFF not found"));
                        roles.add(staffRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(Role.ERole.ROLE_USER)
                                .orElseThrow(() -> new ResourceNotFoundException("Role USER not found"));
                        roles.add(userRole);
                }
            });

            user.setRoles(roles);
        }

        user.setActive(userUpdateRequest.isActive());

        User updatedUser = userRepository.save(user);
        return userMapper.userToUserResponse(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
    }

    @Transactional
    public UserResponse updateUserProfile(Long userId, UserProfileUpdateRequest profileRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        UserProfile profile = user.getProfile();
        if (profile == null) {
            profile = new UserProfile();
            profile.setUser(user);
            user.setProfile(profile);
        }

        // Update profile fields if provided
        if (profileRequest.getPhoneNumber() != null) {
            profile.setPhoneNumber(profileRequest.getPhoneNumber());
        }

        if (profileRequest.getAddress() != null) {
            profile.setAddress(profileRequest.getAddress());
        }

        if (profileRequest.getCity() != null) {
            profile.setCity(profileRequest.getCity());
        }

        if (profileRequest.getState() != null) {
            profile.setState(profileRequest.getState());
        }

        if (profileRequest.getCountry() != null) {
            profile.setCountry(profileRequest.getCountry());
        }

        if (profileRequest.getZipCode() != null) {
            profile.setZipCode(profileRequest.getZipCode());
        }

        if (profileRequest.getProfileImageUrl() != null) {
            profile.setProfileImageUrl(profileRequest.getProfileImageUrl());
        }

        profileRepository.save(profile);
        User updatedUser = userRepository.findById(userId).orElseThrow();
        return userMapper.userToUserResponse(updatedUser);
    }
}
