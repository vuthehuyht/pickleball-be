package com.macrace.pickleball.repository;

import com.macrace.pickleball.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findByAccessToken(String accessToken);

    @Query(value = "select * from token t where t.expired = false and t.user_id = :userId", nativeQuery = true)
    List<Token> findAllTokenByUserId(Integer userId);
}
