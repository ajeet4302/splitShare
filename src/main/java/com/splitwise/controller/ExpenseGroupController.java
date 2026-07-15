package com.splitwise.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.splitwise.dto.GroupRequest;
import com.splitwise.dto.GroupResponse;
import com.splitwise.service.ExpenseGroupService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/groups")
public class ExpenseGroupController {

    @Autowired
    private ExpenseGroupService service;

    @PostMapping
    public ResponseEntity<GroupResponse> createGroup(
            @Valid @RequestBody GroupRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        GroupResponse response = service.createGroup(request, email);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<GroupResponse>> getAllGroups(
            Authentication authentication) {

        String email = authentication.getName();

        List<GroupResponse> response = service.getAllGroups(email);

        return ResponseEntity.ok(response);
    }
    
//    @DeleteMapping("/{groupId}")
//    public ResponseEntity<String> deleteGroup(
//            @PathVariable Long groupId,
//            Authentication authentication){
//
//        String email = authentication.getName();
//
//        String response = service.deleteGroup(groupId, email);
//
//        return ResponseEntity.ok(response);
//    }
//    
    @DeleteMapping("/{groupId}")
    public ResponseEntity<String> deleteGroup(
            @PathVariable Long groupId,
            Authentication authentication){

        String email = authentication.getName();

        String response = service.deleteGroup(groupId, email);

        return ResponseEntity.ok(response);
    }
}