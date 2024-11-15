package com.lotteon.repository.order;

import com.lotteon.dto.admin.AdminOrderItemDTO;
import com.lotteon.dto.order.DeliveryStatus;
import com.lotteon.entity.User.Seller;
import com.lotteon.entity.order.OrderItem;
import com.lotteon.repository.custom.OrderItemRepositoryCustom;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> , OrderItemRepositoryCustom {


    long countBySellerUid(String sellerUid);

    @Query("SELECT new com.lotteon.dto.admin.AdminOrderItemDTO(oi.orderItemId, oi.product.productName, oi.order.orderId, oi.status, oi.price, oi.savedPrice, oi.orderPrice, oi.savedDiscount, oi.shippingFees, oi.stock, oi.product.productId, oi.product.file190, oi.product.savedPath) FROM OrderItem oi WHERE oi.order.orderId = :orderId")
    List<AdminOrderItemDTO> findByOrder_OrderId(Long orderId);
    Page<OrderItem> findByOrder_UidOrderByOrderItemIdDesc(String uid, Pageable pageable);


    long countByStatus(DeliveryStatus status);

    Page<OrderItem> findBySellerUid(String uid, Pageable pageable);
//    Page<OrderItem> findAll(Pageable pageable);

    @Query("SELECT SUM(o.orderPrice) FROM OrderItem o WHERE o.sellerUid = :sellerUid")
    Long findTotalOrderPriceBySellerUid(@Param("sellerUid") String sellerUid);


    // 모든 판매자의 총 판매 수량을 계산하는 메서드
    @Query("SELECT COUNT(o) FROM OrderItem o")
    Long findTotalOrderCountForAllSellers();

    // 모든 판매자의 총 판매 금액을 계산하는 메서드
    @Query("SELECT SUM(o.orderPrice) FROM OrderItem o")
    Long findTotalOrderPriceForAllSellers();

    // 특정 판매자의 날짜 범위 내 주문 건수
    @Query("SELECT COUNT(oi) FROM OrderItem oi JOIN oi.order o WHERE oi.sellerUid = :sellerUid AND o.orderDate BETWEEN :start AND :end")
    long countOrdersBySellerAndDateRange(@Param("sellerUid") String sellerUid, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // 특정 판매자의 날짜 범위 내 총 판매 금액
    @Query("SELECT SUM(oi.orderPrice) FROM OrderItem oi JOIN oi.order o WHERE oi.sellerUid = :sellerUid AND o.orderDate BETWEEN :start AND :end")
    Long sumSalesAmountBySellerAndDateRange(@Param("sellerUid") String sellerUid, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // 모든 판매자의 어제 또는 오늘의 주문 건수를 가져오는 메서드
    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderDate BETWEEN :start AND :end")
    long countOrdersByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // 모든 판매자의 어제 또는 오늘의 주문 금액 합계를 가져오는 메서드
    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.orderDate BETWEEN :start AND :end")
    long sumSalesAmountByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(oi) FROM OrderItem oi WHERE oi.sellerUid = :sellerUid AND oi.status = :status")
    long countReadyForShippingBySellerUid(@Param("sellerUid") String sellerUid, @Param("status") DeliveryStatus status);

    @Query("SELECT COUNT(oi) FROM OrderItem oi WHERE oi.status = :status")
    long countAllReadyForShipping(@Param("status") DeliveryStatus status);

    @Query("SELECT oi.sellerUid, o.orderStatus FROM OrderItem oi JOIN oi.order o WHERE oi.sellerUid = :sellerUid AND o.orderStatus = :orderStatus")
    List<Object[]> findSellerUidAndOrderStatusBySellerUidAndOrderStatus(@Param("sellerUid") String sellerUid, @Param("orderStatus") String orderStatus);




}


