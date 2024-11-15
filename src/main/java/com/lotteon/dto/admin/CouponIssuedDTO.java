package com.lotteon.dto.admin;

import com.lotteon.entity.admin.CouponIssued;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CouponIssuedDTO {

    private String issuanceNumber;
    private String couponId;
    private String couponType;
    private String couponName;
    private String memberName; // 등록한 쿠폰을 발급한 맴버 이름
    private String productName;
    private String usageStatus;
    private LocalDateTime usageDate;
    private String status;
    private Long productId; // 등록된 상품 아이디
    private String restrictions;
    private String benefit;
    private List<CouponDTO> couponDTO;
    private String sellerCompany;
    private String startDate; // 시작 날짜
    private String endDate; // 종료 날짜




    public CouponIssuedDTO(CouponIssued couponIssued) {
        this.issuanceNumber = couponIssued.getIssuanceNumber();
        this.couponId = couponIssued.getCouponId();
        this.couponType = couponIssued.getCouponType();
        this.couponName = couponIssued.getCouponName();
        this.memberName = couponIssued.getMemberName();
        this.productName = couponIssued.getProductName();
        this.usageStatus = couponIssued.getUsageStatus();
        this.usageDate = couponIssued.getUsageDate();
        this.status = couponIssued.getStatus();
        this.productId = couponIssued.getProductId();
        this.restrictions = couponIssued.getRestrictions();
        this.benefit = couponIssued.getBenefit();
        this.sellerCompany = couponIssued.getSellerCompany();
    }
}
