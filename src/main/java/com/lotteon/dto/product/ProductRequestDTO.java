package com.lotteon.dto.product;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lotteon.entity.product.OptionGroup;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductRequestDTO{

    private String firstLevelCategory;
    private String secondLevelCategory;
    private String thirdLevelCategory;
    private String productName;
    private String productDesc;
    private String madeIn;
    private String sellerId;
    private Long price;
    private int discount;
    private Long stock;
    private int shippingFee;
    private int shippingTerms;
    private String condition;
    private String tax;
    private String receiptIssuance;
    private String busniesstype;
    private String manufactureCountry;

    private List<OptionGroupDTO> options;
    private List<ProductOptionCombinationDTO> combinations;

//    @Data
//    public static class OptionGroupDTO {
//        private long optionGroupId;
//        private Long  productId;
//        private String name;
//        private boolean isRequired;
//        private String groupName;
//        private List<String> items;
//    }
//
//    @Data
//    public static class CombinationDTO {
//        private String combination;
//        private String optionCode;
//        private int additionalPrice;
//        private int stock;
//    }

//    private List<MultipartFile> files;



}
