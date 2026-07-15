package com.splitwise.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.splitwise.entity.ExpenseGroup;
import com.splitwise.entity.GroupMember;
import com.splitwise.entity.User;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long>{

    Optional<GroupMember> findByExpenseGroupAndUser(
            ExpenseGroup expenseGroup,
            User user);
    List<GroupMember> findByExpenseGroup(ExpenseGroup expenseGroup);
    void deleteByExpenseGroup(ExpenseGroup expenseGroup);

}