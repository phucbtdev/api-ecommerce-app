package com.ecommerce_app.service.implement;

import com.ecommerce_app.dto.request.NotificationCreationRequest;
import com.ecommerce_app.dto.request.NotificationUpdateRequest;
import com.ecommerce_app.dto.response.NotificationResponse;
import com.ecommerce_app.entity.Notification;
import com.ecommerce_app.entity.User;
import com.ecommerce_app.mapper.NotificationMapper;
import com.ecommerce_app.repository.NotificationRepository;
import com.ecommerce_app.repository.UserRepository;
import com.ecommerce_app.service.interfaces.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserRepository userRepository;

    @Override
    public NotificationResponse createNotification(NotificationCreationRequest request) {
        UUID userId = UUID.fromString(request.getUserId());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Notification notification = notificationMapper.toEntity(request);
        notification.setUser(user);
        Notification savedNotification = notificationRepository.save(notification);
        return notificationMapper.toResponse(savedNotification);
    }

    @Override
    public NotificationResponse updateNotification(UUID id, NotificationUpdateRequest request) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));

        notificationMapper.updateEntityFromRequest(request, notification);
        Notification updatedNotification = notificationRepository.save(notification);
        return notificationMapper.toResponse(updatedNotification);
    }

    @Override
    public NotificationResponse getNotification(UUID id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        return notificationMapper.toResponse(notification);
    }

    @Override
    public List<NotificationResponse> getNotificationsByUser(UUID userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        return notificationMapper.toResponseList(notifications);
    }

    @Override
    public void deleteNotification(UUID id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        notificationRepository.delete(notification);
    }
}