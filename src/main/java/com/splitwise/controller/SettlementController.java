package com.splitwise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.splitwise.dto.SettlementRequest;
import com.splitwise.dto.SettlementResponse;
import com.splitwise.service.SettlementService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/settlements")
public class SettlementController {

    @Autowired
    private SettlementService settlementService;

    @PostMapping
    public ResponseEntity<SettlementResponse> settleUp(
            @Valid @RequestBody SettlementRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        SettlementResponse response =
                settlementService.settleUp(request, email);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}