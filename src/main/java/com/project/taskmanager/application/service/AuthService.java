package com.project.taskmanager.application.service;

import com.project.taskmanager.application.dto.request.UserLoginRequest;
import com.project.taskmanager.application.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(UserLoginRequest request);
    // AuthResponse register(UserRegisterRequest request);
}
