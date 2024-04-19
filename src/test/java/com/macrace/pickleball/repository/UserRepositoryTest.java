package com.macrace.pickleball.repository;

import com.macrace.pickleball.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
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
    }

    @Test
    void userRepositoryInitializedCorrectly() {
        assertThat(userRepository).isNotNull();
    }

    @DisplayName("findByPhoneNumber return user")
    @Test
    void whenFindByPhoneNumber_thenReturnUser() {
        User user = userRepository.findByPhoneNumber("0972808477").get();
        assertThat(user.getPhoneNumber()).isEqualTo("0972808477");
        assertThat(user.getPassword()).isEqualTo("123456");
        assertThat(user.getFullName()).isEqualTo("User Test");
    }
}
