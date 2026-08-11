package com.project.taskmanager.infrastructure.persistence.repository;

import com.project.taskmanager.infrastructure.persistence.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupJpaRepository extends JpaRepository<GroupEntity, Long> {
    Optional<GroupEntity> findByIdAndIsActiveTrue(Long id);
    Optional<GroupEntity> findByNameAndIsActiveTrue(String name);
    List<GroupEntity> findByIsActiveTrueOrderByCreatedAtDesc();
}
