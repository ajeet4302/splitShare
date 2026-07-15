package com.splitwise.serviceimp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.splitwise.dto.BalanceResponse;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.splitwise.dto.ExpenseResponse;
import com.splitwise.dto.ExpenseRequest;
import com.splitwise.entity.Expense;
import com.splitwise.entity.ExpenseGroup;
import com.splitwise.entity.ExpenseSplit;
import com.splitwise.entity.GroupMember;
import com.splitwise.entity.User;
import com.splitwise.exception.GroupNotFoundException;
import com.splitwise.exception.UnauthorizedException;
import com.splitwise.exception.UserNotFoundException;
import com.splitwise.repository.ExpenseGroupRepository;
import com.splitwise.repository.ExpenseRepository;
import com.splitwise.repository.ExpenseSplitRepository;
import com.splitwise.repository.GroupMemberRepository;
import com.splitwise.repository.UserRepository;
import com.splitwise.service.ExpenseService;

@Service
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseSplitRepository splitRepository;

    @Autowired
    private ExpenseGroupRepository groupRepository;

    @Autowired
    private GroupMemberRepository memberRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public String addExpense(ExpenseRequest request, String loggedInEmail) {

        // Logged in user hoga
        User loggedInUser = userRepository.findByEmail(loggedInEmail)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        //  Find group hoga
        ExpenseGroup group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() ->
                        new GroupNotFoundException("Group not found"));

        // it will Check user is member of group
        boolean isMember = memberRepository
                .findByExpenseGroup(group)
                .stream()
                .anyMatch(member ->
                        member.getUser().getId().equals(loggedInUser.getId()));

        if (!isMember) {
            throw new UnauthorizedException(
                    "You are not a member of this group.");
        }

        // 4. Save Expense
        Expense expense = new Expense();

        expense.setTitle(request.getTitle());
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setSplitType(request.getSplitType());

        expense.setPaidBy(loggedInUser);
        expense.setExpenseGroup(group);

        expenseRepository.save(expense);

        // Get all members
        List<GroupMember> members =
                memberRepository.findByExpenseGroup(group);

        // Equal Split
        BigDecimal share = request.getAmount().divide(
                BigDecimal.valueOf(members.size()),
                2,
                RoundingMode.HALF_UP);

        // Save splits
        for (GroupMember member : members) {

            ExpenseSplit split = new ExpenseSplit();

            split.setExpense(expense);
            split.setUser(member.getUser());
            split.setAmountOwed(share);

            splitRepository.save(split);

        }

        return "Expense Added Successfully.";

    }@Override
    public List<ExpenseResponse> getGroupExpenses(Long groupId) {

        ExpenseGroup group = groupRepository.findById(groupId)
                .orElseThrow(() ->
                        new GroupNotFoundException("Group not found"));

        List<Expense> expenses = expenseRepository.findByExpenseGroup(group);

        return expenses.stream().map(expense -> {

            ExpenseResponse response = new ExpenseResponse();

            response.setExpenseId(expense.getId());
            response.setTitle(expense.getTitle());
            response.setDescription(expense.getDescription());
            response.setAmount(expense.getAmount());
            response.setPaidBy(expense.getPaidBy().getName());
            response.setGroupName(expense.getExpenseGroup().getGroupName());
            response.setExpenseDate(expense.getExpenseDate());

            return response;

        }).toList();

    }
    @Override
    public List<BalanceResponse> getGroupBalance(Long groupId) {

        ExpenseGroup group = groupRepository.findById(groupId)
                .orElseThrow(() ->
                        new GroupNotFoundException("Group not found"));

        List<GroupMember> members =
                memberRepository.findByExpenseGroup(group);

        List<Expense> expenses =
                expenseRepository.findByExpenseGroup(group);
        
        System.out.println("========== BALANCE API ==========");
        System.out.println("Group ID = " + groupId);
        System.out.println("Members = " + members.size());
        System.out.println("Expenses = " + expenses.size());

        Map<Long, BigDecimal> balances = new HashMap<>();

        // Initialize everyone's balance to 0
        for (GroupMember member : members) {

            balances.put(
                    member.getUser().getId(),
                    BigDecimal.ZERO
            );

        }

        // Calculate balances
        for (Expense expense : expenses) {

            BigDecimal share = expense.getAmount().divide(
                    BigDecimal.valueOf(members.size()),
                    2,
                    java.math.RoundingMode.HALF_UP
            );

            // Person who paid gets the full amount
            Long paidById = expense.getPaidBy().getId();

            balances.put(
                    paidById,
                    balances.get(paidById).add(expense.getAmount())
            );

            // Everyone owes their share
            for (GroupMember member : members) {

                Long userId = member.getUser().getId();

                balances.put(
                        userId,
                        balances.get(userId).subtract(share)
                );

            }

        }

        List<BalanceResponse> response = new ArrayList<>();

        for (GroupMember member : members) {

            BalanceResponse balance = new BalanceResponse();

            Long userId = member.getUser().getId();

            BigDecimal finalBalance = balances.get(userId);

            balance.setUserName(member.getUser().getName());

            if (finalBalance.compareTo(BigDecimal.ZERO) > 0) {

                balance.setPaid(finalBalance);
                balance.setOwes(BigDecimal.ZERO);

            } else {

                balance.setPaid(BigDecimal.ZERO);
                balance.setOwes(finalBalance.abs());

            }

            balance.setBalance(finalBalance);

            response.add(balance);
        }
        
        System.out.println("Response Size = " + response.size());

        return response;
    }

}