package com.lotteon.dto.order;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProductDeliveryDTO {

    private long productDeliveryId;

    private String receiver;       // 배송지 이름
    private String postcode;   // 우편번호
    private String addr;       // 주소
    private String addr2;      // 상세 주소

    private String deliveryMessage;  // 배송 메시지
    private String trackingnumber;          // 운송장

    private String deliveryCompany; // 택배사

    private long orderItemId;
    private OrderItemDTO orderItem;

}
