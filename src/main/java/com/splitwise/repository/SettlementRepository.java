package com.splitwise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.splitwise.entity.ExpenseGroup;
import com.splitwise.entity.Settlement;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {

    List<Settlement> findByExpenseGroup(ExpenseGroup group);

    void deleteByExpenseGroup(ExpenseGroup group);

}