package com.project.taskmanager.infrastructure.persistence.repository.jpa;

import com.project.taskmanager.infrastructure.persistence.entity.TaskStateHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskStateHistoryJpaRepository extends JpaRepository<TaskStateHistoryEntity, Long> {
    List<TaskStateHistoryEntity> findByTaskAssignmentId(Long taskAssignmentId);
}