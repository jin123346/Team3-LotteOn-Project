package com.lotteon.service.admin;

import com.lotteon.controller.AdminOrderItemPageResponseDTO;
import com.lotteon.dto.User.SellerDTO;
import com.lotteon.dto.admin.AdminOrderDTO;
import com.lotteon.dto.admin.AdminOrderItemDTO;
import com.lotteon.dto.order.DeliveryStatus;
import com.lotteon.dto.order.OrderDTO;
import com.lotteon.dto.order.OrderItemDTO;
import com.lotteon.dto.order.ProductDeliveryDTO;
import com.lotteon.dto.page.AdminOrderPageResponseDTO;
import com.lotteon.dto.page.PageRequestDTO;
import com.lotteon.dto.product.ProductDTO;
import com.lotteon.entity.User.Seller;
import com.lotteon.entity.order.Order;
import com.lotteon.entity.order.OrderItem;
import com.lotteon.entity.order.ProductDelivery;
import com.lotteon.entity.product.Product;
import com.lotteon.repository.order.OrderItemRepository;
import com.lotteon.repository.order.OrderRepository;
import com.lotteon.repository.order.ProductDeliveryRepository;
import com.lotteon.repository.user.SellerRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class AdminOrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ModelMapper modelMapper;
    private final ModelMapper getModelMapper;
    private final SellerRepository sellerRepository;
    private final ProductDeliveryRepository productDeliveryRepository;

    public List<OrderDTO> selectOrdersAll() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDTO> orderDTOs = new ArrayList<>();

        for (Order order : orders) {
            OrderDTO dto = getModelMapper.map(order, OrderDTO.class);
            orderDTOs.add(dto);
            log.info("이값이뭐야 야옹 asdf:" + orderDTOs);
        }
        return orderDTOs;

    }
    public OrderItemDTO selectOrderItemById(long id) {
        OrderItem orderItem = orderItemRepository.findById(id).orElse(null);
        log.info("이거 값이 나오냐냐?! : " + orderItem );
        OrderItemDTO orderItemDTO = getModelMapper.map(orderItem, OrderItemDTO.class);
        return orderItemDTO;
    }

    public Page<OrderItemDTO> convertOrderItemsToOrderDTOs(Page<OrderItem> orderItems, Pageable pageable) {
        List<OrderItemDTO> orderItemDTOS = orderItems.getContent().stream()
                .map(orderItem -> {
                    OrderItemDTO orderItemDTO = getModelMapper.map(orderItem, OrderItemDTO.class);
                    // OrderDTO 생성 및 설정
                    OrderDTO orderDTO = new OrderDTO();
                    // OrderItem의 필드를 OrderDTO에 매핑
                    orderItemDTO.setOrderId(orderItem.getOrder().getOrderId());
                    orderItemDTO.setCustomerId(orderItem.getOrder().getUid());
                    orderItemDTO.setCustomerName(orderItem.getOrder().getMemberName());
                    orderDTO.setTotalPrice(orderItem.getOrder().getTotalPrice());
                    orderDTO.setOrderDate(orderItem.getOrder().getOrderDate());
                    // Product 설정 (Product -> ProductDTO 변환 예시)
                    Product product = orderItem.getProduct();
                    orderItemDTO.setPath(product.getSavedPath());

                    if (product != null) {
                        ProductDTO productDTO = new ProductDTO();
                        productDTO.setProductId(product.getProductId());
                        productDTO.setProductName(product.getProductName());
                        productDTO.setPrice(product.getPrice());
                        orderItemDTO.setProduct(productDTO);
                        orderItemDTO.setPath(product.getSavedPath());

                        // 필요한 필드 추가 설정
                    }
                    String sellerUid= product.getSellerId();
                    Optional<Seller> seller = sellerRepository.findByUserUid(sellerUid);
                    if (seller.isPresent()) {
                        Seller sel = seller.get();
                        orderItemDTO.setSeller(sel);
                    }
                    // 다른 필요한 필드 매핑
                    return orderItemDTO;
                })
                .collect(Collectors.toList());
        return new PageImpl<>(orderItemDTOS, pageable, orderItems.getTotalElements());
    }



    public AdminOrderItemPageResponseDTO selectOrderItemListAll(PageRequestDTO pageRequestDTO) {
        //지니가 지현이에게
        Pageable pageable2 = pageRequestDTO.getPageable("orderItemId");
        Page<OrderItem> orderItems = null;
        log.info("pppppp : " + pageRequestDTO.getKeyword());
        log.info("llllll : " + pageRequestDTO.getType());
        if(pageRequestDTO.getKeyword() == null){
            orderItems = orderItemRepository.findAll(pageable2);
        }else {
            orderItems = orderItemRepository.selectOrderSearchForList(pageRequestDTO,pageable2);
        }
        Page<OrderItemDTO> getOrderItems = convertOrderItemsToOrderDTOs(orderItems, pageable2);
        log.info("orderITems!!!!!"+orderItems.getContent());
        log.info("qqqqqqqqqqqqqqqqqqqq:" + getOrderItems.getContent());
        log.info("cccccccccccccccccccc:" + getOrderItems.getPageable());
        List<OrderItemDTO> orderItemList = getOrderItems.getContent();
        int total2 = (int) getOrderItems.getTotalElements();
        log.info("total:" + total2);
        log.info("uuuuuuuuuuuu:" + getOrderItems);
        return AdminOrderItemPageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .adminorderitemdtoList(orderItemList)
                .total(total2)
                .build();
    }

    public AdminOrderDTO selectOrderAll(long id){
        Order order = orderRepository.findById(id).orElse(null);
        AdminOrderDTO adminOrderDTO = getModelMapper.map(order, AdminOrderDTO.class);
        List<AdminOrderItemDTO> orderItemdtos = orderItemRepository.findByOrder_OrderId(id);
        adminOrderDTO.setOrderItems(orderItemdtos);
        return adminOrderDTO;
    }





    public AdminOrderPageResponseDTO selectOrderListAll(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("no");
        Page<Tuple> pageAdminOrder = null;


        log.info("abababababab:" + pageRequestDTO.getKeyword());
        if (pageRequestDTO.getKeyword() == null) {
            pageAdminOrder = orderRepository.selectOrderAllForList(pageRequestDTO, pageable);
        } else {
            pageAdminOrder = orderRepository.selectOrderSearchForList(pageRequestDTO, pageable);
        }


        List<AdminOrderDTO> orderList = pageAdminOrder.getContent().stream().map(tuple -> {
            Long id = tuple.get(0, Long.class);
            log.info("이거 aaaaaaaid머야 나와?:" + id);

            Order orders = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Faq not found with ID: " + id));
            ; //조건주고 조회하기
            log.info("이게 order!! 머야?:" + orders);

    //            List<OrderItem> orderItems = orderItemRepository.findByOrder_OrderId(id);
            List<AdminOrderItemDTO> orderItemdtos = orderItemRepository.findByOrder_OrderId(id);
            log.info("아니 orderItems가 계속 조회가 안됨: " + orderItemdtos);

            AdminOrderDTO adminOrderDTO = getModelMapper.map(orders, AdminOrderDTO.class);
            // OrderItem들을 OrderItemDTO로 변환


    //            List<OrderItemDTO> orderItemDTOs = orderItems.stream()
    //                    .map(orderItem -> modelMapper.map(orderItem, OrderItemDTO.class))
    //                    .collect(Collectors.toList());

            log.info("아니 orderItemDTO를 변환해야돼 : " + orderItemdtos);


            adminOrderDTO.setOrderItems(orderItemdtos);
            log.info("야옹하고울어요!: " + adminOrderDTO);
            return adminOrderDTO;


        }).toList();
        int total = (int) pageAdminOrder.getTotalElements();

        return AdminOrderPageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .adminorderdtoList(orderList)
                .total(total)
                .build();

    }
    public ProductDelivery insertorderDelivery(long id , ProductDeliveryDTO productDeliveryDTO){

        ProductDelivery productDelivery = ProductDelivery.builder()
                .deliveryCompany(productDeliveryDTO.getDeliveryCompany())
                .addr(productDeliveryDTO.getAddr())
                .deliveryMessage(productDeliveryDTO.getDeliveryMessage())
                .addr2(productDeliveryDTO.getAddr2())
                .trackingnumber(productDeliveryDTO.getTrackingnumber())
                .postcode(productDeliveryDTO.getPostcode())
                .receiver(productDeliveryDTO.getReceiver())
                .build();

        OrderItem orderItem = orderItemRepository.findById(id).orElse(null);
        productDelivery.setOrderItem(orderItem);
        ProductDelivery delivery = productDeliveryRepository.save(productDelivery);

        orderItem.setDeliveryId(delivery.getProductDeliveryId());
        orderItem.setStatus(DeliveryStatus.DELIVERED);
        orderItem.setTraceNumber(delivery.getTrackingnumber());
        orderItemRepository.save(orderItem);

        return delivery;
    }
}