package com.project.taskmanager.application.service;

import com.project.taskmanager.application.dto.request.TaskAssignRequest;
import com.project.taskmanager.application.dto.request.TaskStateUpdateRequest;
import com.project.taskmanager.application.dto.response.TaskAssignmentResponse;

import java.util.List;

public interface TaskAssignmentService {
    TaskAssignmentResponse assignTask(TaskAssignRequest request);
    TaskAssignmentResponse updateTaskState(TaskStateUpdateRequest request);
    List<TaskAssignmentResponse> getAllAssignments();
    List<TaskAssignmentResponse> getAssignmentsByUserId(Long userId);
    List<TaskAssignmentResponse> getAssignmentsByTaskId(Long taskId);
    List<TaskAssignmentResponse> getAssignmentsByGroupId(Long groupId);
    void applyPenaltiesForOverdueTasks();
}
