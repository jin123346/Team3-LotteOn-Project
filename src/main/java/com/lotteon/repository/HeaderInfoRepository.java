package com.lotteon.repository;

import com.lotteon.entity.HeaderInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeaderInfoRepository extends JpaRepository<HeaderInfo, Long> {
}
