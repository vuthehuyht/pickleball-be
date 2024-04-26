package com.macrace.pickleball.service;

import com.macrace.pickleball.model.Token;
import com.macrace.pickleball.model.User;
import com.macrace.pickleball.repository.TokenRepository;
import com.macrace.pickleball.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class JwtServiceTest {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void init() {
        User user = User.builder()
                .phoneNumber("0972808477")
                .password(passwordEncoder.encode("123456"))
                .fullName("User Test")
                .build();
        userRepository.save(user);
    }

    @Test
    void userServiceInitializedCorrectly() {
        assertThat(jwtService).isNotNull();
    }

    @Test
    void whenGenerateToken_returnAccessKey() {
        User user = userRepository.findByPhoneNumber("0972808477").get();
        jwtService.generateToken(user);

        List<Token> tokens = tokenRepository.findAllTokenByUserId(1);

        assertThat(tokens).isNotNull();
    }

    @Test
    void whenIsValidToken_returnBoolean() {
        User user = userRepository.findByPhoneNumber("0972808477").get();
        String token = jwtService.generateToken(user);

        assertThat(jwtService.isValidToken(token)).isEqualTo(true);
    }
}
