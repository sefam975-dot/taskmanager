package com.project.taskmanager.application.service.impl;

import com.project.taskmanager.application.dto.request.TaskAssignRequest;
import com.project.taskmanager.application.dto.request.TaskStateUpdateRequest;
import com.project.taskmanager.application.dto.response.TaskAssignmentResponse;
import com.project.taskmanager.application.exception.IndividualTaskGroupAssignmentException;
import com.project.taskmanager.application.exception.TaskNotFoundException;
import com.project.taskmanager.application.exception.UserNotFoundInTaskContextException;
import com.project.taskmanager.application.service.NotificationService;
import com.project.taskmanager.application.service.TaskAssignmentService;
import com.project.taskmanager.application.service.UserService;
import com.project.taskmanager.domain.enums.AssignmentRule;
import com.project.taskmanager.domain.enums.NotificationType;
import com.project.taskmanager.domain.enums.TaskState;
import com.project.taskmanager.domain.model.Task;
import com.project.taskmanager.domain.model.TaskAssignment;
import com.project.taskmanager.domain.repository.TaskAssignmentRepository;
import com.project.taskmanager.domain.repository.TaskRepository;
import com.project.taskmanager.domain.repository.UserGroupRepository;
import com.project.taskmanager.infrastructure.mapper.TaskAssignmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskAssignmentServiceImpl implements TaskAssignmentService {

    private final TaskAssignmentRepository taskAssignmentRepository;
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final UserGroupRepository userGroupRepository;
    private final NotificationService notificationService;
    private final TaskAssignmentMapper taskAssignmentMapper;

    @Override
    @Transactional
    public TaskAssignmentResponse assignTask(TaskAssignRequest request) {
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new TaskNotFoundException("Görev bulunamadı ID: " + request.getTaskId()));

        if (request.getGroupId() != null) {
            if (task.getAssignmentRule() == AssignmentRule.INDIVIDUAL) {
                throw new IndividualTaskGroupAssignmentException("Bireysel kuralı olan göreve grup ataması yapılamaz.");
            }

            var activeMembers = userGroupRepository.findByGroupId(request.getGroupId()).stream()
                    .filter(ug -> Boolean.TRUE.equals(ug.getIsActive()))
                    .toList();

            if (activeMembers.isEmpty()) {
                throw new IllegalArgumentException("Boş gruba görev atanamaz.");
            }

            TaskAssignment lastAssignment = null;
            for (var member : activeMembers) {
                TaskAssignment assignment = TaskAssignment.builder()
                        .taskId(task.getId())
                        .userId(member.getUserId())
                        .groupId(request.getGroupId())
                        .taskState(TaskState.TODO)
                        .penaltyApplied(false)
                        .createdAt(OffsetDateTime.now())
                        .updatedAt(OffsetDateTime.now())
                        .build();
                lastAssignment = taskAssignmentRepository.save(assignment);
            }

            return taskAssignmentMapper.toResponse(lastAssignment);

        } else if (request.getUserId() != null) {
            var user = userService.getUserById(request.getUserId());
            if (user == null || !user.getIsActive()) {
                throw new UserNotFoundInTaskContextException("Atama yapılacak kullanıcı bulunamadı veya aktif değil.");
            }

            TaskAssignment assignment = TaskAssignment.builder()
                    .taskId(task.getId())
                    .userId(request.getUserId())
                    .taskState(TaskState.TODO)
                    .penaltyApplied(false)
                    .createdAt(OffsetDateTime.now())
                    .updatedAt(OffsetDateTime.now())
                    .build();

            TaskAssignment savedAssignment = taskAssignmentRepository.save(assignment);
            return taskAssignmentMapper.toResponse(savedAssignment);
        }

        throw new IllegalArgumentException("Atama için userId veya groupId sağlanmalıdır.");
    }

    @Override
    @Transactional
    public TaskAssignmentResponse updateTaskState(TaskStateUpdateRequest request) {
        try {
            TaskAssignment assignment = taskAssignmentRepository.findById(request.getTaskAssignmentId())
                    .orElseThrow(() -> new TaskNotFoundException("Atama bulunamadı ID: " + request.getTaskAssignmentId()));

            assignment.setTaskState(request.getNewState());
            assignment.setUpdatedAt(OffsetDateTime.now());

            if (request.getNewState() == TaskState.DONE) {
                assignment.setCompletedAt(OffsetDateTime.now());
            }

            TaskAssignment savedAssignment = taskAssignmentRepository.save(assignment);
            syncMainTaskState(savedAssignment.getTaskId());

            return taskAssignmentMapper.toResponse(savedAssignment);

        } catch (OptimisticLockingFailureException e) {
            throw new RuntimeException("Eşzamanlılık hatası: Bu görev aynı anda başka bir işlem veya kullanıcı tarafından güncellendi. Lütfen tekrar deneyin.", e);
        }
    }

    @Override
    public List<TaskAssignmentResponse> getAllAssignments() {
        return taskAssignmentRepository.findAll().stream()
                .map(taskAssignmentMapper::toResponse)
                .toList();
    }

    @Override
    public List<TaskAssignmentResponse> getAssignmentsByUserId(Long userId) {
        return taskAssignmentRepository.findByUserId(userId).stream()
                .map(taskAssignmentMapper::toResponse)
                .toList();
    }

    @Override
    public List<TaskAssignmentResponse> getAssignmentsByTaskId(Long taskId) {
        return taskAssignmentRepository.findByTaskId(taskId).stream()
                .map(taskAssignmentMapper::toResponse)
                .toList();
    }

    @Override
    public List<TaskAssignmentResponse> getAssignmentsByGroupId(Long groupId) {
        return taskAssignmentRepository.findByGroupId(groupId).stream()
                .map(taskAssignmentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void applyPenaltiesForOverdueTasks() {
        OffsetDateTime now = OffsetDateTime.now();

        taskRepository.findAllActive().stream()
                .filter(task -> task.getDueDate() != null)
                .filter(task -> task.getDueDate().isBefore(now))
                .filter(task -> task.getStatus() != TaskState.DONE)
                .forEach(task -> taskAssignmentRepository.findByTaskId(task.getId()).stream()
                        .filter(assignment -> assignment.getTaskState() != TaskState.DONE)
                        .filter(assignment -> !Boolean.TRUE.equals(assignment.getPenaltyApplied()))
                        .forEach(assignment -> {
                            assignment.setPenaltyApplied(true);
                            assignment.setUpdatedAt(now);
                            TaskAssignment savedAssignment = taskAssignmentRepository.save(assignment);

                            if (savedAssignment.getUserId() != null) {
                                notificationService.sendNotification(
                                        savedAssignment.getUserId(),
                                        savedAssignment.getId(),
                                        NotificationType.PENALTY,
                                        "Görev teslim tarihi geçti: " + task.getTitle()
                                );
                            }
                        }));
    }

    private void syncMainTaskState(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Görev bulunamadı ID: " + taskId));

        List<TaskAssignment> assignments = taskAssignmentRepository.findByTaskId(taskId);

        if (task.getAssignmentRule() == AssignmentRule.GROUP_ANYONE) {
            boolean anyDone = assignments.stream().anyMatch(a -> a.getTaskState() == TaskState.DONE);
            if (anyDone) {
                task.setStatus(TaskState.DONE);
                task.setUpdatedAt(OffsetDateTime.now());
                taskRepository.save(task);

                for (TaskAssignment a : assignments) {
                    if (a.getTaskState() != TaskState.DONE) {
                        a.setTaskState(TaskState.DONE);
                        a.setUpdatedAt(OffsetDateTime.now());
                        taskAssignmentRepository.save(a);
                    }
                }
            }
        } else if (task.getAssignmentRule() == AssignmentRule.GROUP_EVERYONE || task.getAssignmentRule() == AssignmentRule.INDIVIDUAL) {
            boolean allDone = assignments.stream().allMatch(a -> a.getTaskState() == TaskState.DONE);
            if (allDone) {
                task.setStatus(TaskState.DONE);
                task.setUpdatedAt(OffsetDateTime.now());
                taskRepository.save(task);
            }
        }
    }
}
