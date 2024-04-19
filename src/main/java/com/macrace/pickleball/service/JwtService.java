package com.macrace.pickleball.service;

import com.macrace.pickleball.model.User;
import io.jsonwebtoken.Claims;

import java.security.Key;

public interface JwtService {
    Claims extractClaims(String token);

    Key getKey();

    String generateToken(User user);

    String refreshToken(User user);

    boolean isValidToken(String token);
}
