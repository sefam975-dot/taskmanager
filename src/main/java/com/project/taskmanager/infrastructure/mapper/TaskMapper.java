package com.project.taskmanager.infrastructure.mapper;

import com.project.taskmanager.domain.model.Task;
import com.project.taskmanager.infrastructure.persistence.entity.TaskEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task toModel(TaskEntity entity);
    TaskEntity toEntity(Task model);
}
