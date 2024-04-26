package com.macrace.pickleball.controller;

import com.macrace.pickleball.dto.request.LoginRequest;
import com.macrace.pickleball.dto.request.RegisterRequest;
import com.macrace.pickleball.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AuthController {
    private final UserService userService;

    @PostMapping(path = "/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Register new account request");
        return new ResponseEntity<>(userService.createNewUser(request), HttpStatus.CREATED);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest request) {
        log.info("User logging in request");
        return new ResponseEntity<>(userService.loginUser(request), HttpStatus.OK);
    }
}
