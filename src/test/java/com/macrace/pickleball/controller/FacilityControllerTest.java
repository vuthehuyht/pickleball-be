package com.macrace.pickleball.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.macrace.pickleball.dto.request.FacilityRequest;
import com.macrace.pickleball.dto.request.LoginRequest;
import com.macrace.pickleball.dto.request.RegisterRequest;
import com.macrace.pickleball.dto.response.AddFacilityResponse;
import com.macrace.pickleball.dto.response.LoginResponse;
import com.macrace.pickleball.repository.UserRepository;
import com.macrace.pickleball.service.JwtService;
import com.macrace.pickleball.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FacilityControllerTest {
    @Autowired
    private FacilityController facilityController;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    int randomServerPort;

    @BeforeEach
    void init() {
        RegisterRequest request = new RegisterRequest();
        request.setPhoneNumber("0972808478");
        request.setPassword("123456");
        request.setFullName("User Test");

        userService.createNewUser(request);
    }

    @Test
    void facilityControllerInitializedCorrectly() {
        assertThat(facilityController).isNotNull();
    }

    @Test
    void testFacilityNameBlank() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        LoginRequest request = new LoginRequest();
        request.setPhoneNumber("0972808478");
        request.setPassword("123456");
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
                new URI("http://localhost:" + randomServerPort + "/api/v1/auth/login"),
                request, LoginResponse.class);

        FacilityRequest request2 = new FacilityRequest();
        request2.setName("");
        request2.setAddress("address test");
        request2.setPhoneNumber("0972808490");

        mockMvc.perform(post("/api/v1/facility")
                        .header("Authorization", "Bearer " + Objects.requireNonNull(response.getBody()).accessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsBytes(request2)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Facility name is required"));
    }

    @Test
    void testFacilityAddressBlank() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        LoginRequest request = new LoginRequest();
        request.setPhoneNumber("0972808478");
        request.setPassword("123456");
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
                new URI("http://localhost:" + randomServerPort + "/api/v1/auth/login"),
                request, LoginResponse.class);

        FacilityRequest request2 = new FacilityRequest();
        request2.setName("facility test");
        request2.setAddress("");
        request2.setPhoneNumber("0972808490");

        mockMvc.perform(post("/api/v1/facility")
                        .header("Authorization", "Bearer " + Objects.requireNonNull(response.getBody()).accessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsBytes(request2)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Facility address is required"));
    }

    @Test
    void testFacilityPhoneNumberBlank() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        LoginRequest request = new LoginRequest();
        request.setPhoneNumber("0972808478");
        request.setPassword("123456");
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
                new URI("http://localhost:" + randomServerPort + "/api/v1/auth/login"),
                request, LoginResponse.class);

        FacilityRequest request2 = new FacilityRequest();
        request2.setName("facility test");
        request2.setAddress("facility address test");
        request2.setPhoneNumber("");

        mockMvc.perform(post("/api/v1/facility")
                        .header("Authorization", "Bearer " + Objects.requireNonNull(response.getBody()).accessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsBytes(request2)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Phone number is required"));
    }

    @Test
    void testAddFacility() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        LoginRequest request = new LoginRequest();
        request.setPhoneNumber("0972808478");
        request.setPassword("123456");
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
                new URI("http://localhost:" + randomServerPort + "/api/v1/auth/login"),
                request, LoginResponse.class);

        FacilityRequest request2 = new FacilityRequest();
        request2.setName("test");
        request2.setAddress("facility test");
        request2.setPhoneNumber("0972808490");

        mockMvc.perform(post("/api/v1/facility")
                        .header("Authorization", "Bearer " + Objects.requireNonNull(response.getBody()).accessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request2)))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetAllFacility() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        LoginRequest request = new LoginRequest();
        request.setPhoneNumber("0972808478");
        request.setPassword("123456");
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
                new URI("http://localhost:" + randomServerPort + "/api/v1/auth/login"),
                request, LoginResponse.class);

        FacilityRequest entity = new FacilityRequest();
        entity.setName("test 1");
        entity.setAddress("facility test");
        entity.setPhoneNumber("0972808490");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + response.getBody().accessToken());
        HttpEntity<FacilityRequest> request1 = new HttpEntity<>(entity, headers);
        restTemplate.postForEntity(
                new URI("http://localhost:" + randomServerPort + "/api/v1/facility"),
                request1, AddFacilityResponse.class);

        mockMvc.perform(get("/api/v1/facilities")
                        .header("Authorization", "Bearer " + Objects.requireNonNull(response.getBody()).accessToken()))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateFacility_FacilityNotFound() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        LoginRequest request = new LoginRequest();
        request.setPhoneNumber("0972808478");
        request.setPassword("123456");
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
                new URI("http://localhost:" + randomServerPort + "/api/v1/auth/login"),
                request, LoginResponse.class);

        FacilityRequest request2 = new FacilityRequest();
        request2.setName("test 2");

        mockMvc.perform(put("/api/v1/facility/10")
                        .header("Authorization", "Bearer " + Objects.requireNonNull(response.getBody()).accessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request2)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateFacility_Success() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        LoginRequest request = new LoginRequest();
        request.setPhoneNumber("0972808478");
        request.setPassword("123456");
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
                new URI("http://localhost:" + randomServerPort + "/api/v1/auth/login"),
                request, LoginResponse.class);

        FacilityRequest entity = new FacilityRequest();
        entity.setName("test 1");
        entity.setAddress("facility test");
        entity.setPhoneNumber("0972808490");


        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + response.getBody().accessToken());
        HttpEntity<FacilityRequest> request1 = new HttpEntity<>(entity, headers);
        restTemplate.postForEntity(
                new URI("http://localhost:" + randomServerPort + "/api/v1/facility"),
                request1, AddFacilityResponse.class);

        FacilityRequest request2 = new FacilityRequest();
        request2.setName("test 2");
        request2.setAddress("facility test");
        request2.setPhoneNumber("0972808490");

        mockMvc.perform(put("/api/v1/facility/1")
                        .header("Authorization", "Bearer " + Objects.requireNonNull(response.getBody()).accessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request2)))
                .andExpect(status().isOk());
    }
}
