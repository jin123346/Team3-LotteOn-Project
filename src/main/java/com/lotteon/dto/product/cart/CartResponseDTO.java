package com.lotteon.dto.product.cart;


import com.lotteon.dto.product.OptionDTO;
import com.lotteon.dto.product.OptionItemDTO;
import com.lotteon.dto.product.ProductDTO;
import com.lotteon.entity.User.Member;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponseDTO {

    private String productId;
    private String productName;
    private int discount;       // 할인율 (선택 사항)
    private String originalPrice;
    private String finalPrice;
    private int quantity;       // 수량 (추가)
    private String file190;
    private String optionId;
    private String optionName;
    private String point;
    private String shippingFee;
    private String ShippingTerms;
    private int expectedPoint;
    private Member member;
    private List<OptionItemDTO> options;  // Use List<OptionDTO> for options
    private int stock;          // 재고
    private Long cartItemId;    // CartItem ID
    private long price;         // 가격
    private List<OptionDTO> option;      // 옵션 (예: 색상, 사이즈 등)
    private ProductDTO productDTO; // 상품 정보

    private long total;         // 총 가격

    private int points;         // 적립 포인트 (선택 사항)
}
