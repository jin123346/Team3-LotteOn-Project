
package com.lotteon.dto.product.cart;


import com.lotteon.dto.product.*;
import com.lotteon.entity.User.Member;
import lombok.*;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDTO {
    private long cartItemId;    // CartItem ID

    private String productId;
    private String productName;
    private int discount;       // 할인율 (선택 사항)
    private long originalPrice;
    private long finalPrice;
    private int quantity;       // 수량 (추가)
    private String file190;
    private long optionId;
    private long optionGroupId;

    private String optionName;
    private String point;
    private long shippingFee;
    private int deliveryFee;
    private long additionalPrice;
    private long calcOriginalPrice;

    private long ShippingTerms;
    private int expectedPoint;
    private Member member;
    private List<OptionItemDTO> options;  // Use List<OptionDTO> for options
    private int stock;          // 재고
    private long price;         // 가격
    private List<OptionGroupDTO> optionGroupDTOS;      // 옵션 (예: 색상, 사이즈 등)
    private ProductDTO productDTO; // 상품 정보
    private ProductOptionCombinationDTO combinationDTO;
    private long total;         // 총 가격
    private String imageUrl;
    private long calcShippingCost;
    private long cartId;

    private int points;         // 적립 포인트 (선택 사항)







}


