package com.project.taskmanager.application.service.impl;

import com.project.taskmanager.application.dto.response.NotificationResponse;
import com.project.taskmanager.application.service.NotificationService;
import com.project.taskmanager.domain.enums.NotificationType;
import com.project.taskmanager.domain.model.Notification;
import com.project.taskmanager.domain.repository.NotificationRepository;
import com.project.taskmanager.infrastructure.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    @Transactional
    public void sendNotification(Long userId, Long taskAssignmentId, NotificationType type, String message) {
        Notification notification = Notification.builder()
                .userId(userId)
                .taskAssignmentId(taskAssignmentId)
                .notificationType(type)
                .message(message)
                .isRead(false)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        notificationRepository.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getUserNotifications(Long userId) {
        return notificationRepository.findByUserId(userId).stream()
                .map(notificationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getUnreadUserNotifications(Long userId) {
        return notificationRepository.findUnreadByUserId(userId).stream()
                .map(notificationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Bildirim bulunamadı ID: " + notificationId));

        notification.setIsRead(true);
        notification.setUpdatedAt(OffsetDateTime.now());
        notificationRepository.save(notification);
    }
}