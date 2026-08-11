package com.project.taskmanager.application.service;

import com.project.taskmanager.application.dto.request.GroupCreateRequest;
import com.project.taskmanager.application.dto.request.UserGroupAddRequest;
import com.project.taskmanager.application.dto.response.GroupResponse;
import com.project.taskmanager.application.exception.GroupNotFoundException;
import com.project.taskmanager.application.exception.UserNotFoundException;
import com.project.taskmanager.application.service.GroupService;
import com.project.taskmanager.domain.model.Group;
import com.project.taskmanager.domain.model.UserGroup;
import com.project.taskmanager.domain.repository.GroupRepository;
import com.project.taskmanager.domain.repository.UserGroupRepository;
import com.project.taskmanager.domain.repository.UserRepository;
import com.project.taskmanager.infrastructure.mapper.GroupMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final GroupMapper groupMapper;

    @Override
    public GroupResponse createGroup(GroupCreateRequest request, Long adminId) {
        userRepository.findById(adminId)
                .orElseThrow(() -> new UserNotFoundException("Admin user not found"));

        Group group = Group.builder()
                .name(request.getName())
                .createdBy(adminId)
                .isActive(true)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        Group savedGroup = groupRepository.save(group);
        return groupMapper.toResponse(savedGroup);
    }

    @Override
    public GroupResponse getGroupById(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException("Group not found"));
        return groupMapper.toResponse(group);
    }

    @Override
    public List<GroupResponse> getGroupsByUserId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return userGroupRepository.findByUserId(userId).stream()
                .map(UserGroup::getGroupId)
                .distinct()
                .map(groupId -> groupRepository.findById(groupId)
                        .orElseThrow(() -> new GroupNotFoundException("Group not found")))
                .filter(group -> Boolean.TRUE.equals(group.getIsActive()))
                .map(groupMapper::toResponse)
                .toList();
    }

    @Override
    public List<GroupResponse> getAllActiveGroups() {
        return groupRepository.findAllActive().stream()
                .map(groupMapper::toResponse)
                .toList();
    }

    @Override
    public void addUserToGroup(UserGroupAddRequest request) {
        userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new GroupNotFoundException("Group not found"));

        UserGroup userGroup = UserGroup.builder()
                .userId(request.getUserId())
                .groupId(request.getGroupId())
                .isActive(true)
                .joinedAt(OffsetDateTime.now())
                .build();

        userGroupRepository.save(userGroup);
    }

    @Override
    public void removeUserFromGroup(Long userId, Long groupId) {
        UserGroup userGroup = userGroupRepository.findByUserIdAndGroupIdAndIsActiveTrue(userId, groupId)
                .orElseThrow(() -> new RuntimeException("Active user-group relation not found"));

        userGroup.setIsActive(false);
        userGroup.setLeftAt(OffsetDateTime.now());
        userGroupRepository.save(userGroup);
    }

    @Override
    public void deleteGroup(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException("Group not found"));
        group.setIsActive(false);
        group.setUpdatedAt(OffsetDateTime.now());
        groupRepository.save(group);
    }
}
