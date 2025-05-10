package com.ecommerce_app.service.implement;

import com.ecommerce_app.dto.request.UserCreationRequest;
import com.ecommerce_app.dto.request.UserUpdateRequest;
import com.ecommerce_app.dto.response.UserResponse;
import com.ecommerce_app.entity.Role;
import com.ecommerce_app.entity.User;
import com.ecommerce_app.exception.ResourceAlreadyExistsException;
import com.ecommerce_app.exception.ResourceNotFoundException;
import com.ecommerce_app.mapper.UserMapper;
import com.ecommerce_app.repository.RoleRepository;
import com.ecommerce_app.repository.UserRepository;
import com.ecommerce_app.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse createUser(UserCreationRequest userCreationDto) {
        // Check if username already exists
        if (userRepository.existsByUsername(userCreationDto.getUsername())) {
            throw new ResourceAlreadyExistsException("User with username " + userCreationDto.getUsername() + " already exists");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(userCreationDto.getEmail())) {
            throw new ResourceAlreadyExistsException("User with email " + userCreationDto.getEmail() + " already exists");
        }

        // Create new user entity
        User user = userMapper.toEntity(userCreationDto);

        // Encode password
        user.setPassword(passwordEncoder.encode(userCreationDto.getPassword()));

        // Set roles if provided
        if (userCreationDto.getRoleIds() != null && !userCreationDto.getRoleIds().isEmpty()) {
            Set<Role> roles = new HashSet<>();
            for (UUID roleId : userCreationDto.getRoleIds()) {
                Role role = roleRepository.findById(roleId) 
                        .orElseThrow(() -> new ResourceNotFoundException("Role with id " + roleId + " not found"));
                roles.add(role);
            }
            user.setRoles(roles);
        }

        // Save user
        User savedUser = userRepository.save(user);
        log.info("Created new user with id: {}", savedUser.getId());

        return userMapper.toResponseDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));

        return userMapper.toResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username " + username + " not found"));

        return userMapper.toResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));

        return userMapper.toResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponse updateUser(UUID id, UserUpdateRequest userUpdateDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));

        // Check if username is being updated and already exists
        if (userUpdateDto.getUsername() != null &&
                !user.getUsername().equals(userUpdateDto.getUsername()) &&
                userRepository.existsByUsername(userUpdateDto.getUsername())) {
            throw new ResourceAlreadyExistsException("User with username " + userUpdateDto.getUsername() + " already exists");
        }

        // Check if email is being updated and already exists
        if (userUpdateDto.getEmail() != null &&
                !user.getEmail().equals(userUpdateDto.getEmail()) &&
                userRepository.existsByEmail(userUpdateDto.getEmail())) {
            throw new ResourceAlreadyExistsException("User with email " + userUpdateDto.getEmail() + " already exists");
        }

        // Update user entity with values from DTO
        userMapper.updateEntityFromDto(userUpdateDto, user);

        // Encode password if it's being updated
        if (userUpdateDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
        }

        // Update roles if provided
        if (userUpdateDto.getRoleIds() != null) {
            Set<Role> roles = new HashSet<>();
            for (UUID roleId : userUpdateDto.getRoleIds()) {
                Role role = roleRepository.findById(roleId)
                        .orElseThrow(() -> new ResourceNotFoundException("Role with id " + roleId + " not found"));
                roles.add(role);
            }
            user.setRoles(roles);
        }

        // Save updated user
        User updatedUser = userRepository.save(user);
        log.info("Updated user with id: {}", updatedUser.getId());

        return userMapper.toResponseDto(updatedUser);
    }

    @Override
    @Transactional
    public UserResponse updateUserRoles(UUID userId, Set<UUID> roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));

        Set<Role> roles = new HashSet<>();
        for (UUID roleId : roleIds) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new ResourceNotFoundException("Role with id " + roleId + " not found"));
            roles.add(role);
        }

        user.setRoles(roles);
        User updatedUser = userRepository.save(user);
        log.info("Updated roles for user with id: {}", userId);

        return userMapper.toResponseDto(updatedUser);
    }

    @Override
    @Transactional
    public UserResponse activateUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));

        user.setActive(true);
        User updatedUser = userRepository.save(user);
        log.info("Activated user with id: {}", id);

        return userMapper.toResponseDto(updatedUser);
    }

    @Override
    @Transactional
    public UserResponse deactivateUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));

        user.setActive(false);
        User updatedUser = userRepository.save(user);
        log.info("Deactivated user with id: {}", id);

        return userMapper.toResponseDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }

        userRepository.deleteById(id);
        log.info("Deleted user with id: {}", id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
