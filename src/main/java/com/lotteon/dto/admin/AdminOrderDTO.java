package com.lotteon.dto.admin;

import com.lotteon.dto.order.OrderItemDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
public class AdminOrderDTO {

    private long orderId;
    private String uid;
    private String memberName;
    private long totalQuantity;
    private long totalOriginalPrice;
    private long totalDiscount;
    private long totalShipping;
    private long totalPrice;
    private LocalDateTime orderDate;
    private String pay;
    private String receiver;
    private String hp;
    private String addr1;
    private String addr2;
    private String memberHp;
    private String orderStatus;


    private List<AdminOrderItemDTO> orderItems;
}
