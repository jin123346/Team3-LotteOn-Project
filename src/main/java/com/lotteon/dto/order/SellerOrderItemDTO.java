package com.lotteon.dto.order;

import com.lotteon.entity.order.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class SellerOrderItemDTO {

    private String sellerUid;
    private String company;
    private List<OrderItem> orderItems;


}
