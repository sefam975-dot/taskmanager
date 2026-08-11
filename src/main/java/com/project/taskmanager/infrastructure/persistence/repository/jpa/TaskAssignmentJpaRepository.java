package com.project.taskmanager.infrastructure.persistence.repository.jpa;

import com.project.taskmanager.infrastructure.persistence.entity.TaskAssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskAssignmentJpaRepository extends JpaRepository<TaskAssignmentEntity, Long> {
    List<TaskAssignmentEntity> findByTaskId(Long taskId);
    List<TaskAssignmentEntity> findByUserId(Long userId);
    List<TaskAssignmentEntity> findByGroupId(Long groupId); //  !!!!!  SONRADAN EKLENDIII IHTIYAC HALINDE SILINEBILIR
}