package com.lotteon.entity.product;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lotteon.dto.product.OptionGroupDTO;
import com.lotteon.dto.product.OptionItemDTO;
import com.lotteon.entity.product.test.OptionTest;
import com.lotteon.entity.product.test.ProductTest;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name="optionGroup")
public class OptionGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionGroupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String groupName;
    private String name;
    private boolean isRequired;



    // Soft delete 플래그 추가
    @Builder.Default
    private boolean isDeleted = false;

    @ToString.Exclude
    @JsonManagedReference // 순환 참조 방지를 위해 사용
    @OneToMany(mappedBy = "optionGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OptionItem> optionItems;
    // In OptionGroup
    public OptionGroupDTO toDTO() {
        // Map OptionItems to OptionItemDTOs
        List<OptionItemDTO> optionItemDTOs = this.optionItems.stream()
                .map(OptionItem::toDTO)  // Assuming OptionItem has a toDTO() method
                .collect(Collectors.toList());

        return OptionGroupDTO.builder()
                .optionGroupId(this.optionGroupId)
                .groupName(this.groupName)
                .name(this.name)
                .isRequired(this.isRequired)
                .optionItems(optionItemDTOs)  // Add mapped OptionItemDTOs
                .build();

    }
}
