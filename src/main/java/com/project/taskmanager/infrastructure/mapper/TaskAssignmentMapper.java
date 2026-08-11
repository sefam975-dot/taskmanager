package com.project.taskmanager.infrastructure.mapper;

import com.project.taskmanager.application.dto.response.TaskAssignmentResponse;
import com.project.taskmanager.domain.model.TaskAssignment;
import com.project.taskmanager.infrastructure.persistence.entity.TaskAssignmentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskAssignmentMapper {
    TaskAssignment toModel(TaskAssignmentEntity entity);
    TaskAssignmentEntity toEntity(TaskAssignment model);
    TaskAssignmentResponse toResponse(TaskAssignment model);
}
