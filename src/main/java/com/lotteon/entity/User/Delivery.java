package com.lotteon.entity.User;


import com.lotteon.entity.order.OrderItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;       // 배송지 이름
    private String hp;         // 휴대폰 번호
    private String postcode;   // 우편번호
    private String addr;       // 주소
    private String addr2;      // 상세 주소

    private boolean isDefault;  // 기본 배송지 여부
    private String deliveryMessage;  // 배송 메시지
    private String entranceCode;          // 공동현관 출입번호

    private String deliveryCompany;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "orderItemId")
//    private OrderItem orderItem;

    // Member와의 다대일 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 기본 주소 설정
    public void setAsDefault() {
        this.isDefault = true;
    }

}
