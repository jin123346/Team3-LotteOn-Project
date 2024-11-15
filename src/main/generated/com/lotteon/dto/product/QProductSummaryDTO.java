package com.lotteon.dto.product;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.lotteon.dto.product.QProductSummaryDTO is a Querydsl Projection type for ProductSummaryDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QProductSummaryDTO extends ConstructorExpression<ProductSummaryDTO> {

    private static final long serialVersionUID = -960992512L;

    public QProductSummaryDTO(com.querydsl.core.types.Expression<Long> categoryId, com.querydsl.core.types.Expression<Long> productId, com.querydsl.core.types.Expression<String> productName, com.querydsl.core.types.Expression<Long> price, com.querydsl.core.types.Expression<Integer> discount, com.querydsl.core.types.Expression<Integer> shippingFee, com.querydsl.core.types.Expression<Integer> shippingTerms, com.querydsl.core.types.Expression<String> productDesc, com.querydsl.core.types.Expression<String> file230, com.querydsl.core.types.Expression<String> file190, com.querydsl.core.types.Expression<Long> sellerId, com.querydsl.core.types.Expression<String> sellerName, com.querydsl.core.types.Expression<String> company, com.querydsl.core.types.Expression<String> savedPath) {
        super(ProductSummaryDTO.class, new Class<?>[]{long.class, long.class, String.class, long.class, int.class, int.class, int.class, String.class, String.class, String.class, long.class, String.class, String.class, String.class}, categoryId, productId, productName, price, discount, shippingFee, shippingTerms, productDesc, file230, file190, sellerId, sellerName, company, savedPath);
    }

}

