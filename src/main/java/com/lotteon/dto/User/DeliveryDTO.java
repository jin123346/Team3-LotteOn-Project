package com.lotteon.dto.User;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@ToString
public class DeliveryDTO {

    private long id ;           // Delivery의 id
    private long memberId;             // Member의 id
    private String name;               // 배송지 이름
    private String hp;                 // 휴대폰 번호
    private String postcode;           // 우편번호
    private String addr;               // 기본 주소
    private String addr2;              // 상세 주소
    private boolean isDefault;         // 기본 배송지 여부
    private String deliveryMessage;    // 배송 메시지
    private String entranceCode;       // 출입문 코드

    // 생성자
    public DeliveryDTO(long id, long memberId, String name, String hp, String postcode,
                       String addr, String addr2, boolean isDefault, String deliveryMessage, String entranceCode) {
        this.id = id;
        this.memberId = memberId;
        this.name = name;
        this.hp = hp;
        this.postcode = postcode;
        this.addr = addr;
        this.addr2 = addr2;
        this.isDefault = isDefault;
        this.deliveryMessage = deliveryMessage;
        this.entranceCode = entranceCode;
    }

}
