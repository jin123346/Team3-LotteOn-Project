package com.lotteon.dto.product.request;


import com.lotteon.dto.product.OptionDTO;
import com.lotteon.dto.product.OptionItemDTO;
import com.lotteon.entity.User.Member;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyNowRequestDTO {

    private String productId;
    private String productName;
    private String discount;
    private String originalPrice;
    private String finalPrice;
    private int quantity;
    private String file190;
    private String optionId;
    private String optionName;
    private String point;
    private String shippingFee;
    private String ShippingTerms;
    private int expectedPoint;
    private Member member;
    private List<OptionItemDTO> options;  // Use List<OptionDTO> for options\

    private long additionalPrice;
    private long combinationId;
    private String combinationString;
    private long cartId;
    private long cartItemId;

}
