package com.lotteon.service.admin;

import com.lotteon.dto.User.SellerDTO;
import com.lotteon.dto.admin.CouponIssuedDTO;
import com.lotteon.dto.admin.CouponListRequestDTO;
import com.lotteon.entity.User.Member;
import com.lotteon.entity.User.Seller;
import com.lotteon.entity.admin.Coupon;
import com.lotteon.entity.admin.CouponIssued;
import com.lotteon.entity.product.Product;
import com.lotteon.repository.admin.CouponIssuedRepository;
import com.lotteon.repository.admin.CouponRepository;
import com.lotteon.repository.product.ProductRepository;
import com.lotteon.repository.user.MemberRepository;
import com.lotteon.security.MyUserDetails;
import com.lotteon.service.user.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class CouponIssuedService {

    private final MemberRepository memberRepository;
    private final CouponIssuedRepository couponIssuedRepository;
    private final CouponRepository couponRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final SellerService sellerService;

    public String rendomIssuedId(){
        String issuedId;
        do{
            issuedId = UUID.randomUUID().toString().replaceAll("-","").substring(0,10);
        }while (couponRepository.existsById(issuedId));
        return issuedId;
    }
    public Long getLoggedInSellerCompany(Authentication authentication) {
        String loginUid = authentication.getName();
        SellerDTO seller = sellerService.getSeller(loginUid);

        // 셀러가 null이 아닌지 확인하고 셀러의 회사명이나 다른 정보 반환
        if (seller != null) {
            // 셀러의 id를 반환 (셀이 정보를 담고 있음)
            return seller.getId();  // 여기서 getId()를 호출하여 셀러 ID 반환
        } else {
            // 셀러 정보가 없을 경우 적절한 처리
            throw new IllegalStateException("Seller not found for user: " + loginUid);
        }
    }
    public List<String> getUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }
    public boolean isCouponIssuedToMember(Long memberId, String couponId) {
        // 쿠폰 발급 여부 확인 (회원이 해당 쿠폰을 발급받았는지)
        return couponIssuedRepository.existsByMemberIdAndCouponId(memberId, couponId);
    }

    public void useCoupon(String issuanceNumber){

        Optional<CouponIssued> couponIssuedOpt = couponIssuedRepository.findById(issuanceNumber);

        if(couponIssuedOpt.isPresent()){
            CouponIssued couponIssued = couponIssuedOpt.get();
            // 이미 사용된 쿠폰인지 체크 (중복 사용 방지)
            if ("사용됨".equals(couponIssued.getUsageStatus())) {
                log.warn("이미 사용된 쿠폰입니다. 쿠폰번호: {}", issuanceNumber);
                throw new RuntimeException("이 쿠폰은 이미 사용되었습니다.");
            }

            // 쿠폰 사용 상태로 업데이트
            couponIssued.setUsageStatus("사용됨"); // "사용완료" 로 상태 변경
            couponIssued.setStatus("사용불가"); // 상태를 "사용불가"로 설정

            couponIssued.setUsedDateIfApplicable(); // 현재 시간으로 설정
            couponIssuedRepository.save(couponIssued);

            Coupon coupon = couponIssued.getCoupon();
            coupon.setUsedCount((coupon.getUsedCount() + 1));
            couponRepository.save(coupon);

            log.info("쿠폰 사용 처리 완료, 쿠폰번호 :{}", issuanceNumber);
        } else {
            log.warn("사용할 수 없는 쿠폰입니다. 쿠폰번호: {}", issuanceNumber);

        }

}

    public void insertCouponIssued(Member member, Coupon coupon, Product product) {

        Optional<CouponIssued> existingCouponIssued = couponIssuedRepository.findByMemberAndCoupon(member, coupon);

        if (existingCouponIssued.isPresent()) {
            log.info("이미 발급된 쿠폰입니다. 발급을 중단합니다.");
            throw new IllegalStateException("이미 발급된 쿠폰입니다.");  // 예외 처리로 발급 중단
        }
        log.info("쿠폰 서비스까진 들어왓따");
        String couponName = coupon.getCouponName();
        if (product == null || coupon.getProduct() == null) {

            log.info("모든 상품에 대한 쿠폰이므로, ID가 없습니다.");
            CouponIssued couponIssued = CouponIssued.builder()
                    .issuanceNumber(rendomIssuedId())
                    .couponId(coupon.getCouponId())
                    .couponName(couponName)
                    .startDate(coupon.getStartDate())
                    .endDate(coupon.getEndDate())
                    .benefit(coupon.getBenefit())
                    .restrictions(coupon.getRestrictions())
                    .couponType(coupon.getCouponType())
                    .memberName(member.getName())
                    .sellerCompany(coupon.getSellerCompany())
                    .member(member)
                    .productId(null) // "전체상품"을 나타내는 값
                    .build();
            log.info("널인 쿠폰 저장되는 값들" + couponIssued);
            couponIssuedRepository.save(couponIssued); // 엔티티 저장
            coupon.setIssuedCount(coupon.getIssuedCount() + 1);
            couponRepository.save(coupon);
        } else{
                // 특정 상품에 대한 쿠폰 처리
                CouponIssued couponIssued = CouponIssued.builder()
                    .issuanceNumber(rendomIssuedId())
                    .couponId(coupon.getCouponId())
                    .couponName(coupon.getCouponName())
                    .startDate(coupon.getStartDate())
                    .endDate(coupon.getEndDate())
                    .benefit(coupon.getBenefit())
                    .restrictions(coupon.getRestrictions())
                    .couponType(coupon.getCouponType())
                    .memberName(member.getName())
                    .sellerCompany(coupon.getSellerCompany())
                    .productName(product.getProductName())
                    .member(member)
                    .productId(coupon.getProduct().getProductId())
                    .build();

            log.info("아이디가 있는 쿠폰 저장되는 값들: " + couponIssued);
            couponIssuedRepository.save(couponIssued);

            coupon.setIssuedCount(coupon.getIssuedCount() + 1);
            couponRepository.save(coupon);
        }

    }

    public CouponIssuedDTO endCouponIssued(String issuanceNumber){
        CouponIssued couponIssued = couponIssuedRepository.findById(issuanceNumber)
                .orElseThrow(() -> new RuntimeException("Coupon could not be found for id: " + issuanceNumber));

        if ("사용가능".equals(couponIssued.getStatus())) {
            couponIssued.setStatus("종료됨");
            couponIssuedRepository.save(couponIssued);
            log.info("Coupon ended: " + issuanceNumber);
            return modelMapper.map(couponIssued, CouponIssuedDTO.class); // couponIssued 객체를 DTO로 변환하여 반환
        } else {
            throw new RuntimeException("궁시렁궁시렁 오류남");
        }
    }

    // 페이징 기능 추가
    public Page<CouponIssued> selectIssuedCouponsPagination(CouponListRequestDTO request, Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginUid = authentication.getName();
        SellerDTO seller = sellerService.getSeller(loginUid);

        Long sellerId = seller.getId();
        String sellerCompany = seller.getCompany();  // 셀러 회사 이름을 가져옵니다

        List<String> roles = getUserRoles();
        // 쿠폰 페이지 조회
        Page<CouponIssued> couponPage = null;

        if (roles.contains("ROLE_ADMIN")) {
            couponPage = couponIssuedRepository.findAll(pageable);
//            log.info("관리자 쿠폰: " + couponPage);
        } else if (roles.contains("ROLE_SELLER")) {
            // 일반 셀러는 자신의 쿠폰만 조회
            couponPage  = couponIssuedRepository.findBySellerCompanyContaining(sellerCompany, pageable);
            log.info("셀러 발급 쿠폰: " + couponPage);
            log.info("셀러 아이디 : " + sellerId );
            log.info("셀러 등급 : " + roles );
        }

        int total = (int) Objects.requireNonNull(couponPage).getTotalElements();
        int totalPages = (int) Math.ceil((double) total / request.getSize());
        log.info("총 쿠폰 수: {}, 총 페이지 수: {}", total, totalPages);

        return couponPage.map(couponIssued -> modelMapper.map(couponIssued, CouponIssued.class));
    }

    public boolean updateCouponStatus(String issuanceNumber, String usageStatus, String status) {
        log.info("쿠폰 변경 요청 들어옴");
        // 쿠폰을 찾기
        Optional<CouponIssued> couponOptional = couponIssuedRepository.findById(issuanceNumber);

        if (couponOptional.isPresent()) {
            CouponIssued coupon = couponOptional.get();

            // 사용 상태와 전체 상태 업데이트
            coupon.setUsageStatus(usageStatus);
            coupon.setStatus(status);

            // 상태 변경 후 저장
            couponIssuedRepository.save(coupon);
            log.info("쿠폰 상태 업데이트 성공 - 발급번호: {},  사용 상태: {}, 전체 상태: {}", issuanceNumber, usageStatus, status);

            return true;
        } else {
            log.warn("쿠폰을 찾을 수 없거나", issuanceNumber);
            return false;
        }
    }



}
