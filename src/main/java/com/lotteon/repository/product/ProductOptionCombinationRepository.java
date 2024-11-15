package com.lotteon.repository.product;

import com.lotteon.entity.product.ProductOptionCombination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProductOptionCombinationRepository extends JpaRepository<ProductOptionCombination, Long> {
    @Modifying
    @Query("UPDATE ProductOptionCombination p SET p.stock = :stock WHERE p.combinationId = :combinationId")
    void updateQuantity(@Param("stock") long stock, @Param("combinationId") long combinationId);
}
