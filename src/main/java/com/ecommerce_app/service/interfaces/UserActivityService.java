package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.UserActivityCreationRequest;
import com.ecommerce_app.dto.request.UserActivityUpdateRequest;
import com.ecommerce_app.dto.response.UserActivityResponse;

import java.util.UUID;

public interface UserActivityService {
    UserActivityResponse createUserActivity(UserActivityCreationRequest request);
    UserActivityResponse getUserActivityById(UUID id);
    UserActivityResponse updateUserActivity(UUID id, UserActivityUpdateRequest request);
    void deleteUserActivity(UUID id);
}
