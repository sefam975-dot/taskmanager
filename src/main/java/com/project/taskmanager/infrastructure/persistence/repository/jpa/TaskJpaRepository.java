package com.project.taskmanager.infrastructure.persistence.repository.jpa;

import com.project.taskmanager.infrastructure.persistence.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskJpaRepository extends JpaRepository<TaskEntity, Long> {
    Optional<TaskEntity> findByIdAndIsActiveTrue(Long id);
    List<TaskEntity> findByIsActiveTrue();
}
