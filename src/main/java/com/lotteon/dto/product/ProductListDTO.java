package com.lotteon.dto.product;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProductListDTO {

    private Long productId;
    private Long categoryId;
    private String productName;
    private Long price;
    private Long stock;
    private int discount;
    private int shippingFee;
    private int shippingTerms; //무료배송 조건
    private String rdate;
    private String productDesc; //상품설명
    private int point;
    private Boolean isCoupon; // 쿠폰 사용가능 유므
    private Boolean isSaled; // 판매가능여부
    private String sellerId;
    private String productCode;
    private int hit;
    private List<ProductFileDTO> productFiles;
    private List<OptionDTO> options;
    private ProductDetailsDTO productDetails;
    //추가필드
    private String file190;
    private String file230;
    private String file456;
    private List<String> filedesc;

    // 생성자
    public ProductListDTO(Long productId, Long categoryId, String productName, Long price,
                          Long stock, Integer discount, Integer shippingFee, Integer shippingTerms,
                          LocalDateTime rdate, String productDesc, String sellerId, String productCode,
                          Integer hit, List<ProductFileDTO> productFiles) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.discount = discount;
        this.shippingFee = shippingFee;
        this.shippingTerms = shippingTerms;
        this.rdate = String.valueOf(rdate);
        this.productDesc = productDesc;
        this.sellerId = sellerId;
        this.productCode = productCode;
        this.hit = hit;
        this.productFiles = productFiles;
    }


}
//
//    // Ensure the constructor matches the parameters expected by QueryDSL
//    public ProductListDTO(Long productId, Long categoryId, String productName,
//                          Integer price, Integer stock, Integer discount,
//                          Integer shippingFee, Integer shippingTerms,
//                          LocalDateTime rdate, String productDesc,
//                          String file190, String file230, String file456,
//                          Integer hit, List<ProductFileDTO> productFiles) {
//        this.productId = productId;
//        this.categoryId = categoryId;
//        this.productName = productName;
//        this.price = price;
//        this.stock = stock;
//        this.discount = discount;
//        this.shippingFee = shippingFee;
//        this.shippingTerms = shippingTerms;
//        this.rdate =  String.valueOf(rdate);
//        this.ProductDesc = productDesc;
//        this.hit = hit;
//        this.productFiles = productFiles;
//        this.filedesc = new ArrayList<>();  // 리스트 초기화
//        for(ProductFileDTO file : productFiles) {
//            if(file.getType().equals("190")){
//                this.file190= file.getSName();
//            }else if(file.getType().equals("230")){
//                this.file230= file.getSName();
//            }else if(file.getType().equals("456")){
//                this.file456= file.getSName();
//            }else if(file.getType().equals("940")){
//                this.filedesc.add(file.getSName());
//            }
//        }




