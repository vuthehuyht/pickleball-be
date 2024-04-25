package com.macrace.pickleball.repository;

import com.macrace.pickleball.constant.YardState;
import com.macrace.pickleball.model.Facility;
import com.macrace.pickleball.model.User;
import com.macrace.pickleball.model.Yard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class YardRepositoryTest {
    @Autowired
    private YardRepository yardRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        User user = User.builder()
                .phoneNumber("0972808477")
                .password("123456")
                .fullName("User Test")
                .build();
        entityManager.persist(user);
        entityManager.flush();

        Facility facility = Facility.builder()
                .name("facility test")
                .address("facility address")
                .phoneNumber("012345789")
                .user(user)
                .build();
        entityManager.persist(facility);
        entityManager.flush();

        Yard yard = Yard.builder()
                .name("yard test")
                .state(YardState.READY.toString())
                .facility(facility)
                .build();
        entityManager.persist(yard);;
        entityManager.flush();
    }

    @Test
    void yardRepositoryInitializedCorrectly() {
        assertThat(yardRepository).isNotNull();
    }

    @Test
    void testFindByName() {
        Yard yard = yardRepository.findByName("yard test").get();

        assertThat(yard.getName()).isEqualTo("yard test");
        assertThat(yard.getState()).isEqualTo(YardState.READY.toString());
    }
}
