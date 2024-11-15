package com.lotteon.entity.admin;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lotteon.entity.User.Seller;
import com.lotteon.entity.product.Product;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.w3c.dom.Text;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "coupon")
public class Coupon {

    @Id
    private String couponId; // 쿠폰번호

    private String couponName; // 쿠폰명
    private String couponType; // 쿠폰종류
    private String benefit; // 혜택
    private LocalDate startDate; // 시작 날짜
    private LocalDate endDate; // 종료 날짜
    private String notes; // 유의사항

    private String sellerCompany;
    private int issuedCount; // 발급수
    private int usedCount; // 사용수
    private String status; // 상태
    private LocalDate rdate; // 발급일
    private String restrictions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    @JsonIgnore
    private Seller seller; // 발급자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CouponIssued> couponIssued;

    // 새로운 메서드 추가
// 모든 발급된 쿠폰 조회
    public List<CouponIssued> getCouponIssuedList() {
        return this.couponIssued;
    }


}
