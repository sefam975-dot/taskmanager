package com.project.taskmanager.infrastructure.persistence.repository.adapter;

import com.project.taskmanager.domain.model.Notification;
import com.project.taskmanager.domain.repository.NotificationRepository;
import com.project.taskmanager.infrastructure.mapper.NotificationMapper;
import com.project.taskmanager.infrastructure.persistence.repository.jpa.NotificationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NotificationRepositoryAdapter implements NotificationRepository {

    private final NotificationJpaRepository jpaRepository;
    private final NotificationMapper mapper;

    @Override
    public Notification save(Notification notification) {
        var entity = mapper.toEntity(notification);
        var savedEntity = jpaRepository.save(entity);
        return mapper.toModel(savedEntity);
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toModel);
    }

    @Override
    public List<Notification> findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Notification> findUnreadByUserId(Long userId) {
        return jpaRepository.findByUserIdAndIsReadFalse(userId).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }
}
