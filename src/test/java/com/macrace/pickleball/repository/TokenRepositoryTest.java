package com.macrace.pickleball.repository;

import com.macrace.pickleball.model.Token;
import com.macrace.pickleball.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TokenRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TokenRepository tokenRepository;

    @BeforeEach
    void init() {
        User user = User.builder()
                .phoneNumber("0972808477")
                .password("123456")
                .fullName("User Test")
                .build();
        entityManager.persist(user);
        entityManager.flush();

        Token token = Token.builder()
                .user(user)
                .accessToken("accessToken.test")
                .expired(false)
                .build();

        entityManager.persist(token);
        entityManager.flush();
    }

    @Test
    void tokenRepositoryInitializedCorrectly() {
        assertThat(tokenRepository).isNotNull();
    }

    @DisplayName("findAllTokenByUserId return list token")
    @Test
    void whenFindAllTokenByUserId_listToken() {
        List<Token> tokens = tokenRepository.findAllTokenByUserId(1);

        assertThat(tokens).isNotNull();
        assertThat(tokens.size()).isEqualTo(1);
    }

    @DisplayName("findAllTokenByUserId return token")
    @Test
    void whenFindByAccessToken_token() {
        Optional<Token> tokenOptional = tokenRepository.findByAccessToken("accessToken.test");

        assertThat(tokenOptional.get()).isNotNull();
        assertThat(tokenOptional.get().isExpired()).isEqualTo(false);
    }
}
