    package com.lotteon.dto.order;

    import com.fasterxml.jackson.annotation.JsonBackReference;
    import com.fasterxml.jackson.annotation.JsonIgnore;
    import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
    import com.lotteon.dto.User.SellerDTO;
    import com.lotteon.dto.product.OptionDTO;
    import com.lotteon.dto.product.ProductDTO;
    import com.lotteon.entity.User.Seller;
    import com.lotteon.dto.User.SellerDTO;
    import com.lotteon.entity.order.Order;
    import com.lotteon.entity.order.OrderItem;
    import com.lotteon.entity.product.Review;
    import jakarta.persistence.Transient;
    import lombok.*;

    import java.text.DecimalFormat;
    import java.util.ArrayList;
    import java.util.List;

    @Getter
    @Setter
    @AllArgsConstructor
    @RequiredArgsConstructor
    @ToString
    @Builder
    public class OrderItemDTO {


        private long orderItemId;

        private OrderDTO order;
        @JsonIgnoreProperties({"reviewContent", "optionCombinations"})
        private ProductDTO product;
        private long productId;
        private long categoryId;
        private long savedPrice;
        private long savedDiscount;
        private long orderPrice;
        @JsonIgnore
        private List<Review> reviewContent;
        private long orderId;
        private long optionId;
        private String optionDesc;
        private String combination;
        private long combinationId;
        private int stock;
        private long price;
        private String traceNumber;
        private String sellerUid;
        private long shippingTerms;
        private long shippingFees;
        private long point;
        private String orderStatus;


        private DeliveryStatus status;
        @Transient
        @JsonBackReference
        private Seller seller;

        private String company;
        private String image;
        private String path;
        private String customerId;  //  구매자
        private String customerName; //구매자이름

        private String productName;
        //selectOption
        private OptionDTO selectOption;
        private String formattedPrice;

        private String orderDate;
        private String hp;
        private String addr1;
        private String addr2;
        private String shippingInfo;
        private String pay;



        public OrderItemDTO(OrderItem item, Seller seller, Order order) {
            this.orderItemId = item.getOrderItemId();
            this.savedPrice = item.getSavedPrice();
            this.orderPrice = item.getOrderPrice();
            this.savedDiscount = item.getSavedDiscount();
            this.point = item.getPoint();
            this.sellerUid = item.getSellerUid();
            this.optionDesc = item.getOptionDesc();
            this.optionId = item.getOptionId();
            this.combination = item.getCombination();
            this.combinationId = item.getCombinationId();
            this.stock = (int) item.getStock();
            this.price = item.getPrice();
            this.traceNumber = item.getTraceNumber();
            this.shippingFees = item.getShippingFees();
            this.status = item.getStatus();
            this.customerName = item.getCustomerName();
            this.customerId = item.getCustomerId();
            this.sellerUid = (seller != null) ? seller.getUser().getUid() : null;


            DecimalFormat df = new DecimalFormat("###,###");
            this.formattedPrice = df.format(item.getOrderPrice());

            // Product 엔티티의 정보
            if (item.getProduct() != null) {
                this.productId = item.getProduct().getProductId();
                this.productName = item.getProduct().getProductName();
                this.categoryId = item.getProduct().getCategoryId();
                this.image = item.getProduct().getFile190(); // 이미지 URL 설정
                this.reviewContent = item.getProduct().getReviews();
                this.path = item.getProduct().getSavedPath();

            }

            // Seller와 관련된 정보
            if (seller != null) {
                this.seller = seller;  // Seller 객체 설정
                this.company = seller.getCompany();  // 회사명 가져오기
            }

            // Order와 관련된 정보
            if (order != null) {
                this.orderDate = String.valueOf(order.getOrderDate());  // OrderDTO 객체 설정
                this.orderId = order.getOrderId();  // orderId 설정
                this.hp = order.getHp();
                this.addr1 = order.getAddr1();
                this.addr2 = order.getAddr2();
                this.shippingInfo = order.getShippingInfo();

                this.orderStatus = order.getOrderStatus();
                this.pay = order.getPay();

            }
        }

//        public OrderItemDTO(OrderItem item ,Seller seller, OrderDTO order) {
//            this.orderItemId = item.getOrderItemId();
//            this.savedPrice = item.getSavedPrice();
//            this.orderPrice = item.getOrderPrice();
//            this.savedDiscount = item.getSavedDiscount();
//            this.point = item.getPoint();
//            this.sellerUid = item.getSellerUid();
//            this.optionDesc = item.getOptionDesc();
//            this.optionId = item.getOptionId();
//            this.combination = item.getCombination();
//            this.combinationId = item.getCombinationId();
//            this.stock = item.getStock();
//            this.price = item.getPrice();
//            this.traceNumber = item.getTraceNumber();
//            this.shippingFees = item.getShippingFees();
//            this.status = item.getStatus();
//            DecimalFormat df = new DecimalFormat("###,###");
//            this.formattedPrice = df.format(item.getOrderPrice());
//
//            // Product 엔티티의 정보
//            if (item.getProduct() != null) {
//                this.productId = item.getProduct().getProductId();
//                this.productName = item.getProduct().getProductName();
//                this.categoryId = item.getProduct().getCategoryId();
//                this.image = item.getProduct().getFile190(); // 이미지 URL 설정
//            }
//
//            // Seller와 관련된 정보
//            if (seller != null) {
//                this.seller = seller;  // Seller 객체 설정
//                this.company = seller.getCompany();  // 회사명 가져오기
//            }
//            if (order != null) {
//                this.order = order;  // OrderDTO 객체 설정
//                this.orderId = order.getOrderId();  // orderId 설정
//            }
//        }
    }
