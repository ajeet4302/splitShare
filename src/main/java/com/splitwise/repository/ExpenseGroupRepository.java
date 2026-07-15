package com.splitwise.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.splitwise.entity.ExpenseGroup;
import com.splitwise.entity.User;

public interface ExpenseGroupRepository
        extends JpaRepository<ExpenseGroup, Long> {

    Optional<ExpenseGroup> findById(Long id);

    List<ExpenseGroup> findByCreatedBy(User createdBy);

}