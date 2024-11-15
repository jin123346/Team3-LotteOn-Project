package com.lotteon.dto.order;


import com.lotteon.dto.product.cart.CartItemDTO;
import com.lotteon.entity.cart.CartItem;
import lombok.*;

import java.util.List;


@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartOrderRequestDTO {


    private long cartId;

    private List<CartItemRequest> cartItems;  // Renamed to "cartItems" for clarity

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CartItemRequest {
        private long cartItemId;   // Cart item ID
        private int quantity;      // Quantity of the item
    }




}





