package com.macrace.pickleball.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.macrace.pickleball.constant.ErrorCode;
import com.macrace.pickleball.dto.request.FacilityRequest;
import com.macrace.pickleball.dto.request.LoginRequest;
import com.macrace.pickleball.dto.request.RegisterRequest;
import com.macrace.pickleball.dto.request.YardRequest;
import com.macrace.pickleball.dto.response.AddFacilityResponse;
import com.macrace.pickleball.dto.response.LoginResponse;
import com.macrace.pickleball.dto.response.YardResponse;
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
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class YardControllerTest {
    @Autowired
    private YardController yardController;

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
    void yardControllerInitializedCorrectly() {
        assertThat(yardController).isNotNull();
    }

    @Test
    void testYardNameBlank() throws Exception {
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

        YardRequest entity2 = new YardRequest();
        entity2.setName("");
        entity2.setFacilityId(1);

        mockMvc.perform(post("/api/v1/yard")
                        .header("Authorization", "Bearer " + Objects.requireNonNull(response.getBody()).accessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(entity2)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message[0]").value("Yard name is required"));
    }

    @Test
    void testFacilityIdBlank() throws Exception {
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

        YardRequest entity2 = new YardRequest();
        entity2.setName("yard name");

        mockMvc.perform(post("/api/v1/yard")
                        .header("Authorization", "Bearer " + Objects.requireNonNull(response.getBody()).accessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(entity2)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message[0]").value("Facility id is required"));;
    }

    @Test
    void testFacilityNotFound() throws Exception {
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

        YardRequest entity2 = new YardRequest();
        entity2.setName("yard name");
        entity2.setFacilityId(10);

        mockMvc.perform(post("/api/v1/yard")
                        .header("Authorization", "Bearer " + Objects.requireNonNull(response.getBody()).accessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(entity2)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Facility not found"))
                .andExpect(jsonPath("$.error_code").value(ErrorCode.FACILITY_NOT_FOUND));
    }

    @Test
    void testAddYardValid() throws Exception {
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

        YardRequest entity2 = new YardRequest();
        entity2.setName("yard name");
        entity2.setFacilityId(1);

        mockMvc.perform(post("/api/v1/yard")
                        .header("Authorization", "Bearer " + Objects.requireNonNull(response.getBody()).accessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(entity2)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("New yard was added successfully"));
    }

    @Test
    void testGetAllYard() throws Exception {
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

        YardRequest entity2 = new YardRequest();
        entity2.setName("yard name");
        entity2.setFacilityId(1);
        HttpHeaders headers2 = new HttpHeaders();
        headers2.set("Authorization", "Bearer " + response.getBody().accessToken());
        HttpEntity<YardRequest> request2 = new HttpEntity<>(entity2, headers2);
        restTemplate.postForEntity(
                new URI("http://localhost:" + randomServerPort + "/api/v1/yard"),
                request2, YardResponse.class);

        mockMvc.perform(get("/api/v1/yards")
                        .header("Authorization", "Bearer " + Objects.requireNonNull(response.getBody()).accessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("yard name")))
                .andExpect(jsonPath("$[0].state", is("READY")));
    }

    @Test
    void testUpdateYard() throws Exception {
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

        YardRequest entity2 = new YardRequest();
        entity2.setName("yard name");
        entity2.setFacilityId(1);
        HttpHeaders headers2 = new HttpHeaders();
        headers2.set("Authorization", "Bearer " + response.getBody().accessToken());
        HttpEntity<YardRequest> request2 = new HttpEntity<>(entity2, headers2);
        restTemplate.postForEntity(
                new URI("http://localhost:" + randomServerPort + "/api/v1/yard"),
                request2, YardResponse.class);

        YardRequest request3 = new YardRequest();
        entity2.setName("yard name 1");

        mockMvc.perform(put("/api/v1/yard/1")
                        .header("Authorization", "Bearer " + Objects.requireNonNull(response.getBody()).accessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request3)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Updating yard done")));
    }
}
