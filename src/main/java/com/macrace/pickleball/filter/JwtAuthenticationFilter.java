package com.macrace.pickleball.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.macrace.pickleball.config.JwtConfig;
import com.macrace.pickleball.model.Token;
import com.macrace.pickleball.repository.TokenRepository;
import com.macrace.pickleball.service.JwtService;
import com.macrace.pickleball.util.Utils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final JwtConfig jwtConfig;
    private final TokenRepository tokenRepository;

    private final static List<String> ALLOW_PATH = new ArrayList<>() {{
        add("/api/v1/auth/login");
        add("/api/v1/auth/register");
        add("/api/v1/demo");
    }};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Start filter {}", request.getRequestURI());

        if (ALLOW_PATH.contains(request.getServletPath())) {
            log.info("End filter {}", request.getRequestURI());
            filterChain.doFilter(request, response);
        } else {
            var accessToken = request.getHeader(jwtConfig.getHeader());
            try {
                if (jwtService.isValidToken(accessToken.substring(7))) {
                    Optional<Token> tokenOptional = tokenRepository.findByAccessToken(accessToken);
                    if (tokenOptional.isPresent() && !tokenOptional.get().isExpired()) {
                        Claims claims = jwtService.extractClaims(accessToken);
                        var username = claims.getSubject();

                        if (username != null) {
                            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    Collections.emptyList()
                            );
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        }
                    } else {
                        log.error("Error message: Access token is unavailable");
                        var msgError = Utils.handleTokenUnavailable();
                        var msgJson = objectMapper.writeValueAsString(msgError);

                        returnResponse(response, msgJson);
                    }
                }
            } catch (Exception e) {
                log.error("Error message: {}", e.getLocalizedMessage());
                var msgError = Utils.handleUnauthorized();
                var msgJson = objectMapper.writeValueAsString(msgError);

                returnResponse(response, msgJson);
            } finally {
                log.info("End filter {}", request.getRequestURI());
                filterChain.doFilter(request, response);
            }
        }

//        var accessToken = request.getHeader(jwtConfig.getHeader());
//        if (accessToken != null && !accessToken.isBlank() && accessToken.startsWith(jwtConfig.getPrefix() + " ")) {
//            accessToken = accessToken.substring(7);
//
//            try {
//                if (jwtService.isValidToken(accessToken)) {
//                    Optional<Token> tokenOptional = tokenRepository.findByAccessToken(accessToken);
//                    if (tokenOptional.isPresent() && !tokenOptional.get().isExpired()) {
//                        Claims claims = jwtService.extractClaims(accessToken);
//                        var username = claims.getSubject();
//
//                        if (username != null) {
//                            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//                                    username,
//                                    null,
//                                    Collections.emptyList()
//                            );
//                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//                        }
//                    } else {
//                        log.error("Error message: Access token is unavailable");
//                        var msgError = Utils.handleTokenUnavailable();
//                        var msgJson = objectMapper.writeValueAsString(msgError);
//
//                        returnResponse(response, msgJson);
//                    }
//                }
//            } catch (Exception e) {
//                log.error("Error message: {}", e.getLocalizedMessage());
//                var msgError = Utils.handleUnauthorized();
//                var msgJson = objectMapper.writeValueAsString(msgError);
//
//                returnResponse(response, msgJson);
//            }
//
//            log.info("End filter {}", request.getRequestURI());
//            filterChain.doFilter(request, response);
//        } else {
//            log.error("Error message: Access token is blank or null");
//            var msgError = Utils.handleUnauthorized();
//            var msgJson = objectMapper.writeValueAsString(msgError);
//
//            returnResponse(response, msgJson);
//            log.info("End filter {}", request.getRequestURI());
//            filterChain.doFilter(request, response);
//        }
    }

    private void returnResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(message);
    }
}
