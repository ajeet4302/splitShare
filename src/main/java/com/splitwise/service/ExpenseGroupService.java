package com.splitwise.service;

import java.util.List;

import com.splitwise.dto.GroupRequest;
import com.splitwise.dto.GroupResponse;

public interface ExpenseGroupService {

    GroupResponse createGroup(GroupRequest request, String email);

    List<GroupResponse> getAllGroups(String email);
    String deleteGroup(Long groupId, String email);

}