package com.splitwise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.splitwise.dto.BalanceResponse;
import com.splitwise.dto.ExpenseRequest;
import com.splitwise.service.ExpenseService;

import jakarta.validation.Valid;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.splitwise.dto.ExpenseResponse;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService service;

    @PostMapping
    public ResponseEntity<String> addExpense(
            @Valid @RequestBody ExpenseRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        String response = service.addExpense(request, email);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<ExpenseResponse>> getGroupExpenses(
            @PathVariable Long groupId) {

        List<ExpenseResponse> response = service.getGroupExpenses(groupId);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/balance/{groupId}")
    public ResponseEntity<List<BalanceResponse>> getBalance(
            @PathVariable Long groupId) {

        List<BalanceResponse> response =
                service.getGroupBalance(groupId);

        return ResponseEntity.ok(response);
    }

}