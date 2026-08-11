package com.project.taskmanager.infrastructure.persistence.repository;

import com.project.taskmanager.domain.model.Group;
import com.project.taskmanager.domain.repository.GroupRepository;
import com.project.taskmanager.infrastructure.mapper.GroupMapper;
import com.project.taskmanager.infrastructure.persistence.entity.GroupEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GroupRepositoryImpl implements GroupRepository {

    private final GroupJpaRepository jpaRepository;
    private final GroupMapper groupMapper;

    @Override
    public Group save(Group group) {
        GroupEntity entity = groupMapper.toEntity(group);
        GroupEntity savedEntity = jpaRepository.save(entity);
        return groupMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Group> findById(Long id) {
        return jpaRepository.findByIdAndIsActiveTrue(id)
                .map(groupMapper::toDomain);
    }

    @Override
    public Optional<Group> findByName(String name) {
        return jpaRepository.findByNameAndIsActiveTrue(name)
                .map(groupMapper::toDomain);
    }

    @Override
    public List<Group> findAllActive() {
        return jpaRepository.findByIsActiveTrueOrderByCreatedAtDesc().stream()
                .map(groupMapper::toDomain)
                .toList();
    }
}
