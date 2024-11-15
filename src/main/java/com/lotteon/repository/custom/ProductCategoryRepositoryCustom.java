package com.lotteon.repository.custom;

/*
    날짜 : 2024.10.20
    이름 : 하진희
    내용 : product category Custom
    =========================

 */

import com.lotteon.entity.product.ProductCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ProductCategoryRepositoryCustom {
    public List<ProductCategory> SelectParentCategories(Long id);

}
