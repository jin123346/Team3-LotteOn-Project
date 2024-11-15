package com.lotteon.dto.admin;

import com.lotteon.dto.order.DeliveryStatus;
import com.lotteon.dto.order.OrderDTO;
import com.lotteon.dto.product.ProductDTO;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
public class AdminOrderItemDTO {

    //
    private long orderItemId;
    //상품명
    private String productName;

    private long orderId;

    private DeliveryStatus status;

    //상태
    private long price;
    //가격
    private long savedPrice;
    //결제금액
    private long orderPrice;
    //할인
    private long savedDiscount;
    //배달비
    private long shippingFees;
    //재고
    private long stock;
    //상품번호
    private Long productId;
    //파일이름
    private String file190;
    //파일경로
    private String savedPath;


}
