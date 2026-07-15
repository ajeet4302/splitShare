package com.splitwise.serviceimp;
import java.util.ArrayList;
import com.splitwise.repository.ExpenseRepository;
import com.splitwise.repository.ExpenseSplitRepository;
import com.splitwise.repository.SettlementRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.splitwise.dto.GroupRequest;
import com.splitwise.dto.GroupResponse;
import com.splitwise.entity.ExpenseGroup;
import com.splitwise.entity.GroupMember;
import com.splitwise.entity.User;
import com.splitwise.exception.UserNotFoundException;
import com.splitwise.repository.ExpenseGroupRepository;
import com.splitwise.repository.GroupMemberRepository;
import com.splitwise.repository.UserRepository;
import com.splitwise.service.ExpenseGroupService;

@Service
@Transactional
public class ExpenseGroupServiceImpl implements ExpenseGroupService {

    @Autowired
    private ExpenseGroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;
    
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseSplitRepository expenseSplitRepository;

    @Autowired
    private SettlementRepository settlementRepository;

    @Override
    public GroupResponse createGroup(GroupRequest request, String email) {

        // Find logged-in user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        // Create Group karega ye
        ExpenseGroup group = new ExpenseGroup();
        group.setGroupName(request.getGroupName());
        group.setDescription(request.getDescription());
        group.setCreatedBy(user);

        // Save Group
        ExpenseGroup savedGroup = groupRepository.save(group);

        // Automatically add group creator as first member
        GroupMember owner = new GroupMember();
        owner.setExpenseGroup(savedGroup);
        owner.setUser(user);

        groupMemberRepository.save(owner);

        // Prepare Response
        GroupResponse response = new GroupResponse();
        response.setId(savedGroup.getId());
        response.setGroupName(savedGroup.getGroupName());
        response.setDescription(savedGroup.getDescription());
        response.setCreatedBy(user.getName());

        return response;
    }
    @Override
    public List<GroupResponse> getAllGroups(String email) {

        // Logged-in user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        // Get all groups created by this user
        List<ExpenseGroup> groups =
                groupRepository.findByCreatedBy(user);

        List<GroupResponse> responseList = new ArrayList<>();

        for (ExpenseGroup group : groups) {

            GroupResponse response = new GroupResponse();

            response.setId(group.getId());
            response.setGroupName(group.getGroupName());
            response.setDescription(group.getDescription());
            response.setCreatedBy(group.getCreatedBy().getName());

            responseList.add(response);
        }

        return responseList;
    }
//    @Override
//    
//    public String deleteGroup(Long groupId, String email) {
//
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() ->
//                        new UserNotFoundException("User not found"));
//
//        ExpenseGroup group = groupRepository.findById(groupId)
//                .orElseThrow(() ->
//                        new RuntimeException("Group not found"));
//
//        if(!group.getCreatedBy().getId().equals(user.getId())){
//
//            throw new RuntimeException("You can delete only your own groups.");
//
//        }
//
//        groupRepository.delete(group);
//
//        return "Group Deleted Successfully";
//    }
    @Override
    public String deleteGroup(Long groupId, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        ExpenseGroup group = groupRepository.findById(groupId)
                .orElseThrow(() ->
                        new RuntimeException("Group not found"));

        // Check ownership
        if (!group.getCreatedBy().getId().equals(user.getId())) {

            throw new RuntimeException("You can delete only your own groups.");

        }

        // Delete settlements
        settlementRepository.deleteByExpenseGroup(group);

        // Delete expense splits
        expenseSplitRepository.deleteByExpense_ExpenseGroup(group);

        // Delete expenses
        expenseRepository.deleteByExpenseGroup(group);

        // Delete group members
        groupMemberRepository.deleteByExpenseGroup(group);

        // Finally delete group
        groupRepository.delete(group);

        return "Group Deleted Successfully";

    }
}