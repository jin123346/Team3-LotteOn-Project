package com.lotteon.repository.Impl;

import com.lotteon.entity.product.ProductCategory;
import com.lotteon.entity.product.QProductCategory;
import com.lotteon.repository.custom.ProductCategoryRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/*
    날짜 : 2024.10.20
    이름 : 하진희
    내용 : product category Custom
    =========================

 */

@Log4j2
@RequiredArgsConstructor
@Repository
public class ProductCategoryRepositoryImpl implements ProductCategoryRepositoryCustom {



    private final JPAQueryFactory queryFactory;

    @Override
    public List<ProductCategory> SelectParentCategories(Long id) {
        QProductCategory category = QProductCategory.productCategory;

        // 3차 카테고리부터 상위 부모를 추적하여 리스트에 담기

        //자신의 현재 카테고리 정보 가져오기
        ProductCategory currentCategory = queryFactory
                .selectFrom(category)
                .leftJoin(category.parent).fetchJoin()  // fetch join으로 parent를 즉시 로딩
                .where(category.id.eq(id))
                .fetchOne();

        List<ProductCategory> parentCategories = new ArrayList<>();
        parentCategories.add(currentCategory);
        while (currentCategory != null && currentCategory.getParent() != null) {
            currentCategory = currentCategory.getParent();
            parentCategories.add(currentCategory);
        }

        return parentCategories;
    }
}
