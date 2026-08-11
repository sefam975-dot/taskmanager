package com.project.taskmanager.infrastructure.persistence.repository;

import com.project.taskmanager.domain.model.User;
import com.project.taskmanager.domain.repository.UserRepository;
import com.project.taskmanager.infrastructure.persistence.entity.UserEntity;
import com.project.taskmanager.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository jpaRepository;
    private final UserMapper userMapper;

    @Override
    public User save(User user) {
        UserEntity entity = userMapper.toEntity(user);
        UserEntity savedEntity = jpaRepository.save(entity);
        return userMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaRepository.findByIdAndIsActiveTrue(id)
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaRepository.findByUsernameAndIsActiveTrue(username)
                .map(userMapper::toDomain);
    }

    @Override
    public List<User> findAllActive() {
        return jpaRepository.findByIsActiveTrueOrderByCreatedAtDesc().stream()
                .map(userMapper::toDomain)
                .toList();
    }
}
