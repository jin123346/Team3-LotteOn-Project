package com.lotteon.dto.product;

import com.lotteon.entity.product.Product;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProductDetailsDTO {
    private int detailId;
    private Long productId;
    private String condition;
    private String tax;
    private String receiptIssuance;
    private String busniesstype;
    private String manufactureImporter;
    private String manufactureCountry;
    private String handlingPrecautions;
    private String qualityWarranty;
    private String afterSalseManager;
    private String phoneNumber;
    private String shippingType;





}
