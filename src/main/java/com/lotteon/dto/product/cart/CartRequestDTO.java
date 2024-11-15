package com.lotteon.dto.product.cart;


import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartRequestDTO {

    private long productId;
    private long cartItemId;

    private String productName;
    private int originalPrice;
    private long finalPrice;
    private int quantity;
    private String file190;
    private long optionId;
    private String optionName;
    private int point;
    private int shippingFee;
    private long discount;


}
