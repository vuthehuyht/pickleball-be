package com.macrace.pickleball.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.macrace.pickleball.constant.ErrorCode;
import com.macrace.pickleball.dto.request.RegisterRequest;
import com.macrace.pickleball.service.UserService;
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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[1]").value("Phone number is required"));
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
}
