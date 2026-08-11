package com.project.taskmanager.application.dto.request;

import com.project.taskmanager.domain.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleUpdateRequest {
    private Role role;
}
