package com.lotteon.dto.product;

import lombok.*;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
public class CategoryResponseDTO {

    private String firstCategoryName;
    private String secondCategoryName;
    private String thirdCategoryName;


}
