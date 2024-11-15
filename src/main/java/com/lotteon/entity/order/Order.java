package com.lotteon.entity.order;


import com.lotteon.dto.User.SellerDTO;
import com.lotteon.dto.order.OrderDTO;
import com.lotteon.dto.order.OrderItemDTO;
import com.lotteon.dto.product.ProductDTO;
import com.lotteon.entity.User.Seller;
import com.lotteon.entity.product.Product;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
@Entity
@Table(name= "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;

    private String uid;
    private long totalOriginalPrice;   // 원래가격
    private long totalPrice;   // 할인된 금액
    private long totalQuantity;  //총 수량
    @Builder.Default
    private long totalDiscount=0;
    private long totalShipping;
    @Builder.Default
    private long expectedPoint=0;
    @Builder.Default
    private long productDiscount=0;

    private String receiver;
    private String hp;
    private String postcode;
    private String addr1;
    private String addr2;
    private String shippingInfo;
    private long usedPoint;   //사용 포인트
    private long usedCoupon;   //사용 포인트
    private String memberName;
    private String memberHp;

    @CreationTimestamp
    private LocalDateTime orderDate;
    private String orderStatus;
    private String pay;
    //쿠폰 사용유무
    private boolean isCoupon;
    private String couponId;


    // OrderProduct와의 OneToMany 관계 설정
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderProducts;

    private String productName; // 대표 상품명
    private String sellerCompany; // 대표 회사명

    // 셀러 정보 가져오기
    public String getSellerUid() {
        // 첫 번째 OrderItem의 Product에서 Seller 정보를 가져옵니다.
        if (orderProducts != null && !orderProducts.isEmpty()) {
            return orderProducts.get(0).getProduct().getSellerId();
        }
        return null; // 셀러 정보가 없다면 null 반환
    }


//    public OrderDTO toDTO(Order order) {
//        // Order의 모든 OrderItem을 OrderItemDTO로 변환하고, 각 OrderItem의 Seller를 가져와 전달합니다.
//        List<OrderItemDTO> orderItemDTOs = order.getOrderProducts().stream()
//                .map(orderItem -> {
//                    Seller seller = Optional.ofNullable(orderItem.getProduct())
//                            .map(Product::getSeller)
//                            .orElse(null); // Seller가 없을 경우 null로 설정
//                    return new OrderItemDTO(orderItem, seller, null); // OrderDTO는 이후에 설정됨
//                })
//                .collect(Collectors.toList());
//
//        String productName = order.getOrderProducts().isEmpty() ? "" : order.getOrderProducts().get(0).getProduct().getProductName();
//
//        return OrderDTO.builder()
//                .orderId(order.getOrderId())
//                .uid(order.getUid())
//                .totalOriginalPrice(order.getTotalOriginalPrice())
//                .totalPrice(order.getTotalPrice())
//                .totalQuantity(order.getTotalQuantity())
//                .totalDiscount(order.getTotalDiscount())
//                .totalShipping(order.getTotalShipping())
//                .expectedPoint(order.getExpectedPoint())
//                .receiver(order.getReceiver())
//                .hp(order.getHp())
//                .postcode(order.getPostcode())
//                .shippingInfo(order.getShippingInfo())
//                .usedPoint(order.getUsedPoint())
//                .orderDate(order.getOrderDate())
//                .orderStatus(order.getOrderStatus())
//                .pay(order.getPay())
//                .memberHp(order.getMemberHp())
//                .memberName(order.getMemberName())
//                .couponId(order.getCouponId())
//                .usedCoupon(order.getUsedCoupon())
//                .addr1(order.getAddr1())
//                .addr2(order.getAddr2())
//                .orderItems(orderItemDTOs)
//                .productName(productName)
//                .build();
//    }



}
