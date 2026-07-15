package com.splitwise.serviceimp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.splitwise.dto.BalanceResponse;
import com.splitwise.entity.Expense;
import com.splitwise.entity.ExpenseGroup;
import com.splitwise.entity.ExpenseSplit;
import com.splitwise.entity.GroupMember;
import com.splitwise.entity.User;
import com.splitwise.exception.GroupNotFoundException;
import com.splitwise.repository.ExpenseGroupRepository;
import com.splitwise.repository.ExpenseRepository;
import com.splitwise.repository.ExpenseSplitRepository;
import com.splitwise.repository.GroupMemberRepository;
import com.splitwise.service.BalanceService;
import com.splitwise.entity.Settlement;
import com.splitwise.repository.SettlementRepository;

@Service
public class BalanceServiceImpl implements BalanceService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseSplitRepository splitRepository;

    @Autowired
    private ExpenseGroupRepository groupRepository;

    @Autowired
    private GroupMemberRepository memberRepository;
    
    @Autowired
    private SettlementRepository settlementRepository;

    @Override
    public List<BalanceResponse> calculateBalance(Long groupId) {

        // Find the group
        ExpenseGroup group = groupRepository.findById(groupId)
                .orElseThrow(() ->
                        new GroupNotFoundException("Group not found"));

        // Get all expenses of the group
        List<Expense> expenses = expenseRepository.findByExpenseGroup(group);

        // Store total paid by each user
        Map<User, BigDecimal> paidMap = new HashMap<>();

        // Store total owed by each user
        Map<User, BigDecimal> oweMap = new HashMap<>();
        
     // Store total settlement paid by each user
        Map<User, BigDecimal> settlementPaidMap = new HashMap<>();

        // Store total settlement received by each user
        Map<User, BigDecimal> settlementReceivedMap = new HashMap<>();

        // Process every expense
        for (Expense expense : expenses) {

            User paidBy = expense.getPaidBy();

            // Add amount paid
            paidMap.put(
                    paidBy,
                    paidMap.getOrDefault(paidBy, BigDecimal.ZERO)
                            .add(expense.getAmount()));

            // Get all splits for this expense
            List<ExpenseSplit> splits =
                    splitRepository.findByExpense(expense);

            // Add amount owed by every user
            for (ExpenseSplit split : splits) {

                User user = split.getUser();

                oweMap.put(
                        user,
                        oweMap.getOrDefault(user, BigDecimal.ZERO)
                                .add(split.getAmountOwed()));
            }
        }

        // Get all group members
        List<GroupMember> members = memberRepository.findByExpenseGroup(group);

        List<BalanceResponse> responseList = new ArrayList<>();

        // Calculate balance for every member
        for (GroupMember member : members) {

            User user = member.getUser();

            BigDecimal paid =
                    paidMap.getOrDefault(user, BigDecimal.ZERO);

            BigDecimal owes =
                    oweMap.getOrDefault(user, BigDecimal.ZERO);

            BigDecimal balance = paid.subtract(owes);

            BalanceResponse response = new BalanceResponse();

            response.setUserName(user.getName());
            response.setPaid(paid);
            response.setOwes(owes);
            response.setBalance(balance);

            responseList.add(response);
        }

        return responseList;
    }

}