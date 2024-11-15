package com.lotteon.repository.order;

import com.lotteon.entity.order.ProductDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDeliveryRepository extends JpaRepository<ProductDelivery,Long> {
}
