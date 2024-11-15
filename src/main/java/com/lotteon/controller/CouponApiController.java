package com.lotteon.controller;

import com.lotteon.dto.admin.CouponDTO;
import com.lotteon.dto.admin.CouponIssuedDTO;
import com.lotteon.dto.admin.CouponIssuedRequestDTO;
import com.lotteon.entity.User.Member;
import com.lotteon.entity.admin.Coupon;
import com.lotteon.entity.admin.CouponIssued;
import com.lotteon.entity.product.Product;
import com.lotteon.repository.admin.CouponIssuedRepository;
import com.lotteon.repository.admin.CouponRepository;
import com.lotteon.repository.user.MemberRepository;
import com.lotteon.security.MyUserDetails;
import com.lotteon.service.admin.CouponIssuedService;
import com.lotteon.service.admin.CouponService;
import com.lotteon.service.product.ProductCategoryService;
import com.lotteon.service.user.CouponDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("/api/coupon")
public class CouponApiController {

    private final CouponIssuedService couponIssuedService;
    private final CouponDetailsService couponDetailsService;
    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;
    private final CouponService couponService;
    private final ModelMapper modelMapper;
    private final CouponIssuedRepository couponIssuedRepository;

    @GetMapping("/{productId}")
    public ResponseEntity<List<CouponDTO>> getCouponsForProduct(@PathVariable Long productId) {
        log.info("쿠폰 리스트 불러오는거 요청 왔따 : " + productId);
        if (productId == null) {
            return ResponseEntity.ok(Collections.emptyList()); // 빈 리스트 반환
        }

        log.info("상품 ID: {}", productId);

        // 상품 ID에 해당하는 쿠폰을 조회하는 로직
        List<Coupon> coupons = couponService.selectCouponIssued(productId);

        // 상품 ID에 해당하는 쿠폰이 없을 경우 빈 리스트 반환
        if (coupons == null || coupons.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList()); // 빈 리스트 반환
        }
        // Coupon 리스트를 CouponDTO 리스트로 변환
        List<CouponDTO> couponDTOs = coupons.stream()
                .map(coupon -> modelMapper.map(coupon, CouponDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(couponDTOs);
    }

    @GetMapping("/all/coupons")
    public ResponseEntity<List<CouponDTO>> getAllCoupons() {
        log.info("쿠폰 전체 리스트 불러오는거 요청 왔따");

        List<Coupon> coupons = couponService.selectCouponIssued(null);
        // Coupon 리스트를 CouponDTO 리스트로 변환
        List<CouponDTO> couponDTOs = coupons.stream()
                .map(coupon -> modelMapper.map(coupon, CouponDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(couponDTOs);
    }
    // 쿠폰 발급 여부 확인 API
    @GetMapping("/check/{couponId}")
    public ResponseEntity<Boolean> checkCouponIssued(@PathVariable String couponId, Authentication authentication) {
       log.info("쿠폰 발급 여부 확인");
        try {
            // 현재 로그인된 사용자의 정보
            String memberUid = authentication.getName(); //

            List<CouponIssued> issuedCoupons = couponDetailsService.couponIssuedList(memberUid);

            // 발급된 쿠폰 리스트에서 couponId가 있는지 확인
            boolean isIssued = issuedCoupons.stream()
                    .anyMatch(coupon -> coupon.getCouponId().equals(couponId));

            log.info("발급현황"+ isIssued);
            return ResponseEntity.ok(isIssued);


        } catch (Exception e) {
            log.error("예외 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @PostMapping("/apply/{couponId}")
    public ResponseEntity<CouponDTO> applyCoupon(@PathVariable("couponId") String couponId, Principal principal) {
        log.info("쿠폰 발금 버튼 클릭되서 요청왔다");

        String userUid = principal.getName();

        Member member = memberRepository.findByUser_Uid(userUid)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        log.info("사용자 가 누군가"+ member);

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new RuntimeException("쿠폰을 찾을 수 없습니다."));
        log.info("어떤 쿠폰인가 "+coupon);
        Product product = coupon.getProduct();

        couponIssuedService.insertCouponIssued(member, coupon, product);

        couponIssuedService.useCoupon(couponId);


        CouponDTO couponDTO = new CouponDTO();
        couponDTO.setCouponId(coupon.getCouponId());
        couponDTO.setCouponName(coupon.getCouponName());
        couponDTO.setCouponType(coupon.getCouponType());

        return ResponseEntity.ok(couponDTO);
    }
    @GetMapping("/getCouponsForMember")
    public ResponseEntity<Map<String, Object>> getCouponsForMember(@RequestParam Long productId) {
        log.info("쿠폰 리스트 요청 - 상품 ID: {}", productId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 현재 로그인된 사용자의 정보
        String memberUid = authentication.getName(); //
        log.info("사용자 UID: {}", memberUid);

        // 쿠폰 조회 서비스 호출 (상품 ID에 해당하는 쿠폰 조회)
        List<CouponIssued> couponIssuedList = couponDetailsService.memberOrderCouponList(memberUid, productId);

        // 쿠폰이 없을 경우 빈 응답 반환
        if (couponIssuedList.isEmpty()) {
            log.info("해당 상품에 대한 쿠폰이 없음");
            return ResponseEntity.ok(Map.of("result", "failure", "message", "해당 상품에 대한 쿠폰이 없습니다."));
        }

        // Coupon 엔티티를 CouponDTO로 변환
        List<CouponIssuedDTO> couponIssuedDTOList = couponIssuedList.stream()
                .map(CouponIssued::toDTO)
                .collect(Collectors.toList());

        // 결과를 Map 형태로 응답
        Map<String, Object> response = new HashMap<>();
        response.put("result", "success");
        response.put("coupons", couponIssuedDTOList);
        log.info("발급 반환 데이터 3" + couponIssuedDTOList);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getCouponsForCart")
    public ResponseEntity<Map<String, Object>> getCouponsForCart(@RequestParam Long productId) {
        log.info("쿠폰 리스트 요청 - 상품 ID: {}", productId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 현재 로그인된 사용자의 정보
        String memberUid = authentication.getName();
        log.info("사용자 UID: {}", memberUid);

        // 쿠폰 조회 서비스 호출 (상품 ID에 해당하는 쿠폰 조회)
        List<CouponIssued> couponIssuedList = couponDetailsService.memberOrderCouponList(memberUid, productId);

        // 쿠폰이 없을 경우 빈 응답 반환
        if (couponIssuedList.isEmpty()) {
            log.info("해당 상품에 대한 쿠폰이 없음");
            return ResponseEntity.ok(Map.of("result", "failure", "message", "해당 상품에 대한 쿠폰이 없습니다."));
        }
        log.info("발급 반환 데이터 1" + couponIssuedList);

        // CouponIssued 목록을 DTO로 변환
        List<CouponIssuedDTO> couponIssuedDTOList = couponIssuedList.stream()
                .map(CouponIssued::toDTO)
                .collect(Collectors.toList());
        log.info("발급 반환 데이터 2" + couponIssuedDTOList);

        // 결과를 Map 형태로 응답
        Map<String, Object> response = new HashMap<>();
        response.put("result", "success");
        response.put("coupons", couponIssuedDTOList);
        log.info("발급 반환 데이터 3" + couponIssuedDTOList);
        return ResponseEntity.ok(response);
    }


}
