package com.lotteon.dto.product;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.lotteon.entity.product.Review;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.annotate.JsonIgnore;

import java.io.Serial;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Log4j2
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "productId")
@JsonIgnoreProperties({"reviews"})  // reviews나 순환참조가 생길 수 있는 필드를 명시
public class ProductSummaryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long categoryId;
    private Long productId;
    private String productName;
    private Long price;
    private int discount;
    private int shippingFee;
    private int shippingTerms; //무료배송 조건
    private String productDesc; //상품설명
    private String file230;
    private String file190;
    private Long sellerId;
    private String sellerName;
    private String company;

    @JsonIgnore // Redis 직렬화 시 reviews 필드를 무시합니다.
    private List<Review> reviews;
    private Double rating = 0.0; // rating이 null일 경우 기본값 설정
    private long discountPrice;
    private long finalPrice;
    private double productRating;
    private Integer reviewCount;
    private String savedPath;



    @Builder
    @QueryProjection
    public  ProductSummaryDTO(Long categoryId,Long productId, String productName, Long price, int discount, int shippingFee, int shippingTerms, String productDesc, String file230, String file190, Long sellerId, String sellerName, String company,String savedPath) {
        this.categoryId = categoryId;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.discount = discount;
        this.shippingFee = shippingFee;
        this.shippingTerms = shippingTerms;
        this.productDesc = productDesc;
        this.file230 = file230;
        this.file190 = file190;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.company = company;
        this.savedPath= savedPath;

        this.discountPrice = Math.round(price * discount / 100.0 / 100) * 100;
        this.finalPrice = price - this.discountPrice;

    }

    public void setRating(List<String> ratings) {
        this.rating = calculateAverageRating(ratings);
    }

    public double calculateAverageRating(List<String> ratings) {
        if (ratings == null || ratings.isEmpty()) {
            return 0.0;
        }

        double average = ratings.stream()
                .filter(Objects::nonNull)
                .mapToDouble(rating -> {
                    try {
                        return Double.parseDouble(rating);
                    } catch (NumberFormatException e) {
                        log.warn("Failed to parse rating: " + rating);
                        return 0.0;
                    }
                })
                .average()
                .orElse(0.0);

        // 소수점 첫째 자리까지 반올림
        return Double.parseDouble(String.format("%.1f", average));
    }



}
