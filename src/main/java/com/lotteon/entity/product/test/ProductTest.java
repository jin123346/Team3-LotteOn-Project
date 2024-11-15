package com.lotteon.entity.product.test;


import com.lotteon.dto.User.SellerDTO;
import com.lotteon.dto.product.OptionDTO;
import com.lotteon.dto.product.ProductFileDTO;
import com.lotteon.dto.product.ReviewDTO;
import com.lotteon.entity.product.OptionGroup;
import com.lotteon.entity.product.ProductDetails;
import com.lotteon.entity.product.Review;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProductTest {

    private long productId;

    private long categoryId;
    private long sellerNo;
    private String productName;
    private long price;
    private long stock;
    private int discount;
    private int shippingFee;
    private int shippingTerms; //무료배송 조건
    private String rdate;
    private String ProductDesc; //상품설명
    private int point;
    private Boolean isCoupon; // 쿠폰 사용가능 유므
    private Boolean isSaled; // 판매가능여부
    private String sellerId;
    private String productCode;
    private long hit;

    private List<OptionGroup> optionGroups;

    private String file190;
    private String file230;
    private String file456;
    private List<String> filedesc = new ArrayList<>();;

    private List<ProductFileDTO> productFiles;
    private List<OptionDTO> options;
    private ProductDetails productDetails;
    private SellerDTO seller;

    //main list 판매자회사이름
    private String company;
    private double productRating;

    //리뷰 추가
    private List<Review> reviews;
    private List<ReviewDTO> reviewDTOs;



    public void addFileDescriptions(List<String> files) {
        if (files != null && !files.isEmpty()) {
            this.filedesc.addAll(files);
        }
    }


}
