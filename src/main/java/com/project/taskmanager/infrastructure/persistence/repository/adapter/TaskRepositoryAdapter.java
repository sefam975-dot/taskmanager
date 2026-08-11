package com.project.taskmanager.infrastructure.persistence.repository.adapter;

import com.project.taskmanager.domain.model.Task;
import com.project.taskmanager.domain.repository.TaskRepository;
import com.project.taskmanager.infrastructure.mapper.TaskMapper;
import com.project.taskmanager.infrastructure.persistence.repository.jpa.TaskJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskRepositoryAdapter implements TaskRepository {

    private final TaskJpaRepository taskJpaRepository;
    private final TaskMapper taskEntityMapper;

    @Override
    public Task save(Task task) {
        var entity = taskEntityMapper.toEntity(task);
        var savedEntity = taskJpaRepository.save(entity);
        return taskEntityMapper.toModel(savedEntity);
    }

    @Override
    public Optional<Task> findById(Long id) {
        return taskJpaRepository.findById(id)
                .map(taskEntityMapper::toModel);
    }

    @Override
    public Optional<Task> findActiveById(Long id) {
        return taskJpaRepository.findByIdAndIsActiveTrue(id)
                .map(taskEntityMapper::toModel);
    }

    @Override
    public List<Task> findAllActive() {
        return taskJpaRepository.findByIsActiveTrue().stream()
                .map(taskEntityMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        taskJpaRepository.deleteById(id);
    }
}
