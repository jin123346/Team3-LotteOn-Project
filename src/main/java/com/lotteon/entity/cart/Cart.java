package com.lotteon.entity.cart;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lotteon.entity.User.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/*
    이름 : 최영진
    날짜 : 2024-10-29
    @JsonInclude(JsonInclude.Include.NON_NULL) 추가

*/
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@ToString(exclude = "cartItem")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;                   // Cart ID

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;                     // 사용자와 일대일 관계

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> cartItems = new ArrayList<>();      // CartItem과 일대다 관계
    private int itemQuantity;             // 장바구니에 담긴 아이템 수량
    private long totalPrice;               // 장바구니 전체 금액
    private LocalDateTime rdate;
    // 생성자, getter 및 setter 메서드
}
