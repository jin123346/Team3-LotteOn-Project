package com.lotteon.dto.order;

import com.lotteon.entity.User.Seller;
import com.lotteon.entity.order.Order;
import com.lotteon.repository.user.SellerRepository;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class OrderWithGroupedItemsDTO {

    private long orderId;
    private String uid;
    private LocalDateTime orderDate;
    private String productName;
    private List<OrderItemDTO> groupedOrderItems;

    public OrderWithGroupedItemsDTO(Order order, SellerRepository sellerRepository) {
        this.orderId = order.getOrderId();
        this.orderDate = order.getOrderDate();

        // OrderDTO 객체를 생성하여 넘겨줌
        OrderDTO orderDTO = new OrderDTO(order);  // 필요한 필드를 OrderDTO에 추가하세요

        this.groupedOrderItems = order.getOrderProducts().stream()
                .map(item -> {
                    Seller seller = sellerRepository.findByUserUid(item.getSellerUid())
                            .orElse(null);
                    return new OrderItemDTO(item, seller, order);  // OrderDTO를 넘겨줌
                })
                .collect(Collectors.toList());
    }
}
