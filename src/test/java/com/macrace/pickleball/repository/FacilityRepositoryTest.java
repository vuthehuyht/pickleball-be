package com.macrace.pickleball.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FacilityRepositoryTest {
    @Autowired
    private FacilityRepository facilityRepository;

    @Test
    void facilityRepositoryInitializedCorrectly() {
        assertThat(facilityRepository).isNotNull();
    }
}
