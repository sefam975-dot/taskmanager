package com.project.taskmanager.application.service.impl;

import com.project.taskmanager.application.dto.request.TaskAssignRequest;
import com.project.taskmanager.application.dto.request.TaskCreateRequest;
import com.project.taskmanager.application.dto.request.TaskStateUpdateRequest;
import com.project.taskmanager.application.dto.response.TaskAssignmentResponse;
import com.project.taskmanager.application.dto.response.TaskResponse;
import com.project.taskmanager.application.exception.TaskNotFoundException;
import com.project.taskmanager.application.service.TaskService;
import com.project.taskmanager.domain.enums.TaskState;
import com.project.taskmanager.domain.model.Task;
import com.project.taskmanager.domain.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskResponse createTask(TaskCreateRequest request) {
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .assignmentRule(request.getAssignmentRule())
                .dueDate(request.getDueDate())
                .status(TaskState.TODO)
                .createdBy(request.getCreatedBy())
                .isActive(true)
                .createdAt(OffsetDateTime.now())
                .build();

        Task savedTask = taskRepository.save(task);
        return mapToTaskResponse(savedTask);
    }

    @Override
    public TaskAssignmentResponse assignTask(TaskAssignRequest request) {
        // TODO: Task atama iş mantığı, TaskAssignmentRepository kullanımı ve Notification gönderimi eklenecek.
        return null;
    }

    @Override
    public TaskAssignmentResponse updateTaskState(TaskStateUpdateRequest request) {
        // TODO: Durum güncelleme, TaskStateHistory kaydı ve ilgili kontroller eklenecek.
        return null;
    }

    @Override
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        return mapToTaskResponse(task);
    }

    @Override
    public List<TaskResponse> getAllActiveTasks() {
        return taskRepository.findAllActive().stream()
                .map(this::mapToTaskResponse)
                .toList();
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.delete(id);
    }

    private TaskResponse mapToTaskResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setPriority(task.getPriority());
        response.setAssignmentRule(task.getAssignmentRule());
        response.setDueDate(task.getDueDate());
        response.setStatus(task.getStatus());
        response.setCreatedBy(task.getCreatedBy());
        response.setIsActive(task.getIsActive());
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdatedAt());
        return response;
    }
}