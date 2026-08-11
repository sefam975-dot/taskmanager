package com.project.taskmanager.application.service;

import com.project.taskmanager.application.dto.response.NotificationResponse;
import com.project.taskmanager.domain.enums.NotificationType;

import java.util.List;

public interface NotificationService {
    void sendNotification(Long userId, Long taskAssignmentId, NotificationType type, String message);
    List<NotificationResponse> getUserNotifications(Long userId);
    List<NotificationResponse> getUnreadUserNotifications(Long userId);
    void markAsRead(Long notificationId);
}
