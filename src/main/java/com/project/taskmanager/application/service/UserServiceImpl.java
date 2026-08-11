package com.project.taskmanager.application.service;

import com.project.taskmanager.application.dto.request.UserCreateRequest;
import com.project.taskmanager.application.dto.request.UserRoleUpdateRequest;
import com.project.taskmanager.application.dto.response.UserResponse;
import com.project.taskmanager.application.exception.UserNotFoundException;
import com.project.taskmanager.application.service.UserService;
import com.project.taskmanager.domain.enums.Role;
import com.project.taskmanager.domain.model.User;
import com.project.taskmanager.domain.repository.UserRepository;
import com.project.taskmanager.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserResponse createUser(UserCreateRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .isActive(true)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return userMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> getAllActiveUsers() {
        return userRepository.findAllActive().stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    public UserResponse updateUserRole(Long id, UserRoleUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setRole(request.getRole());
        user.setUpdatedAt(OffsetDateTime.now());
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return userMapper.toResponse(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setIsActive(false);
        user.setUpdatedAt(OffsetDateTime.now());
        userRepository.save(user);
    }
}
