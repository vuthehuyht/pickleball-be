package com.macrace.pickleball.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.macrace.pickleball.exceptions.CAccessDeniedHandler;
import com.macrace.pickleball.filter.JwtAuthenticationFilter;
import com.macrace.pickleball.repository.TokenRepository;
import com.macrace.pickleball.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class FilterChainConfig {
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;
    private final AuthenticationProvider authenticationProvider;
    private final TokenRepository tokenRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/demo").permitAll()
                        .requestMatchers("/api/v1/auth/register").permitAll()
                        .requestMatchers("/api/v1/auth/login").permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(handling -> handling.accessDeniedHandler(new CAccessDeniedHandler()))
                .httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.authenticationEntryPoint(
                        (((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED)))
                ))
                .addFilterBefore(new JwtAuthenticationFilter(jwtService, objectMapper, jwtConfig, tokenRepository), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
