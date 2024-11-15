package com.lotteon.entity.admin;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lotteon.dto.admin.CouponIssuedDTO;
import com.lotteon.entity.User.Member;
import com.lotteon.entity.User.User;
import com.lotteon.entity.product.Product;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "couponIssued")
public class CouponIssued {

    @Id
    private String issuanceNumber;

    private String couponId;
    private String couponType;
    private String couponName;
    private String memberName;
    private String productName;
    private Long productId;
    private String restrictions;
    private String benefit; // 혜택
    private String sellerCompany;
    private LocalDate startDate; // 시작 날짜
    private LocalDate endDate; // 종료 날짜


    @JsonFormat(pattern = "MM월 dd일") // 원하는 형식으로 지정
    private LocalDateTime usedDate;



    @Builder.Default
    private String usageStatus = "미사용"; // 기존 UsageStatus -> usageStatus

    private LocalDateTime usageDate; // 기존 UsageDate -> usageDate


    @Builder.Default
    private String status = "사용가능";


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "coupon_couponId")
    @JsonIgnore
    @JsonBackReference
    private Coupon coupon;

    public void setUsedDateIfApplicable() {
        // 사용 시점에 현재 시간으로 설정
        if (this.usedDate == null) {
            this.usedDate = LocalDateTime.now();  // 유저가 쿠폰을 처음 사용할 때
        }
    }

    // 사용일을 "MM월dd일" 형태로 반환하는 메서드
    public String getFormattedUsedDate() {
        // usedDate가 null이면 "00월00일" 표시
        if (this.usedDate == null) {
            return "00월00일";
        }

        // usedDate가 있으면 포맷된 날짜를 반환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM월dd일");
        return this.usedDate.format(formatter);
    }
    public CouponIssuedDTO toDTO() {
        return CouponIssuedDTO.builder()
                .issuanceNumber(this.issuanceNumber)
                .couponId(this.couponId)
                .couponType(this.couponType)
                .couponName(this.couponName)
                .memberName(this.memberName)
                .productName(this.productName)
                .productId(this.productId)
                .restrictions(this.restrictions)
                .benefit(this.benefit)
                .sellerCompany(this.sellerCompany)
                .usageStatus(this.usageStatus)
                .usageDate(this.usageDate)
                .status(this.status)
                .build();
    }


}
