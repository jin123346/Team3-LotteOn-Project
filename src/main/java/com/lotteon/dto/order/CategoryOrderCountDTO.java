package com.lotteon.dto.order;


import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class CategoryOrderCountDTO {

    private Long categoryFirstId;
    private Long orderCount;

    @QueryProjection
    public CategoryOrderCountDTO(Long categoryFirstId, Long orderCount) {
        this.categoryFirstId = categoryFirstId;
        this.orderCount = orderCount;
    }

    // getters and setters
}
