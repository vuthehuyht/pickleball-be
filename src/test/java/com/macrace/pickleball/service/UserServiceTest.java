package com.macrace.pickleball.service;

import com.macrace.pickleball.dto.request.RegisterRequest;
import com.macrace.pickleball.model.User;
import com.macrace.pickleball.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

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
        assertThat(userService).isNotNull();
    }

    @DisplayName("test create new user")
    @Test
    void testCreateNewUser() {
        RegisterRequest request = new RegisterRequest();
        request.setPhoneNumber("0972808478");
        request.setPassword("123456");
        request.setFullName("User Test");

        userService.createNewUser(request);
        User user = userRepository.findByPhoneNumber("0972808478").get();

        assertThat(user.getPhoneNumber()).isEqualTo("0972808478");
        assertThat(user.getFullName()).isEqualTo("User Test");
    }
}
