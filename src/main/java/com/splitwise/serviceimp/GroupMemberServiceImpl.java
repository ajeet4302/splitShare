package com.splitwise.serviceimp;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;

import com.splitwise.dto.GroupMemberResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.splitwise.dto.AddMemberRequest;
import com.splitwise.entity.ExpenseGroup;
import com.splitwise.entity.GroupMember;
import com.splitwise.entity.User;
import com.splitwise.exception.GroupNotFoundException;
import com.splitwise.exception.MemberAlreadyExistsException;
import com.splitwise.exception.UnauthorizedException;
import com.splitwise.exception.UserNotFoundException;
import com.splitwise.repository.ExpenseGroupRepository;
import com.splitwise.repository.GroupMemberRepository;
import com.splitwise.repository.UserRepository;
import com.splitwise.service.GroupMemberService;

@Service
@Transactional
public class GroupMemberServiceImpl implements GroupMemberService {

    @Autowired
    private GroupMemberRepository memberRepository;

    @Autowired
    private ExpenseGroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public String addMember(AddMemberRequest request, String loggedInEmail) {

        // Logged-in user
        User loggedInUser = userRepository.findByEmail(loggedInEmail)
                .orElseThrow(() ->
                        new UserNotFoundException("Logged in user not found"));

        // Group
        ExpenseGroup group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() ->
                        new GroupNotFoundException("Group not found"));

        // Authorization
        if (!group.getCreatedBy().getId().equals(loggedInUser.getId())) {
            throw new UnauthorizedException("Only group owner can add members.");
        }

        // Member to be added
        User member = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        // Duplicate check
        if (memberRepository.findByExpenseGroupAndUser(group, member).isPresent()) {
            throw new MemberAlreadyExistsException("User already exists in this group.");
        }

        GroupMember groupMember = new GroupMember();
        groupMember.setExpenseGroup(group);
        groupMember.setUser(member);

        memberRepository.save(groupMember);

        return "Member added successfully.";

    }
    @Override
    public List<GroupMemberResponse> getGroupMembers(Long groupId) {

        ExpenseGroup group = groupRepository.findById(groupId)
                .orElseThrow(() ->
                        new GroupNotFoundException("Group not found"));

        List<GroupMember> members =
                memberRepository.findByExpenseGroup(group);

        return members.stream().map(member -> {

            GroupMemberResponse response = new GroupMemberResponse();

            response.setUserId(member.getUser().getId());
            response.setUserName(member.getUser().getName());

            return response;

        }).collect(Collectors.toList());
    }
}