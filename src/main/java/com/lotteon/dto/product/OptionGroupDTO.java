package com.lotteon.dto.product;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class OptionGroupDTO {

    private Long optionGroupId;
    private Long  productId;
    private String name; //옵션 그룹명 (예: 색상, 사이즈)
    private boolean isRequired;  // 필수 여부

//    @JsonManagedReference("product-options")
//    private ProductDTO product;


    private String groupName;
    private boolean isDeleted;

    // JSON 데이터의 문자열 배열을 OptionDTO로 변환하기 위한 필드
    private List<String> items;
    private List<OptionItemDTO> optionItems;



}
