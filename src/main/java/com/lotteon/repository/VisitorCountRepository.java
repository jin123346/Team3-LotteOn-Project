package com.lotteon.repository;

import com.lotteon.entity.VisitorCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitorCountRepository extends JpaRepository<VisitorCount, Long> {
}
