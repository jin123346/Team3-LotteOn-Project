package com.lotteon.dto.order;

import com.lotteon.dto.product.request.BuyNowRequestDTO;
import com.lotteon.entity.User.Member;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {


  private List<BuyNowRequestDTO> productDataArray; // Mapping to List<BuyNowRequestDTO>
  private String receiver;
  private String hp;
  private String postcode;
  private String addr1;
  private String addr2;
  private String totalDiscount;             // Total discount amount
  private String productDiscount;           // Product discount amount
  private int totalPointandCoupon;          // Total amount of points and coupon applied
  private int usedPointResult;              // Amount of points used
  private int usedCouponResult;             // Amount of coupon used
  private String usedCouponName;            // Name of the coupon used
  private String totalOrderQuantity;        // Total quantity of items ordered
  private String totalShippingFee;          // Total shipping fee
  private String totalFinalPrice;           // Final total price of the order
  private String totalOriginalPrice;           // Final total price of the order
  private String credit;                    // Payment method
  private String couponId;                     // ID of the coupon used
  private String shippingInfo;              // Shipping information or notes
  private String finalOrderPoint;
  private long point;   // totalpoint(적립예정금액)
  private int gradePercentage;
  private String memberName;
  private String memberHp;






}
