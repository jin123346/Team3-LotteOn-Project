package com.lotteon.controller.user;

import org.springframework.ui.Model;

import com.lotteon.entity.admin.CouponIssued;
import com.lotteon.security.MyUserDetails;
import com.lotteon.service.user.CouponDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class ApiMyPageController {

    private final CouponDetailsService couponDetailsService;



    // 쿠폰 개수를 반환하는 API
    @GetMapping("/coupons")
    public ResponseEntity<Integer> getCouponCount(Model model) {

        log.info("요청 들어옴 api");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        String memberId = (userDetails.getId());  // 로그인한 사용자의 Member ID (String 타입)

        log.info("멤버 아이디다"+memberId);

        // 해당 멤버의 발급된 쿠폰 목록 조회

            List<CouponIssued> issuedCoupons = couponDetailsService.memberCouponList(memberId); // 서비스에서 발급된 쿠폰 조회
            log.info("발급받은 쿠폰: {}", issuedCoupons);

            model.addAttribute("IssuedList", issuedCoupons);
        // 쿠폰 개수만 반환
        return ResponseEntity.ok(issuedCoupons.size());
    }
}

