package com.macrace.pickleball.service.impl;

import com.macrace.pickleball.dto.request.RegisterRequest;
import com.macrace.pickleball.dto.response.RegisterResponse;
import com.macrace.pickleball.exceptions.PhoneNumberExistException;
import com.macrace.pickleball.model.User;
import com.macrace.pickleball.repository.UserRepository;
import com.macrace.pickleball.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public RegisterResponse createNewUser(RegisterRequest request) {
        Optional<User> userOptional = userRepository.findByPhoneNumber(request.getPhoneNumber());
        if (userOptional.isPresent()) {
            throw new PhoneNumberExistException();
        }

        User newUser = User.builder()
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .build();
        userRepository.save(newUser);
        return new RegisterResponse("New account is registered successfully");
    }
}
