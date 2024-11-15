package com.lotteon.dto.product.cart;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartSummary {

    private int finalTotalQuantity;
    private long finalTotalPrice;
    private long finalTotalDiscount;
    private long finalTotalDeliveryFee;
    private long finalTotalOrderPrice;
    private long finalTotalPoints;

}
