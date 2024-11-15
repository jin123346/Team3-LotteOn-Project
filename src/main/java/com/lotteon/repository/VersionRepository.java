package com.lotteon.repository;

import com.lotteon.entity.Version;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VersionRepository extends JpaRepository<Version, Integer> {

    Optional<Version> findTopByOrderByVerIdDesc();
    Optional<Version>findByVerId(int id);
}
