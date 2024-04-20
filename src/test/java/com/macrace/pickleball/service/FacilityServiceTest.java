package com.macrace.pickleball.service;

import com.macrace.pickleball.dto.request.FacilityRequest;
import com.macrace.pickleball.dto.response.FacilityResponse;
import com.macrace.pickleball.model.Facility;
import com.macrace.pickleball.model.User;
import com.macrace.pickleball.repository.FacilityRepository;
import com.macrace.pickleball.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class FacilityServiceTest {
    @Autowired
    private FacilityService facilityService;

    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        User user = User.builder()
                .phoneNumber("0972808477")
                .password("123456")
                .fullName("User Test")
                .build();
        userRepository.save(user);

        Facility facility = Facility.builder()
                .name("test facility")
                .user(user)
                .build();
        facilityRepository.save(facility);
    }

    @Test
    void facilityServiceInitializedCorrectly() {
        assertThat(facilityService).isNotNull();
    }

    @Test
    void testGetAllFacility() {
        List<FacilityResponse> facilities = facilityService.getAllFacility();

        assertThat(facilities.size()).isEqualTo(1);
    }

    @Test
    void testUpdateFacility() {
        FacilityRequest request = new FacilityRequest();
        request.setName("test 1");

        facilityService.updateFacility(1, request);
        Facility facility = facilityRepository.findById(1).get();

        assertThat(facility.getName()).isEqualTo(request.getName());
    }
}
