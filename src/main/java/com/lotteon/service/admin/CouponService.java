package com.lotteon.service.admin;

import com.lotteon.dto.admin.CouponDTO;
import com.lotteon.dto.admin.CouponListRequestDTO;
import com.lotteon.dto.admin.CouponListResponseDTO;
import com.lotteon.dto.product.ProductDTO;
import com.lotteon.entity.User.Seller;
import com.lotteon.entity.admin.Coupon;
import com.lotteon.entity.admin.CouponIssued;
import com.lotteon.entity.product.Product;
import com.lotteon.repository.admin.CouponIssuedRepository;
import com.lotteon.repository.admin.CouponRepository;
import com.lotteon.repository.product.ProductRepository;
import com.lotteon.repository.user.SellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.scheduler.Scheduler;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class CouponService {

    private final CouponRepository couponRepository;
    private final SellerRepository sellerRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final CouponIssuedRepository couponIssuedRepository;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public String randomCouponId() {

        String couponId;
        do {
            couponId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
        } while (couponRepository.existsById(couponId));

        return couponId;
    }

    public List<String> getUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }

    public void insertCoupon(CouponDTO couponDTO) {
        // 현재 인증된 사용자의 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserUid = authentication.getName(); // 로그인한 사용자의 username을 가져옴 (Seller의 식별자로 사용)

        log.info("Logged in seller username: " + loggedInUserUid);

        // 로그인한 셀러 정보를 조회
        Seller seller = sellerRepository.findByUserUid(loggedInUserUid)
                .orElseThrow(() -> new RuntimeException("Seller not found for username: " + loggedInUserUid));

        Product product = null;
        if ("discount".equals(couponDTO.getCouponType()) && couponDTO.getProductId() != null) {
            product = productRepository.findById(couponDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found for ID: " + couponDTO.getProductId()));
        }
        String couponName = couponDTO.getCouponName();
            // 상품이 null일 경우, 쿠폰 이름에 "전체상품 적용"을 추가
        if (product == null) {
           couponName = "[전체상품 적용]" + couponName;
        }
        Coupon coupon = Coupon.builder()
                .couponId(randomCouponId())
                .couponName(couponName)
                .couponType(couponDTO.getCouponType())
                .benefit(couponDTO.getBenefit())
                .startDate(couponDTO.getStartDate())
                .restrictions(couponDTO.getRestrictions() + "원 이상적용가능")
                .endDate(couponDTO.getEndDate())
                .notes(couponDTO.getNotes())
                .issuedCount(0) // 발급수 초기화
                .usedCount(0) // 사용수 초기화
                .status("발급 중") // 기본 상태 설정 (필요에 따라 수정)
                .rdate(LocalDate.now()) // 현재 날짜로 발급일 설정
                .sellerCompany(seller.getCompany())
                .product(product) // 조회 및 필터링 을 위한 관계
                .seller(seller)
                .build();

        log.info("Coupon 상품 등록함------------------------" + coupon);

        couponRepository.save(coupon);
    }


    public CouponDTO endCoupon(String couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new RuntimeException("Coupon could not be found for id: " + couponId));

        if ("발급 중".equals(coupon.getStatus()) || "사용완료".equals(coupon.getStatus())) {
            coupon.setStatus("종료됨");
            couponRepository.save(coupon);
            log.info("Coupon ended: " + coupon);

            List<CouponIssued> issuedCoupons = couponIssuedRepository.findByCouponId(couponId);
            if (!issuedCoupons.isEmpty()) {
                for (CouponIssued issuedCoupon : issuedCoupons) {
                    issuedCoupon.setStatus("종료됨");  // 각 발급된 쿠폰의 상태를 '종료됨'으로 변경
                    issuedCoupon.setUsageStatus("사용불가"); // 종료 버튼을 누르면 issued 쪽에서 사용 불가 로 변환
                }
                couponIssuedRepository.saveAll(issuedCoupons);  // 발급된 쿠폰 모두 저장
                log.info("삭제요청");
                // 발급된 쿠폰은 30일 후 자동 삭제
                scheduler.schedule(() -> deleteCouponById(couponId), 30, TimeUnit.DAYS);
            } else {
                // 일반 쿠폰은 14일 후 자동 삭제
                scheduler.schedule(() -> deleteCouponById(couponId), 14, TimeUnit.DAYS);
            }
            couponIssuedRepository.saveAll(issuedCoupons);
            return modelMapper.map(coupon, CouponDTO.class);
        } else {
            throw new RuntimeException("궁시렁궁시렁 오류남");
        }
    }
    private void deleteCouponById(String couponId) {
        couponRepository.deleteById(couponId);
        couponIssuedRepository.deleteByCouponId(couponId);  // 발급된 쿠폰들도 삭제
        log.info("Coupon and issued coupons deleted after delay: " + couponId);
    }
    // 페이징 기능 추가
    public Page<CouponDTO> selectCouponsPagination(CouponListRequestDTO request, long sellerId, Pageable pageable) {

        List<String> roles = getUserRoles();
        // 쿠폰 페이지 조회
        Page<Coupon> couponPage = null;

        if (roles.contains("ROLE_ADMIN")) {
            couponPage = couponRepository.findAll(pageable);
//            log.info("관리자 쿠폰: " + couponPage);
        } else if (roles.contains("ROLE_SELLER")) {
            // 일반 셀러는 자신의 쿠폰만 조회
            couponPage = couponRepository.findBySellerId(sellerId, pageable);
            log.info("일반인 쿠폰: " + couponPage);
        }

        int total = (int) Objects.requireNonNull(couponPage).getTotalElements();
        int totalPages = (int) Math.ceil((double) total / request.getSize());
        log.info("총 쿠폰 수: {}, 총 페이지 수: {}", total, totalPages);

        return couponPage.map(coupon -> modelMapper.map(coupon, CouponDTO.class));
    }

    public List<Coupon> selectCouponIssued(Long productId) {
        if (productId == null) {
            // productId가 null인 경우, 모든 상품에 적용 가능한 쿠폰 조회
            log.info("서비스에서 null 값 조회 요청");
            return couponRepository.findByProductIsNull();

        } else {
            log.info("서비스에서 아이디가 있는 쿠폰 조회 요청");
            // productId가 주어진 경우, 해당 상품 쿠폰과 모든 상품에 적용 가능한 쿠폰 조회
            List<Coupon> productCoupons = couponRepository.findByProduct_productId(productId);
            List<Coupon> allApplicableCoupons = couponRepository.findByProductIsNull();

            // 두 리스트를 합칩니다.
            productCoupons.addAll(allApplicableCoupons);
            return productCoupons;
        }
    }


    public List<ProductDTO> getProductsBySellerId(String sellerId) {
        // 예시: SecurityContextHolder를 통해 로그인한 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserId = authentication.getName(); // 로그인한 사용자 ID

        List<Product> products = productRepository.findAllBySellerId(loggedInUserId );

        return products.stream()
                .filter(product -> !product.isDeleted()) // isDeleted가 false인 항목만 필터링
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }

    }
