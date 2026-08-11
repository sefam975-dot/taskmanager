package com.project.taskmanager.application.service;

import com.project.taskmanager.application.dto.request.TaskAssignRequest;
import com.project.taskmanager.application.dto.request.TaskCreateRequest;
import com.project.taskmanager.application.dto.request.TaskStateUpdateRequest;
import com.project.taskmanager.application.dto.response.TaskAssignmentResponse;
import com.project.taskmanager.application.dto.response.TaskResponse;

import java.util.List;

public interface TaskService {
    TaskResponse createTask(TaskCreateRequest request);
    TaskAssignmentResponse assignTask(TaskAssignRequest request);
    TaskAssignmentResponse updateTaskState(TaskStateUpdateRequest request);
    TaskResponse getTaskById(Long id);
    List<TaskResponse> getAllActiveTasks();
    void deleteTask(Long id);
}
//