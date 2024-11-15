package com.lotteon.dto.order;

import com.lotteon.dto.User.SellerDTO;
import com.lotteon.dto.product.ProductDTO;
import com.lotteon.entity.User.Seller;
import com.lotteon.entity.order.OrderItem;
import com.lotteon.entity.product.Product;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@Log4j2
@NoArgsConstructor
@AllArgsConstructor
public class OrderCompletedResponseDTO {

    OrderDTO order;
    List<SellerDTO> sellers;
    List<OrderPriceDTO> orderPrices;
    private long originalTotalPrice;
    private long totalDiscount;
    private long expectedPoint;
    private long totalShippingFee;
    private long finalPaymentAmount;
    private long finalOrderPrice;




    @Builder
    public OrderCompletedResponseDTO(OrderDTO order, Set<SellerDTO> sellerDTOs,List<OrderDTO> orderDTOSs) {
        List<OrderItemDTO> orderItems = order.getOrderItems() != null ? order.getOrderItems() : new ArrayList<>();
        this.order = order;
        this.sellers = new ArrayList<>();
        this.orderPrices = new ArrayList<>();
        // Initialize totals
        this.originalTotalPrice = 0;
        this.totalDiscount = 0;
        this.totalShippingFee = 0;
        this.expectedPoint=0;
        long pointCoupon= order.getUsedCoupon()+order.getUsedPoint();
        for (SellerDTO seller : sellerDTOs) {
            log.info("Processing seller: " + seller.getUid()); // Log each seller

            long sellerTotalOriginalPrice=0;
            long sellerTotalFinalPrice = 0;
            long sellerTotalDiscount = 0;
            long sellerShippingFee = 0;
            long sellerTotalQuantity=0;
            List<OrderItemDTO> sellerOrderItems = new ArrayList<>();

            for (OrderItemDTO orderItem : orderItems) {

                ProductDTO product = orderItem.getProduct();
                long quantity = orderItem.getStock();
                long productTotalPrice = orderItem.getSavedPrice() * quantity;
                long productDiscount = orderItem.getSavedDiscount();
                long productFinalPrice = productTotalPrice-(productDiscount);

                // Calculate original price and discount for all items
                if (orderItem.getCombination() != null) {
                    orderItem.setCombination(orderItem.getCombination());
                }

                ;

                // Calculate seller-specific totals

                // Check shipping conditions

                // Add item to seller's list if seller matches
                if (seller.getUid().equals(orderItem.getSellerUid())) {
                    //할인전 상품금액
                    this.originalTotalPrice += productTotalPrice;
                    sellerTotalOriginalPrice += productTotalPrice;


                    //할인 금액
                    this.totalDiscount += productDiscount;
                    sellerTotalDiscount += productDiscount;

                    //할인 후 상품긍맥
                    this.finalOrderPrice += productFinalPrice;
                    sellerTotalFinalPrice += productFinalPrice;

                    this.expectedPoint += orderItem.getPoint();
                    this.totalShippingFee += sellerShippingFee;
                    if (product.getShippingTerms() > productFinalPrice) {
                        log.info("shippingTerms!!:"+product.getShippingTerms());
                        sellerShippingFee += product.getShippingFee();
                    }

                    log.info("Seller " + seller.getUid() + " - Total Price: " + sellerTotalFinalPrice + ", Total Discount: " + sellerTotalDiscount + ", Shipping Fee: " + sellerShippingFee);
                    sellerOrderItems.add(orderItem);
                }


            }
            seller.setTotalShipping(sellerShippingFee);
            seller.setTotalFinalPrice(sellerTotalFinalPrice);
            seller.setTotalDiscount(sellerTotalDiscount);
            seller.setTotalOriginalPrice(sellerTotalOriginalPrice);


            // Finalize shipping fee for each seller
            OrderPriceDTO orderPriceDTO  = OrderPriceDTO.builder()
                    .sellerUid(seller.getUid())
                    .totalFinalPrice(sellerTotalFinalPrice)
                    .totalDiscount(sellerTotalDiscount)
                    .totalShipping(sellerShippingFee)
                    .totalOriginalPrice(sellerTotalOriginalPrice)
                    .build();
            this.orderPrices.add(orderPriceDTO);


            seller.setOrderItems(sellerOrderItems);
            this.sellers.add(seller);
            log.info("Seller Order Items: " + seller.getOrderItems());
        }

        // Calculate final payment amount
        this.finalPaymentAmount = this.originalTotalPrice - this.totalDiscount + this.totalShippingFee -pointCoupon;
        log.info("Original Total Price: " + originalTotalPrice);
        log.info("Total Discount: " + totalDiscount);
        log.info("Total Shipping Fee: " + totalShippingFee);
        log.info("Final Payment Amount: " + finalPaymentAmount);


    }



}
