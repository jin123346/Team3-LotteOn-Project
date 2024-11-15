package com.lotteon.service.user;

import com.lotteon.entity.User.Member;
import com.lotteon.entity.admin.CouponIssued;
import com.lotteon.entity.product.Product;
import com.lotteon.repository.admin.CouponIssuedRepository;
import com.lotteon.repository.product.ProductRepository;
import com.lotteon.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class CouponDetailsService {


    private final CouponIssuedRepository couponIssuedRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;


    public List<CouponIssued> memberCouponList(String uid) {
        log.info("쿠폰디테일 서비스 요청");

        Optional<Member> memberOtp = memberRepository.findByUid(uid);

        Member member = memberOtp.get();

        log.info("멤버 유아이디"+member.getUid());
        return couponIssuedRepository.findByMemberId(member.getId());
    }

    public List<CouponIssued> memberOrderCouponList(String uid, Long productId) {
        log.info("쿠폰디테일 서비스 요청");

        Optional<Member> memberOtp = memberRepository.findByUid(uid);
        log.info("멤버아이디" + uid);

        if (memberOtp.isEmpty()) {
            throw new RuntimeException("Member not found for uid: " + uid);
        }

        List<CouponIssued> couponIssuedList = new ArrayList<>();
        Member member = memberOtp.get();
        log.info("전달된 productId: " + productId);

        List<CouponIssued> validCoupons  = couponIssuedRepository.findValidCoupons(member.getUid(), productId);
        log.info("불러온 쿠폰 상품아이디들: ");
        validCoupons.stream()
                .map(coupon -> coupon.getProductId())  // 상품아이디 추출
                .forEach(productIdValue -> log.info("상품아이디: " + productIdValue));

        List<CouponIssued> couponIssuedList1 = couponIssuedRepository.findByMemberId(member.getId());
        log.info("멤버 발급 쿠폰:" + couponIssuedList1 );
// 상품 리스트 조회 (Product)
        Optional<Product> products = productRepository.findByProductId(productId);
        log.info("불러온 상품 리스트: " + products);
        return validCoupons;
    }

    public List<CouponIssued> couponIssuedList(String uid) {

        Optional<Member> memberOpt = memberRepository.findByUid(uid);

        if (memberOpt.isEmpty()) {
            throw new RuntimeException("Member not found for uid: " + uid);
        }
        Member member = memberOpt.get();

        return couponIssuedRepository.findByMemberId(member.getId());
    }



}
