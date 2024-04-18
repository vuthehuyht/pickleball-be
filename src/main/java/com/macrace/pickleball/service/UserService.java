package com.macrace.pickleball.service;

import com.macrace.pickleball.dto.request.RegisterRequest;
import com.macrace.pickleball.dto.response.RegisterResponse;

public interface UserService {
    RegisterResponse createNewUser(RegisterRequest request);
}
