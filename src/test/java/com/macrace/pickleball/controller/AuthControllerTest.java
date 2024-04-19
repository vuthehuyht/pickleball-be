package com.macrace.pickleball.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.macrace.pickleball.constant.ErrorCode;
import com.macrace.pickleball.dto.request.LoginRequest;
import com.macrace.pickleball.dto.request.RegisterRequest;
import com.macrace.pickleball.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthControllerTest {
    @Autowired
    private AuthController authController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;

    @BeforeEach
    void init() {
        RegisterRequest request = new RegisterRequest();
        request.setPhoneNumber("0972808478");
        request.setPassword("123456");
        request.setFullName("User Test");

        userService.createNewUser(request);
    }

    @Test
    void authControllerInitializedCorrectly() {
        assertThat(authController).isNotNull();
    }

    @Test
    void testAllFieldsBlank() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setPhoneNumber("");
        request.setPassword("");
        request.setFullName("");

        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPhoneNumberBlank() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setPassword("123456");
        request.setFullName("test");

        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPhoneNumberInvalid() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setPhoneNumber("0123456");
        request.setPassword("123456");
        request.setFullName("test");

        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Invalid phone number format"));
    }

    @Test
    void testPasswordBlank() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setPhoneNumber("0972808477");
        request.setPassword("");
        request.setFullName("test");

        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Password is required"));
    }

    @Test
    void testFullnameBlank() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setPhoneNumber("0972808477");
        request.setPassword("123456");
        request.setFullName("");

        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Full name is required"));
    }

    @Test
    void testRegisterValid() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setPhoneNumber("0972808477");
        request.setPassword("123456");
        request.setFullName("User Test");

        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("New account is registered successfully"));
    }

    @Test
    void testPhoneNumberExist() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setPhoneNumber("0972808477");
        request.setPassword("123456");
        request.setFullName("User Test");

        userService.createNewUser(request);

        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_code").value(ErrorCode.PHONE_NUMBER_EXIST))
                .andExpect(jsonPath("$.message").value("Phone number exists"));
    }

    @Test
    void testLoginAllFieldBlank() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setPhoneNumber("");
        request.setPassword("");

        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginPhoneNumberBlank() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setPhoneNumber("");
        request.setPassword("1234556");

        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginPasswordBlank() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setPhoneNumber("0972809810");
        request.setPassword("");

        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Password is required"));
    }

    @Test
    void testLoginPhoneNumberNotFound() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setPhoneNumber("0972809810");
        request.setPassword("123456");

        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error_code").value(ErrorCode.PHONE_NUMBER_NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Phone number not found"));
    }

    @Test
    void testLoginPhoneNumberOrPasswordWrong() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setPhoneNumber("0972808478");
        request.setPassword("123456789");

        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_code").value(ErrorCode.USERNAME_PASSWORD_WRONG))
                .andExpect(jsonPath("$.message").value("Phone number or password incorrect"));
    }

    @Test
    void testLoginValid() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setPhoneNumber("0972808478");
        request.setPassword("123456");

        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk());
    }
}
