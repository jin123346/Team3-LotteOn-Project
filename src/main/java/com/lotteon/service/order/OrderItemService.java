package com.lotteon.service.order;

import com.lotteon.entity.order.OrderItem;
import com.lotteon.repository.order.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public List<OrderItem> findByUid() {
        return null;
    }
}
