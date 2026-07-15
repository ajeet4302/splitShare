package com.splitwise.service;

import com.splitwise.dto.LoginRequest;
import com.splitwise.dto.LoginResponse;
import com.splitwise.dto.UpdateProfileRequest;
import com.splitwise.dto.UserProfileResponse;
import com.splitwise.dto.UserRequest;
import com.splitwise.dto.UserResponse;

public interface UserService {

    UserResponse register(UserRequest request);
    LoginResponse login(LoginRequest request);
    UserProfileResponse getProfile(String email);
    String updateProfile(UpdateProfileRequest request, String email);


}

