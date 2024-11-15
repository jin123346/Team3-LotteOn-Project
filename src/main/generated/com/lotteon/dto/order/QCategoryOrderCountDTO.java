package com.lotteon.dto.order;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.lotteon.dto.order.QCategoryOrderCountDTO is a Querydsl Projection type for CategoryOrderCountDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QCategoryOrderCountDTO extends ConstructorExpression<CategoryOrderCountDTO> {

    private static final long serialVersionUID = -1870930281L;

    public QCategoryOrderCountDTO(com.querydsl.core.types.Expression<Long> categoryFirstId, com.querydsl.core.types.Expression<Long> orderCount) {
        super(CategoryOrderCountDTO.class, new Class<?>[]{long.class, long.class}, categoryFirstId, orderCount);
    }

}

