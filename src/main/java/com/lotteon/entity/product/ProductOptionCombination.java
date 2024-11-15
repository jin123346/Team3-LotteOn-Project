package com.lotteon.entity.product;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lotteon.dto.product.ProductOptionCombinationDTO;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
@Entity
@Table(name="productOptionCombination")
public class ProductOptionCombination {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long combinationId;

    private String combination;
    private Long stock;
    private String optionCode;
    private Long additionalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    // Soft delete 플래그 추가
    @Builder.Default
    private boolean isDeleted = false;


    public ProductOptionCombinationDTO toDTO() {
        System.out.println("여기바로 그지점");
        
        return ProductOptionCombinationDTO.builder()
                .combinationId(this.combinationId)
                .combination(this.combination)
                .stock(this.stock)
                .optionCode(this.optionCode)
                .additionalPrice(this.additionalPrice)
                .build();
    }




}
