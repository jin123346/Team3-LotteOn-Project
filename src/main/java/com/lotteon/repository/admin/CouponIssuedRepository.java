package com.lotteon.repository.admin;

import com.lotteon.entity.User.Member;
import com.lotteon.entity.admin.Coupon;
import com.lotteon.entity.admin.CouponIssued;
import com.lotteon.repository.custom.CouponRepositoryCustom;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponIssuedRepository extends JpaRepository<CouponIssued, String>{


    List<CouponIssued> findByMemberId(Long memberId);

    @Query("SELECT c FROM CouponIssued c WHERE c.member.user.uid = :uid AND (c.productId IS NULL OR c.productId = :productId)")
    List<CouponIssued> findValidCoupons(@Param("uid") String uid, @Param("productId") Long productId);

    Optional<CouponIssued> findByMemberAndCoupon(Member member, Coupon coupon);

    List<CouponIssued> findByCouponTypeContaining(String couponName);

    boolean existsByMemberIdAndCouponId(Long memberId, String couponId);

    List<CouponIssued> findBySellerCompanyContaining(String sellerCompany);

    Page<CouponIssued> findBySellerCompanyContaining(String sellerCompany, Pageable pageable);
    List<CouponIssued> findByCouponNameContaining(String sellerCompany);
    List<CouponIssued> deleteByCouponId(String couponId);
    List<CouponIssued> findByCouponId(String couponId);



}
