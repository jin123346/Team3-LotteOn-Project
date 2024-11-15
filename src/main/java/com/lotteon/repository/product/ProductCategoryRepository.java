package com.lotteon.repository.product;


import com.lotteon.entity.product.ProductCategory;
import com.lotteon.repository.custom.ProductCategoryRepositoryCustom;
import com.lotteon.repository.custom.ProductRepositoryCustom;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> , ProductCategoryRepositoryCustom {

    public List<ProductCategory> findByLevel(int level);
    public List<ProductCategory> findByParentId(Long parentId);

    // Fetch all 2nd-level categories directly under the specified 1st-level category
    @Query("SELECT c.id FROM ProductCategory c WHERE c.parent.id = :parentId")
    List<Long> findSecondLevelCategoryIds(@Param("parentId") Long parentId);

    // Fetch all 3rd-level categories directly under the specified list of 2nd-level categories
    @Query("SELECT c.id FROM ProductCategory c WHERE c.parent.id IN :secondLevelIds")
    List<Long> findThirdLevelCategoryIds(@Param("secondLevelIds") List<Long> secondLevelIds);

}
