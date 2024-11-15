package com.lotteon.dto.product;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class OptionItemDTO {

    private Long item_id;
    private Long group_id;   // 옵션그룹 Id
    private String optionName;   // 예: 빨강 파랑 과 연결
    private Long additionalPrice;   //추가금액
    private String itemName;
    private boolean isDeleted;

    //추가필드
    private  String combinationString;
    private long combinationId;


}
