package com.splitwise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.splitwise.dto.LoginRequest;
import com.splitwise.dto.LoginResponse;
import com.splitwise.dto.UpdateProfileRequest;
import com.splitwise.dto.UserProfileResponse;
import com.splitwise.dto.UserRequest;
import com.splitwise.dto.UserResponse;
import com.splitwise.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRequest request){

        UserResponse response = service.register(request);

        return new ResponseEntity<>(response,HttpStatus.CREATED);

    }
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

        LoginResponse response = service.login(request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile(
            Authentication authentication) {

        String email = authentication.getName();

        UserProfileResponse response = service.getProfile(email);

        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        String response = service.updateProfile(request, email);
        
        

        return ResponseEntity.ok(response);
    }
}