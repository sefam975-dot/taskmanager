package com.project.taskmanager.infrastructure.persistence.repository.adapter;

import com.project.taskmanager.domain.model.TaskAssignment;
import com.project.taskmanager.domain.repository.TaskAssignmentRepository;
import com.project.taskmanager.infrastructure.mapper.TaskAssignmentMapper;
import com.project.taskmanager.infrastructure.persistence.repository.jpa.TaskAssignmentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskAssignmentRepositoryAdapter implements TaskAssignmentRepository {

    private final TaskAssignmentJpaRepository jpaRepository;
    private final TaskAssignmentMapper mapper;

    @Override
    public TaskAssignment save(TaskAssignment taskAssignment) {
        var entity = mapper.toEntity(taskAssignment);
        var savedEntity = jpaRepository.save(entity);
        return mapper.toModel(savedEntity);
    }

    @Override
    public Optional<TaskAssignment> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toModel);
    }

    @Override
    public List<TaskAssignment> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskAssignment> findByTaskId(Long taskId) {
        return jpaRepository.findByTaskId(taskId).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskAssignment> findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskAssignment> findByGroupId(Long groupId) {
        return jpaRepository.findByGroupId(groupId).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }
}
