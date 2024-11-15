package com.lotteon.dto.product;


import com.lotteon.entity.product.Product;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
public class ProductOptionCombinationDTO {


    private Long combinationId;
    private String combination;
    private String optionCode;
    private Long productId; // 상품 id
    private String combination_name;
    private Long additionalPrice;
    private Long stock;
    private Long  product;
    private boolean isDeleted;


    public String getCombinationAfterColon() {
        String[] parts = combination.split(":");
        System.out.println("parts[0]" + parts[0]);
        return parts.length > 1 ? parts[0] : ""; // ":" 뒤의 값 반환
    }

    // 옵션 항목 이름으로 추가 금액을 가져오는 메서드
    public Long getAdditionalPriceForOption(String optionName, Set<ProductOptionCombinationDTO> optionCombinationDto) {
        return optionCombinationDto.stream()
                .filter(comb -> comb.getCombination().contains(optionName) && comb.getAdditionalPrice() > 0)
                .findFirst()
                .map(ProductOptionCombinationDTO::getAdditionalPrice)
                .orElse(0L);
    }
}
