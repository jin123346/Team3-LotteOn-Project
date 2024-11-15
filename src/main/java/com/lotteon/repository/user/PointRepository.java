package com.lotteon.repository.user;

import com.lotteon.entity.User.Member;
import com.lotteon.entity.User.Point;
import com.lotteon.entity.admin.CouponIssued;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {

    Page<Point> findByMemberId(Long memberId, Pageable pageable);
}
