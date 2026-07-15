package com.splitwise.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.splitwise.dto.GroupMemberResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.splitwise.dto.AddMemberRequest;
import com.splitwise.service.GroupMemberService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/groups")
public class GroupMemberController {

    @Autowired
    private GroupMemberService service;

    @PostMapping("/{groupId}/members")
    public ResponseEntity<String> addMember(
            @PathVariable Long groupId,
            @Valid @RequestBody AddMemberRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        request.setGroupId(groupId);

        String response = service.addMember(request, email);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<GroupMemberResponse>> getMembers(
            @PathVariable Long groupId) {

        return ResponseEntity.ok(service.getGroupMembers(groupId));
    }
}