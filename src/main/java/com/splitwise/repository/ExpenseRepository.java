package com.splitwise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.splitwise.entity.Expense;
import com.splitwise.entity.ExpenseGroup;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByExpenseGroup(ExpenseGroup expenseGroup);

    void deleteByExpenseGroup(ExpenseGroup expenseGroup);

}