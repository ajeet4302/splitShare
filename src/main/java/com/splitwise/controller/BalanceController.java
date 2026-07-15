package com.splitwise.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.splitwise.dto.BalanceResponse;
import com.splitwise.service.BalanceService;

@RestController
@RequestMapping("/balance")
public class BalanceController {

    @Autowired
    private BalanceService balanceService;

    @GetMapping("/{groupId}")
    public ResponseEntity<List<BalanceResponse>> getBalance(
            @PathVariable Long groupId) {

        return ResponseEntity.ok(
                balanceService.calculateBalance(groupId));
    }
}