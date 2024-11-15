package com.lotteon.entity.product;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lotteon.dto.product.*;
import com.lotteon.entity.User.Seller;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Setter
@Table(name="product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String productCode;
    private Long categoryId;
    private Long categoryFirstId;
    private Long categorySecondId;

    private String productName;
    private Long price;
    private Long stock;
    private int discount;
    private int shippingFee;
    private int shippingTerms; //무료배송 조건

    @CreationTimestamp
    private LocalDateTime rdate;
    private String productDesc; //상품설명

    @Builder.Default
    private int point=0;

    @Builder.Default
    private Boolean isCoupon =true;
    // 쿠폰 사용가능 유므
    @Builder.Default
    private Boolean isSaled = true; // 판매가능여부

    private Long sellerNo;
    private String sellerId;

    @Builder.Default
    private boolean isDeleted=false;

    @Builder.Default
    private Long sold=0L; //판매량
    @Builder.Default
    private Long hit=0L; //보는수

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @ToString.Exclude  // 외래키는 자식 테이블에 생성
    @BatchSize(size = 10)
    private Set<ProductFile> files;


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="detail_id")
    @ToString.Exclude
    private ProductDetails productDetails;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    @ToString.Exclude
    @JsonManagedReference // 순환 참조 방지를 위해 사용
    private Set<OptionGroup> optionGroups;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @BatchSize(size = 30)
    private Set<ProductOptionCombination> optionCombinations;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    @ToString.Exclude
    private List<Option> options;

    private String file190;
    private String file230;
    private String file456;
    private String savedPath;


    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @Fetch(FetchMode.SUBSELECT)
    @JsonManagedReference // 순환 참조 방지를 위해 사용
    private List<Review> reviews = new ArrayList<>();

    @Transient
    private List<String> fileDescs = new ArrayList<>();
  
    //리뷰 별 평균 값
    private double productRating;



    @PostPersist
    public void generateProductCode(){
        this.productCode = "C"+categoryId+"-P"+productId;
    }

    // `setFiles` 메서드
    public void setFiles(Set<ProductFile> files) {
        this.files = files;
        if (files != null) {
            if (this.fileDescs == null) {
                this.fileDescs = new ArrayList<>();
            }
            for (ProductFile file : files) {
                this.savedPath = file.getPath();
                switch (file.getType()) {
                    case "190" -> this.file190 = file.getSName();
                    case "230" -> this.file230 = file.getSName();
                    case "456" -> this.file456 = file.getSName();
                    default -> this.fileDescs.add(file.getSName());
                }
            }
        }
    }
    public void setHit(){
        this.hit++;
    }



    public void setOptionGroups(Set<OptionGroup> optionGroups) {
        this.optionGroups = optionGroups;
    }
    public void setOptions(List<Option> options) {
        this.options=options;
    }

    public void setSavedPath(String savedPath){
        this.savedPath = savedPath;
    }

    public void setOptionCombinations(Set<ProductOptionCombination> optionCombinations) {
        this.optionCombinations = optionCombinations;
    }

    public void setProductDetails(ProductDetails productDetails) {
        this.productDetails=productDetails;
    }


    public ProductDTO toDTO(Product product) {
        // Convert ProductDetails to DTO if not null
//        ProductDetailsDTO dto = product.getProductDetails() != null ? product.getProductDetails().toDTO() : null;

//        // Map OptionGroups to OptionGroupDTOs
//        List<OptionGroupDTO> optionGroupDTOs = product.getOptionGroups().stream()
//                .map(OptionGroup::toDTO) // Assuming OptionGroup has a toDTO() method
//                .collect(Collectors.toList());
//
//        // Map OptionCombinations to OptionCombinationDTOs
//        List<ProductOptionCombinationDTO> optionCombinationDTOs = product.getOptionCombinations().stream()
//                .map(ProductOptionCombination::toDTO) // Assuming OptionCombination has a toDTO() method
//                .collect(Collectors.toList());

        return ProductDTO.builder()
                .file190(this.file190)
                .file230(this.file230)
                .file456(this.file456)
                .productId(this.getProductId())
                .productRating(this.productRating)
                .productCode(this.productCode)
                .productName(this.productName)
                .hit(this.hit)
                .point(this.point)
                .discount(this.discount)
                .sellerId(this.sellerId)
                .sellerNo(this.sellerNo)
                .shippingFee(this.shippingFee)
                .shippingTerms(this.shippingTerms)
                .rdate(String.valueOf(this.rdate))
                .categoryFirstId(this.categoryFirstId)
                .categorySecondId(this.categorySecondId)
                .categoryId(this.categoryId)
                .filedesc(this.fileDescs)
                .price(this.price)
                .stock(this.stock)
                .productDesc(this.productDesc)
                .isSaled(this.isSaled)
                .isCoupon(this.isCoupon)
                .sold(this.sold)
                .productDetails(null)
                .optionGroups(null)
                .optionCombinations(null)
                .savedPath(this.savedPath)

                // 추가: ProductDetails가 null이 아닐 때 DTO로 변환
                .productDetails(product.getProductDetails() != null ? this.productDetails.toDTO() : null)
                .build();
    }



}
