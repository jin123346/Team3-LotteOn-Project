package com.lotteon.dto.User;

import com.lotteon.dto.order.OrderItemDTO;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SellerDTO {

    @Id
    private Long id;

    private String  company;

    private String  ceo;

    private String  bno;

    private String  mo;

    private String hp;

    private String fax;

    private String addr;

    private String addr2;

    private String postcode;

    private String brand;

    private String grade;

    private LocalDateTime regDate;

    //추가필드
    private List<OrderItemDTO> orderItems;
    private String uid;
    private long totalOriginalPrice;
    private long totalFinalPrice;
    private long totalDiscount;
    private long totalShipping;


    // uid 필드를 기준으로 equals와 hashCode 오버라이드
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SellerDTO sellerDTO = (SellerDTO) o;
        return Objects.equals(uid, sellerDTO.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid);
    }

    private String status;


}
