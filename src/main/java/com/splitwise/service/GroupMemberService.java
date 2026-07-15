package com.splitwise.service;

import java.util.List;

import com.splitwise.dto.AddMemberRequest;
import com.splitwise.dto.GroupMemberResponse;

public interface GroupMemberService {

    String addMember(AddMemberRequest request, String loggedInEmail);

    List<GroupMemberResponse> getGroupMembers(Long groupId);

}