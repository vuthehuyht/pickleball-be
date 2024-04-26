package com.macrace.pickleball.service.impl;

import com.macrace.pickleball.dto.request.LoginRequest;
import com.macrace.pickleball.dto.request.RegisterRequest;
import com.macrace.pickleball.dto.response.LoginResponse;
import com.macrace.pickleball.dto.response.RegisterResponse;
import com.macrace.pickleball.exceptions.PhoneNumberExistException;
import com.macrace.pickleball.exceptions.PhoneNumberNotFoundException;
import com.macrace.pickleball.exceptions.UsernameOrPasswordWrongException;
import com.macrace.pickleball.model.Token;
import com.macrace.pickleball.model.User;
import com.macrace.pickleball.repository.TokenRepository;
import com.macrace.pickleball.repository.UserRepository;
import com.macrace.pickleball.service.JwtService;
import com.macrace.pickleball.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;

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

    @Override
    public LoginResponse loginUser(LoginRequest request) {
        User user = getUserByPhoneNumber(request.getPhoneNumber());
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UsernameOrPasswordWrongException();
        }

        List<Token> tokens = tokenRepository.findAllTokenByUserId(user.getId());
        tokens.forEach(token -> {
            token.setExpired(true);
        });
        tokenRepository.saveAll(tokens);

        var accessToken = jwtService.generateToken(user);
        Token token = Token.builder()
                .user(user)
                .accessToken(accessToken)
                .expired(false)
                .build();
        tokenRepository.save(token);

        return new LoginResponse(accessToken);
    }

    private User getUserByPhoneNumber(String phoneNumber) {
        Optional<User> userOptional = userRepository.findByPhoneNumber(phoneNumber);
        if (userOptional.isEmpty()) {
            throw new PhoneNumberNotFoundException();
        }

        return userOptional.get();
    }
}
