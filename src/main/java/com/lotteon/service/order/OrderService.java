package com.lotteon.service.order;

//
/*
        날짜: 2024.11.01
        이름 : 하진희
        내용 : orderService

        2024.11.08 주문시 포인트사용 적용
 */

import com.lotteon.controller.SellerController;
import com.lotteon.dto.User.SellerDTO;
import com.lotteon.dto.admin.PageRequestDTO;
import com.lotteon.dto.order.*;
import com.lotteon.dto.page.OrderPageResponseDTO;
import com.lotteon.dto.product.ProductDTO;
import com.lotteon.dto.product.ProductRedisDTO;
import com.lotteon.entity.User.Member;
import com.lotteon.entity.User.Point;
import com.lotteon.entity.User.Seller;
import com.lotteon.entity.order.Order;
import com.lotteon.entity.order.OrderItem;
import com.lotteon.entity.product.ProductOptionCombination;
import com.lotteon.repository.order.OrderItemRepository;
import com.lotteon.repository.order.OrderRepository;
import com.lotteon.repository.product.ProductOptionCombinationRepository;
import com.lotteon.repository.product.ProductRepository;
import com.lotteon.repository.user.MemberRepository;
import com.lotteon.repository.user.PointRepository;
import com.lotteon.repository.user.SellerRepository;
import com.lotteon.service.product.BestProductService;
import com.lotteon.service.product.OptionService;
import com.lotteon.service.product.ProductService;
import com.lotteon.service.user.MemberService;
import com.lotteon.service.user.PointService;
import com.lotteon.service.user.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ModelMapper getModelMapper;
    private final ProductService productService;
    private final OptionService optionService;
    private final SellerService sellerService;
    private final SellerController sellerController;
    private final ProductOptionCombinationRepository productOptionCombinationRepository;
    private final ProductRepository productRepository;

    private final SellerRepository sellerRepository;

    private final MemberRepository memberRepository;
    private final PointService pointService;
    private final PointRepository pointRepository;
    private final BestProductService bestProductService;
    private final MemberService memberService;


    @Transactional
    public long saveOrder(OrderResponseDTO orderResponseDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();


        log.info("구매중 uid : " + uid);
        long result = 0;
        OrderDTO orderDTO = orderResponseDTO.getOrder();
        log.info("orderDTO!!!!!!!!!:" + orderDTO);
        orderDTO.setUid(uid);
        Order order = getModelMapper.map(orderDTO, Order.class);
        log.info("orderEntity!!!!!!!!!:" + orderDTO);

        Order savedOrder = orderRepository.save(order);
        log.info("productDicount!!! " + savedOrder.getProductDiscount());


        result = savedOrder.getOrderId();

        // 결제 방법에 따라 orderStatus 설정
        String paymentMethod = orderResponseDTO.getOrder().getPay();
        if ("no-bankbook".equals(paymentMethod)) {
            savedOrder.setOrderStatus("waiting");
            orderRepository.save(savedOrder);
        }

        Optional<Member> member= memberRepository.findByUser_Uid(uid);
        if(member.isPresent()) {
            Member currentMember = member.get();
            long usedPoint = orderResponseDTO.getUsePoint();
            if(usedPoint>0){
                currentMember.usedPoint(usedPoint);
                memberRepository.save(currentMember);
                Point savePoint = Point.builder()
                        .orderId(result)
                        .createdAt(LocalDateTime.now())
                        .usedPoint(usedPoint)
                        .description("상품구매시 포인트 사용")
                        .member(currentMember)
                        .remainingPoints(currentMember.getPoint())
                        .build();

                pointRepository.save(savePoint);
                currentMember.saveTotalOrder(order.getTotalPrice());
                memberRepository.save(currentMember);
            }
        }

        List<OrderItemDTO> orderItems = orderResponseDTO.getOrderItems();
        for (OrderItemDTO orderItemDTO : orderItems) {
            orderItemDTO.setOrderId(savedOrder.getOrderId());
            orderItemDTO.setCustomerId(uid);
            orderItemDTO.setCustomerName(savedOrder.getMemberName());
            orderItemDTO.setOrder(getModelMapper.map(savedOrder, OrderDTO.class));


            ProductDTO product = productService.selectProduct(orderItemDTO.getProductId());
            orderItemDTO.setSeller(getModelMapper.map(product.getSeller(), Seller.class));
            orderItemDTO.setCompany(product.getSeller().getCompany());


            log.info("sellerUid가 안들어와???" + product.getSeller());
            orderItemDTO.setSellerUid(product.getSellerId());
            orderItemDTO.setProduct(product);

            if (orderItemDTO.getSelectOption() != null) {
                orderItemDTO.setOptionId(orderItemDTO.getOptionId());

                // option재고 업데이트
                Optional<ProductOptionCombination> productOptionCombination = productOptionCombinationRepository.findById(orderItemDTO.getOptionId());
                if (productOptionCombination.isPresent()) {
                    long savestock = productOptionCombination.get().getStock() - orderItemDTO.getStock();
                    productOptionCombinationRepository.updateQuantity(savestock, orderItemDTO.getCombinationId());
                    log.info("업데이트 재고 : " + savestock);
                }
            }
            long saveStock = product.getStock() - orderItemDTO.getStock();
            long saveSold = product.getSold() + orderItemDTO.getStock();
            productRepository.updateProductQuantity(saveStock, orderItemDTO.getProductId(), saveSold);
            log.info("업데이트 재고 : " + saveStock);


            //각각의 오더아이템 저장
            OrderItem OrderItem = getModelMapper.map(orderItemDTO, OrderItem.class);
            OrderItem savedOrderItem = orderItemRepository.save(OrderItem);


            ProductRedisDTO productRedisDTO = ProductRedisDTO.builder()
                    .productId(savedOrderItem.getProduct().getProductId())
                    .productName(savedOrderItem.getProduct().getProductName())
                    .categoryId(savedOrderItem.getProduct().getCategoryId())
                    .discount(savedOrderItem.getProduct().getDiscount())
                    .file230(savedOrderItem.getProduct().getFile230())
                    .savedPath(savedOrderItem.getProduct().getSavedPath())
                    .build();
            bestProductService.updateSellingInRedis(productRedisDTO, savedOrderItem.getStock());

        }


        return result;

    }

    //사용자별 유저 찾기
    public OrderCompletedResponseDTO selectOrderById(long id) {
        Order order = orderRepository.findByOrderId(id);
        log.info("order::::::::::::: " + order);
        //orderDTO에 orderItem이 없다.
        OrderDTO orderDTO = getModelMapper.map(order, OrderDTO.class);
        log.info("OrderDTO::::::::::: " + orderDTO);
        HashSet<SellerDTO> sellers = new HashSet<>();
        List<OrderItem> orderItems = order.getOrderProducts();
        orderDTO.setOrderItems(orderItems.stream().map((element) -> getModelMapper.map(element, OrderItemDTO.class)).collect(Collectors.toList()));

        List<OrderItemDTO> orderItemDtos = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            OrderItemDTO orderItemDTO = getModelMapper.map(orderItem, OrderItemDTO.class);
            String sellerid = orderItem.getProduct().getSellerId();

            if (orderItem.getOptionId() != 0) {
                Optional<ProductOptionCombination> opt = productOptionCombinationRepository.findById(orderItem.getOptionId());
                if (opt.isPresent()) {
                    ProductOptionCombination optionCombination = opt.get();
                    orderItemDTO.setCombination(optionCombination.getCombination());
                    orderItemDTO.setCombinationId(optionCombination.getCombinationId());
                    orderItemDTO.setOrderItemId(orderItem.getOrderItemId());

                }
            }

            orderItemDtos.add(orderItemDTO);

            SellerDTO seller = sellerService.getSeller(sellerid);
            seller.setUid(sellerid);
            sellers.add(seller);
        }
        orderDTO.setOrderItems(orderItemDtos);

        log.info("sellereeeeeee:" + sellers);

        List<OrderDTO> orderDTOSs = new ArrayList<>();

        return new OrderCompletedResponseDTO(orderDTO, sellers,orderDTOSs);
    }

    //seller별 orderItem 찾기
    public List<OrderDTO> selectOrderItemsBySeller() {
        return null;
    }


    public OrderPageResponseDTO<OrderItemDTO> getOrderByUserItems(PageRequestDTO pageRequestDTO, String uid) {
        Pageable pageable = pageRequestDTO.getPageable("orderItemId");  // orderItemId로 정렬

        // 사용자 ID로 OrderItems 페이징 조회
        Page<OrderItem> pageOrderItems = orderItemRepository.findByOrder_UidOrderByOrderItemIdDesc(uid, pageable);

        // OrderItem을 OrderItemDTO로 변환
        List<OrderItemDTO> orderItemList = pageOrderItems.stream()
                .map(orderItem -> {
                    // 각 OrderItem에 대해 Seller 정보 추가
                    Seller seller = sellerRepository.findByUserUid(orderItem.getSellerUid()).orElse(null);
                    return new OrderItemDTO(orderItem, seller, orderItem.getOrder());
                })
                .collect(Collectors.toList());

        int total = (int) pageOrderItems.getTotalElements();

        return OrderPageResponseDTO.<OrderItemDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(orderItemList)
                .total(total)
                .build();
    }

//    public List<OrderWithGroupedItemsDTO> getOrdersGroupedBySeller(String uid) {
//        List<Object[]> results = orderRepository.findOrderAndOrderItemsByUid(uid);
//        Map<Long, OrderWithGroupedItemsDTO> orderMap = new HashMap<>();
//
//        for (Object[] row : results) {
//            Order order = (Order) row[0];
//            OrderItem orderItem = (OrderItem) row[1];
//            String sellerUid = orderItem.getSellerUid();
//
//            // OrderWithGroupedItemsDTO가 없으면 생성하여 추가
//            OrderWithGroupedItemsDTO orderDTO = orderMap.computeIfAbsent(order.getOrderId(), id ->
//                    new OrderWithGroupedItemsDTO(order.getOrderId(), order.getUid(), order.getOrderDate(), new ArrayList<>())
//            );
//
//            String company=null;
//            // SellerOrderItemDTO를 찾거나 생성하여 추가
//            SellerOrderItemDTO sellerOrderItemDTO = orderDTO.getGroupedOrderItems().stream()
//                    .filter(s -> s.getSellerUid().equals(sellerUid))
//                    .findFirst()
//                    .orElseGet(() -> {
//                        String companyIn = sellerService.findCompanyByuid(sellerUid);
//                        SellerOrderItemDTO newSellerDTO = new SellerOrderItemDTO(sellerUid,companyIn, new ArrayList<>());
//                        orderDTO.getGroupedOrderItems().add(newSellerDTO);
//                        return newSellerDTO;
//                    });
//
//            // OrderItem을 해당 SellerOrderItemDTO에 추가
//            sellerOrderItemDTO.getOrderItems().add(orderItem);
//        }
//
//        return new ArrayList<>(orderMap.values());
//    }

    public List<OrderItemDTO> getOrdersGroupedByOrderItemId(String userId) {
        // 사용자 ID로 주문을 조회하고 주문 날짜 순으로 정렬 (최근 날짜부터)
        List<Order> orders = orderRepository.findByUidOrderByOrderDateDesc(userId);

        // 현재 날짜 기준으로 3일 전 날짜 계산
        LocalDate threeDaysAgo = LocalDate.now().minusDays(3);

        // 3일 이내의 주문만 필터링하고, Order 객체를 OrderItemDTO로 변환
        return orders.stream()
                .filter(order -> order.getOrderDate().toLocalDate().isAfter(threeDaysAgo))  // 3일 이내의 주문만 필터링
                .flatMap(order -> order.getOrderProducts().stream()  // 주문 아이템들로 평평하게 만들기
                        .map(item -> {
                            // Seller 정보를 추가하여 OrderItemDTO를 반환
                            Seller seller = sellerRepository.findByUserUid(item.getSellerUid()).orElse(null);
                            return new OrderItemDTO(item, seller, order);  // order를 전달
                        })
                )
                .collect(Collectors.groupingBy(OrderItemDTO::getOrderItemId))  // orderItemId로 그룹화
                .entrySet()  // Map.Entry Set으로 변환
                .stream()  // 스트림으로 변환
                .flatMap(entry -> entry.getValue().stream())  // 그룹화된 리스트를 스트림으로 변환
                .collect(Collectors.toList());  // 최종적으로 리스트로 변환
    }




//    public List<OrderDTO> selectOrderByuid(String uid,Pageable pageable) {
//
//        List<Order> orders =  orderRepository.findByUid(uid,pageable);
//        List<OrderDTO> orderDTOs = new ArrayList<>();
//        for(Order order : orders){
//            OrderDTO orderDTO = order.toDTO(order);
//
//            List<OrderItem> items= order.getOrderProducts();
//            List<OrderItemDTO> itemDTOS = new ArrayList<>();
//
//            String path =items.get(0).getProduct().getSavedPath();
//            String image=items.get(0).getProduct().getFile190();
//
//            if(path!=null){
//                image = path+items.get(0).getProduct().getFile190();
//            }
//            orderDTO.setPath(path);
//            orderDTO.setImage(image);
//            orderDTO.setOrderItems(itemDTOS);
//            orderDTOs.add(orderDTO);
//        }
//
//        return orderDTOs;
//    }


//update Order

//deleteOrder

//반품요청

//환불요청

    public long getSalesCountBySeller(String sellerUid) {
        long salesCount = orderItemRepository.countBySellerUid(sellerUid);
        return (salesCount < 0) ? 0 : salesCount;  // 음수일 경우 0 반환
    }

    public long getTotalSalesAmountBySeller(String sellerUid) {
        Long totalSalesAmount = orderItemRepository.findTotalOrderPriceBySellerUid(sellerUid);
        return (totalSalesAmount == null || totalSalesAmount < 0) ? 0 : totalSalesAmount;  // null 또는 음수일 경우 0 반환
    }

    // 모든 판매자의 총 판매 수량을 반환
    public long getTotalSalesCountForAllSellers() {
        return orderItemRepository.findTotalOrderCountForAllSellers();
    }

    // 모든 판매자의 총 판매 금액을 반환
    public long getTotalSalesAmountForAllSellers() {
        return orderItemRepository.findTotalOrderPriceForAllSellers();
    }

    // 특정 판매자의 날짜 범위에 따른 주문 건수
    public long getSalesCountBySellerAndDateRange(String sellerUid, LocalDateTime start, LocalDateTime end) {
        // countOrdersBySellerAndDateRange가 0보다 작은 값을 반환할 경우 0으로 설정
        long count = orderItemRepository.countOrdersBySellerAndDateRange(sellerUid, start, end);
        return (count < 0) ? 0 : count;  // 음수일 경우 0 반환
    }

    // 특정 판매자의 날짜 범위에 따른 총 판매 금액
    public long getTotalSalesAmountBySellerAndDateRange(String sellerUid, LocalDateTime start, LocalDateTime end) {
        Long amount = orderItemRepository.sumSalesAmountBySellerAndDateRange(sellerUid, start, end);
        return amount != null ? amount : 0; // 금액이 null일 경우 0으로 처리
    }

    // 모든 판매자의 날짜 범위에 따른 주문 건수
    public long getTotalSalesCountForAllSellersByDateRange(LocalDateTime start, LocalDateTime end) {
        return orderItemRepository.countOrdersByDateRange(start, end);
    }

    // 모든 판매자의 날짜 범위에 따른 총 판매 금액
    public long getTotalSalesAmountForAllSellersByDateRange(LocalDateTime start, LocalDateTime end) {
        Long amount = orderItemRepository.sumSalesAmountByDateRange(start, end);
        return amount != null ? amount : 0;
    }

    public List<CategoryOrderCountDTO> getOrderCountGroupedByCategoryFirstId() {
        List<CategoryOrderCountDTO> newCategoryCount = orderItemRepository.selectCount();

        return newCategoryCount;
    }

    public Long getOrderCount(String uid){
        List<Order> orderCount = orderRepository.findByUid(uid); // 여러 개의 주문을 가져옴

          return (long) orderCount.size(); // 주문 개수를 반환

    }


    public List<CategoryOrderCountDTO> getCountPaymentGroupedByCategoryFirstId() {
        List<CategoryOrderCountDTO> count = orderItemRepository.selectCountByCompletedGroupCategory();
        return count;
    }

    public List<CategoryOrderCountDTO> getCountCancelGroupedByCategoryFirstId() {
        List<CategoryOrderCountDTO> count = orderItemRepository.selectCountByCancelGroupCategory();
        return count;
    }

    public boolean updateOrderStatusToConfirmation(Long orderItemId) {
        Optional<OrderItem> orderItemOptional = orderItemRepository.findById(orderItemId);

        if (orderItemOptional.isPresent()) {
            OrderItem orderItem = orderItemOptional.get();
            orderItem.setStatus(DeliveryStatus.CONFIRMATION);  // 올바른 열거형 값 할당  // 상태를 CONFIRMATION으로 설정
            orderItemRepository.save(orderItem);  // 상태 변경된 객체 저장
            Optional<Member> opt = memberService.findByUserId(orderItem.getCustomerId());
            if (opt.isPresent()) {
                Member member = opt.get();
                member.setPoint(orderItem.getPoint());

                Point point = Point.builder()
                        .orderId(orderItemId)
                        .member(member)
                        .createdAt(LocalDateTime.now())
                        .description("상품구매 확정")
                        .amount(orderItem.getPoint())
                        .build();
                pointRepository.save(point);
                memberRepository.save(member);
            }
            long svaedPoint = orderItem.getPoint();



            return true;
        } else {
            return false;
        }
    }

    public boolean updateOrderStatusToConfirmation2(Long orderItemId) {
        Optional<OrderItem> orderItemOptional = orderItemRepository.findById(orderItemId);

        if (orderItemOptional.isPresent()) {
            OrderItem orderItem = orderItemOptional.get();

            // 이미 상태가 CONFIRMATION이면 반품을 할 수 없음
            if (orderItem.getStatus() == DeliveryStatus.CONFIRMATION) {
                return false; // 상태가 CONFIRMATION인 경우 반품 불가
            }

            orderItem.setStatus(DeliveryStatus.RETURN_REQUESTED);  // 상태를 RETURN_REQUESTED로 설정
            orderItemRepository.save(orderItem);  // 상태 변경된 객체 저장
            return true;
        } else {
            return false;
        }
    }

    public boolean updateOrderStatusToConfirmation3(Long orderItemId) {
        Optional<OrderItem> orderItemOptional = orderItemRepository.findById(orderItemId);

        if (orderItemOptional.isPresent()) {
            OrderItem orderItem = orderItemOptional.get();

            // 이미 상태가 CONFIRMATION이면 반품을 할 수 없음
            if (orderItem.getStatus() == DeliveryStatus.CONFIRMATION) {
                return false; // 상태가 CONFIRMATION인 경우 반품 불가
            }

            orderItem.setStatus(DeliveryStatus.EXCHANGE_REQUESTED);  // 상태를 RETURN_REQUESTED로 설정
            orderItemRepository.save(orderItem);  // 상태 변경된 객체 저장
            return true;
        } else {
            return false;
        }
    }

    public long getWaitingOrderCount() {
        return orderRepository.countOrdersByStatus("waiting");  // "waiting" 상태의 주문 개수
    }

    public List<Object[]> getWaitingDepositOrderCountBySeller(String sellerUid) {
        // "WAITING_FOR_PAYMENT" 상태의 주문 수를 카운트
        return orderItemRepository.findSellerUidAndOrderStatusBySellerUidAndOrderStatus(sellerUid, "WAITING");
    }




}




