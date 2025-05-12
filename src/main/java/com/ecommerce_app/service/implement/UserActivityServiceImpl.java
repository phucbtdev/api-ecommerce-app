package com.ecommerce_app.service.implement;

import com.ecommerce_app.dto.request.UserActivityCreationRequest;
import com.ecommerce_app.dto.request.UserActivityUpdateRequest;
import com.ecommerce_app.dto.response.UserActivityResponse;
import com.ecommerce_app.entity.User;
import com.ecommerce_app.entity.UserActivity;
import com.ecommerce_app.exception.EntityNotFoundException;
import com.ecommerce_app.mapper.UserActivityMapper;
import com.ecommerce_app.repository.UserActivityRepository;
import com.ecommerce_app.repository.UserRepository;
import com.ecommerce_app.service.interfaces.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserActivityServiceImpl implements UserActivityService {
    private final UserActivityRepository userActivityRepository;
    private final UserRepository userRepository;
    private final UserActivityMapper userActivityMapper;

    @Override
    public UserActivityResponse createUserActivity(UserActivityCreationRequest request) {
        User user = userRepository.findById(UUID.fromString(request.getUserId()))
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + request.getUserId()));

        UserActivity userActivity = userActivityMapper.toEntity(request, user);
        UserActivity savedActivity = userActivityRepository.save(userActivity);
        return userActivityMapper.toResponse(savedActivity);
    }

    @Override
    public UserActivityResponse getUserActivityById(UUID id) {
        UserActivity userActivity = userActivityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("UserActivity not found with id: " + id));
        return userActivityMapper.toResponse(userActivity);
    }

    @Override
    public UserActivityResponse updateUserActivity(UUID id, UserActivityUpdateRequest request) {
        UserActivity userActivity = userActivityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("UserActivity not found with id: " + id));

        userActivityMapper.updateEntity(request, userActivity);
        UserActivity updatedActivity = userActivityRepository.save(userActivity);
        return userActivityMapper.toResponse(updatedActivity);
    }

    @Override
    public void deleteUserActivity(UUID id) {
        if (!userActivityRepository.existsById(id)) {
            throw new EntityNotFoundException("UserActivity not found with id: " + id);
        }
        userActivityRepository.deleteById(id);
    }
}