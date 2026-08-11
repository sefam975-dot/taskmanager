package com.project.taskmanager.domain.repository;

import com.project.taskmanager.domain.model.TaskAssignment;

import java.util.List;
import java.util.Optional;

public interface TaskAssignmentRepository {
    TaskAssignment save(TaskAssignment taskAssignment);
    Optional<TaskAssignment> findById(Long id);
    List<TaskAssignment> findAll();
    List<TaskAssignment> findByTaskId(Long taskId);
    List<TaskAssignment> findByUserId(Long userId);
    List<TaskAssignment> findByGroupId(Long groupId);
}
