package com.macrace.pickleball.repository;

import com.macrace.pickleball.model.Yard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface YardRepository extends JpaRepository<Yard, Integer> {
    Optional<Yard> findByName(String yardName);
}
