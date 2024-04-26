package com.macrace.pickleball.service;

import com.macrace.pickleball.dto.request.LoginRequest;
import com.macrace.pickleball.dto.request.RegisterRequest;
import com.macrace.pickleball.dto.response.LoginResponse;
import com.macrace.pickleball.dto.response.RegisterResponse;

public interface UserService {
    RegisterResponse createNewUser(RegisterRequest request);

    LoginResponse loginUser(LoginRequest request);
}
