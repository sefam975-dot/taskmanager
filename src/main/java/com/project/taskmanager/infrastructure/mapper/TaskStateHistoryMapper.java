package com.project.taskmanager.infrastructure.mapper;

import com.project.taskmanager.domain.model.TaskStateHistory;
import com.project.taskmanager.infrastructure.persistence.entity.TaskStateHistoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskStateHistoryMapper {
    TaskStateHistory toModel(TaskStateHistoryEntity entity);
    TaskStateHistoryEntity toEntity(TaskStateHistory model);
}
