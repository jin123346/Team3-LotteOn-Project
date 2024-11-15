package com.lotteon.entity.product;

import com.lotteon.dto.product.OptionItemDTO;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name="optionItem")
public class OptionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long item_id; // 변경: long -> Long

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private OptionGroup optionGroup;

    private String optionName;   // 예: 빨강 파랑 과 연결

    @Builder.Default
    private Long additionalPrice=0L;   //추가금액


    // Soft delete 플래그 추가
    @Builder.Default
    private boolean isDeleted = false;


    public OptionItemDTO toDTO() {
        return OptionItemDTO.builder()
                .item_id(this.item_id)
                .optionName(this.optionName)
                .additionalPrice(this.additionalPrice)
                .group_id(this.optionGroup.getOptionGroupId())
                // Map other fields as needed
                .build();
    }
}
