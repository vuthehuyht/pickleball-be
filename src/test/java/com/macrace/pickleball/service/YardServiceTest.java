package com.macrace.pickleball.service;

import com.macrace.pickleball.constant.YardState;
import com.macrace.pickleball.dto.request.YardRequest;
import com.macrace.pickleball.dto.response.MessageResponseTemplate;
import com.macrace.pickleball.model.Facility;
import com.macrace.pickleball.model.User;
import com.macrace.pickleball.model.Yard;
import com.macrace.pickleball.repository.FacilityRepository;
import com.macrace.pickleball.repository.UserRepository;
import com.macrace.pickleball.repository.YardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class YardServiceTest {
    @Autowired
    private YardService yardService;

    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private YardRepository yardRepository;

    @Test
    void yardServiceInitializedCorrectly() {
        assertThat(yardService).isNotNull();
    }

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
                .address("test")
                .phoneNumber("0972808490")
                .user(user)
                .build();
        facilityRepository.save(facility);

        Yard yard = Yard.builder()
                .name("yard test")
                .state(YardState.READY.toString())
                .facility(facility)
                .build();
        yardRepository.save(yard);
    }

    @Test
    void testAddNewYard() {
        YardRequest request = new YardRequest();
        request.setName("yard test 2");
        request.setFacilityId(1);

        MessageResponseTemplate response = yardService.addNewYard(request);
        assertThat(response.message()).isEqualTo("New yard was added successfully");
    }

    @Test
    void testGetAllYards() {
        YardRequest request = new YardRequest();
        request.setName("yard test 2");
        request.setFacilityId(1);
        yardService.addNewYard(request);

        assertThat(yardService.getAllYard().size()).isEqualTo(2);
    }

    @Test
    void testUpdateYard() {
        YardRequest request = new YardRequest();
        request.setName("yard test 3");

        MessageResponseTemplate response = yardService.updateYard(1, request);
        assertThat(response.message()).isEqualTo("Updating yard done");
    }
}
