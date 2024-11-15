package com.lotteon.entity.product;

import com.lotteon.dto.product.ProductDetailsDTO;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name="productDetails")
public class ProductDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int detailId;
    private String Productcondition;
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

    public ProductDetailsDTO toDTO(){
        return ProductDetailsDTO.builder()
                .detailId(detailId)
                .afterSalseManager(afterSalseManager)
                .phoneNumber(phoneNumber)
                .manufactureImporter(manufactureImporter)
                .manufactureCountry(manufactureCountry)
                .handlingPrecautions(handlingPrecautions)
                .busniesstype(busniesstype)
                .condition(Productcondition)
                .tax(tax)
                .receiptIssuance(receiptIssuance)
                .qualityWarranty(qualityWarranty)
                .shippingType(shippingType)
                .build();
    }


}
