package com.lotteon.entity.order;


import com.lotteon.entity.User.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productDeliveryId;

    private String receiver;       // 배송지 이름
    private String postcode;   // 우편번호
    private String addr;       // 주소
    private String addr2;      // 상세 주소

    private String deliveryMessage;  // 배송 메시지
    private String trackingnumber;          // 운송장

    private String deliveryCompany; // 택배사

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderItemId")
    private OrderItem orderItem;




}
