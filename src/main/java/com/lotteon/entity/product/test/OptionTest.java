package com.lotteon.entity.product.test;


import com.lotteon.entity.product.OptionGroup;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
public class OptionTest {

    private long optionId;
    private long parent_id;    //부모상품
    private String optionName;     //프로덕트 서브네임
    private String optionDesc;
    private long optionStock;
    private String OptionCode;
    private String parentCode;

    private OptionGroup optionGroup;
    private String name;
    private int additionalPrice;


}
