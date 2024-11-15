package com.lotteon.repository.user;

import com.lotteon.entity.User.Seller;
import com.lotteon.entity.order.Order;
import com.lotteon.entity.order.OrderItem;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {

    Optional<Seller> findById(long id);
    Optional<Seller> findByUserUid(String userUid); // UserUid로 조회

    void deleteAllByIdIn(List<Long> sellerIds);

    @Query("SELECT oi FROM OrderItem oi JOIN FETCH oi.product p JOIN FETCH p.sellerId WHERE oi.order = :order")
    List<OrderItem> findOrderItemsWithSeller(@Param("order") Order order);





}

