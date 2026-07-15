package com.splitwise.service;

import java.util.List;

import com.splitwise.dto.BalanceResponse;
import com.splitwise.dto.ExpenseRequest;
import com.splitwise.dto.ExpenseResponse;

public interface ExpenseService {

    String addExpense(ExpenseRequest request, String loggedInEmail);

    List<ExpenseResponse> getGroupExpenses(Long groupId);
 
    List<BalanceResponse> getGroupBalance(Long groupId);

}