package com.project.taskmanager.domain.repository;

import com.project.taskmanager.domain.model.UserGroup;
import java.util.List;
import java.util.Optional;

public interface UserGroupRepository {
    UserGroup save(UserGroup userGroup);
    Optional<UserGroup> findById(Long id);
    List<UserGroup> findByUserId(Long userId);
    List<UserGroup> findByGroupId(Long groupId);
    Optional<UserGroup> findByUserIdAndGroupIdAndIsActiveTrue(Long userId, Long groupId);
}
