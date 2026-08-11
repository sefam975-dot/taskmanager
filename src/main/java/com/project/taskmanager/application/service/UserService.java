package com.project.taskmanager.application.service;

import com.project.taskmanager.application.dto.request.UserCreateRequest;
import com.project.taskmanager.application.dto.request.UserRoleUpdateRequest;
import com.project.taskmanager.application.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserCreateRequest request);
    UserResponse getUserById(Long id);
    UserResponse getUserByUsername(String username);
    List<UserResponse> getAllActiveUsers();
    UserResponse updateUserRole(Long id, UserRoleUpdateRequest request);
    void deleteUser(Long id);
}
