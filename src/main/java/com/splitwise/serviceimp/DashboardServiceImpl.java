package com.splitwise.serviceimp;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.splitwise.dto.DashboardResponse;
import com.splitwise.entity.Expense;
import com.splitwise.repository.ExpenseGroupRepository;
import com.splitwise.repository.ExpenseRepository;
import com.splitwise.repository.GroupMemberRepository;
import com.splitwise.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private ExpenseGroupRepository groupRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private GroupMemberRepository memberRepository;

    @Override
    public DashboardResponse getDashboard() {

        DashboardResponse response = new DashboardResponse();

        response.setTotalGroups(groupRepository.count());

        response.setTotalExpenses(expenseRepository.count());

        response.setTotalMembers(memberRepository.count());

        BigDecimal totalAmount = expenseRepository.findAll()
                .stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        response.setTotalAmountSpent(totalAmount);

        return response;
    }
}