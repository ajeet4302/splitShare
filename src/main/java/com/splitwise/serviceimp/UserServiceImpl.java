
package com.splitwise.serviceimp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.splitwise.dto.LoginRequest;
import com.splitwise.dto.LoginResponse;
import com.splitwise.dto.UpdateProfileRequest;
import com.splitwise.dto.UserProfileResponse;
import com.splitwise.dto.UserRequest;
import com.splitwise.dto.UserResponse;
import com.splitwise.entity.User;
import com.splitwise.exception.InvalidCredentialsException;
import com.splitwise.exception.UserAlreadyExistsException;
import com.splitwise.exception.UserNotFoundException;
import com.splitwise.repository.UserRepository;
import com.splitwise.security.JwtUtil;
import com.splitwise.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public UserResponse register(UserRequest request) {

        if(repository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists.");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());

        User savedUser = repository.save(user);

        UserResponse response = new UserResponse();

        response.setId(savedUser.getId());
        response.setName(savedUser.getName());
        response.setEmail(savedUser.getEmail());
        response.setPhone(savedUser.getPhone());

        return response;
    }
    
    @Override
    public LoginResponse login(LoginRequest request) {

        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        LoginResponse response = new LoginResponse();

        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setMessage("Login Successful");
        response.setToken(token);

        return response;
    }
    @Override
    public UserProfileResponse getProfile(String email) {

        User user = repository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        UserProfileResponse response = new UserProfileResponse();

        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());

        return response;
    }
    
    @Override
    public String updateProfile(UpdateProfileRequest request, String email) {

        User user = repository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        user.setName(request.getName());
        user.setPhone(request.getPhone());

        repository.save(user);

        return "Profile Updated Successfully";
    }
     

}