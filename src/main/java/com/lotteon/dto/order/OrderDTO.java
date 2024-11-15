package com.lotteon.dto.order;


import com.lotteon.dto.User.SellerDTO;
import com.lotteon.entity.User.Seller;
import com.lotteon.entity.order.Order;
import com.lotteon.entity.product.Product;


import com.lotteon.repository.user.SellerRepository;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
public class OrderDTO {

    private long orderId;
    private String uid;
    private long totalOriginalPrice;
    private long totalPrice;
    private long totalQuantity;
    private long totalDiscount;
    private long totalShipping;
    private long productDiscount=0;
    private long expectedPoint;
    private String receiver;
    private String hp;
    private String postcode;
    private String addr1;
    private String addr2;
    private String shippingInfo;
    private long usedPoint;   //사용 포인트
    private long usedCoupon;   //사용 포인트
    private LocalDateTime orderDate;
    private String orderStatus;
    private String pay;
    //쿠폰 사용유무
    private boolean isCoupon;
    private String couponId;
    private long couponDiscount;
    private SellerDTO seller;
    private List<OrderItemDTO> orderItems;
    private long totalPoint;
    private String memberName;
    private String memberHp;

    //추가필드
    private List<Long> orderItemIds;
    private String image;
    private String path;
    private String productName;
    private String company;

    private Product product;

    public OrderDTO(Order order) {

        this.totalDiscount = order.getTotalDiscount();
        this.totalPrice = order.getTotalPrice();
        this.totalOriginalPrice = order.getTotalOriginalPrice();
        this.orderDate = order.getOrderDate();
        this.orderId = order.getOrderId();
        this.memberName = order.getMemberName();
        this.addr1 = order.getAddr1();
        this.addr2 = order.getAddr2();
        this.shippingInfo = order.getShippingInfo();
        this.memberHp = order.getMemberHp();
        this.productName = order.getProductName();

    }

    public OrderDTO(Order order, SellerRepository sellerRepository) {

        this.orderId = order.getOrderId();
        this.orderDate = order.getOrderDate();
        
        OrderDTO orderDTO = new OrderDTO(order);  // 필요한 필드를 OrderDTO에 추가하세요

        this.orderItems = order.getOrderProducts().stream()
                .map(item -> {
                    Seller seller = sellerRepository.findByUserUid(item.getSellerUid())
                            .orElse(null);
                    return new OrderItemDTO(item, seller, order);  // OrderDTO를 넘겨줌
                })
                .collect(Collectors.toList());
    }


    public Order toEntity() {
        return  Order.builder()
                .isCoupon(this.isCoupon)
                .orderId(this.orderId)
                .uid(this.uid)
                .totalOriginalPrice(this.totalOriginalPrice)
                .totalPrice(this.totalPrice)
                .totalQuantity(this.totalQuantity)
                .totalDiscount(this.totalDiscount)
                .totalShipping(this.totalShipping)
                .expectedPoint(this.expectedPoint)
                .receiver(this.receiver)
                .hp(this.hp)
                .postcode(this.postcode)
                .shippingInfo(this.shippingInfo)
                .usedPoint(this.usedPoint)
                .orderDate(this.orderDate)
                .orderStatus(this.orderStatus)
                .pay(this.pay)
                .memberHp(this.memberHp)
                .memberName(this.memberName)
                .couponId(this.couponId)
                .usedCoupon(this.usedCoupon)
                .productDiscount(this.productDiscount)
                .addr1(this.addr1)
                .addr2(this.addr2)
                .build();
    }




}
