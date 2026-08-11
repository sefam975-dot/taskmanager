package com.project.taskmanager.application.service;

import com.project.taskmanager.application.dto.request.GroupCreateRequest;
import com.project.taskmanager.application.dto.request.UserGroupAddRequest;
import com.project.taskmanager.application.dto.response.GroupResponse;

import java.util.List;

public interface GroupService {
    GroupResponse createGroup(GroupCreateRequest request, Long adminId);
    GroupResponse getGroupById(Long id);
    List<GroupResponse> getGroupsByUserId(Long userId);
    List<GroupResponse> getAllActiveGroups();
    void addUserToGroup(UserGroupAddRequest request);
    void removeUserFromGroup(Long userId, Long groupId);
    void deleteGroup(Long id);
}
