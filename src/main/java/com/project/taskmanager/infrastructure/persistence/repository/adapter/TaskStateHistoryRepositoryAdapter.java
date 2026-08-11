package com.project.taskmanager.infrastructure.persistence.repository.adapter;

import com.project.taskmanager.domain.model.TaskStateHistory;
import com.project.taskmanager.domain.repository.TaskStateHistoryRepository;
import com.project.taskmanager.infrastructure.mapper.TaskStateHistoryMapper;
import com.project.taskmanager.infrastructure.persistence.repository.jpa.TaskStateHistoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskStateHistoryRepositoryAdapter implements TaskStateHistoryRepository {

    private final TaskStateHistoryJpaRepository jpaRepository;
    private final TaskStateHistoryMapper mapper;

    @Override
    public TaskStateHistory save(TaskStateHistory history) {
        var entity = mapper.toEntity(history);
        var savedEntity = jpaRepository.save(entity);
        return mapper.toModel(savedEntity);
    }

    @Override
    public Optional<TaskStateHistory> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toModel);
    }

    @Override
    public List<TaskStateHistory> findByTaskAssignmentId(Long assignmentId) {
        return jpaRepository.findByTaskAssignmentId(assignmentId).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }
}
