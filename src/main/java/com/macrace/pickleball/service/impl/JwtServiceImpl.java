package com.macrace.pickleball.service.impl;

import com.macrace.pickleball.config.JwtConfig;
import com.macrace.pickleball.exceptions.GeneralException;
import com.macrace.pickleball.model.User;
import com.macrace.pickleball.repository.UserRepository;
import com.macrace.pickleball.service.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl extends JwtConfig implements JwtService {
    private final UserRepository userRepository;

    @Override
    public Claims extractClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public Key getKey() {
        byte[] keys = Decoders.BASE64.decode(getSecret());
        return Keys.hmacShaKeyFor(keys);
    }

    @Override
    public String generateToken(User user) {
        Instant currentDateTime = Instant.now();
        return Jwts.builder()
                .setSubject(user.getPhoneNumber())
                .setIssuedAt(Date.from(currentDateTime))
                .setExpiration(Date.from(currentDateTime.plusSeconds(getExpiration())))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String refreshToken(User user) {
        return "";
    }

    @Override
    public boolean isValidToken(String token) {
        final String username = extractUsernameByToken(token);
        Optional<User> userOptional = userRepository.findByPhoneNumber(username);

        return userOptional.isPresent();
    }

    private String extractUsernameByToken(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractClaimsByToken(token);
        return claimsTFunction.apply(claims);
    }

    private Claims extractClaimsByToken(String token) {
        var unauthorized = String.valueOf(HttpStatus.UNAUTHORIZED.value());
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (UnsupportedJwtException e) {
            log.error("Not support JWT token {}", e.getLocalizedMessage());
            throw new GeneralException("Token not support", unauthorized);
        } catch (SignatureException | MalformedJwtException e) {
            log.error("Token is invalid format {}", e.getLocalizedMessage());
            throw new GeneralException("Token is invalid format", unauthorized);
        } catch (ExpiredJwtException e) {
            log.error("Token is expired {}", e.getLocalizedMessage());
            throw new GeneralException("Token is expired", unauthorized);
        } catch (Exception e) {
            log.error("{}", e.getLocalizedMessage());
            throw new GeneralException("Unknown error", unauthorized);
        }
    }
}
