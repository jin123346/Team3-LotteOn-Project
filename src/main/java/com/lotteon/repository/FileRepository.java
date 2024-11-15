package com.lotteon.repository;

import com.lotteon.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<Banner, Integer> {
}
