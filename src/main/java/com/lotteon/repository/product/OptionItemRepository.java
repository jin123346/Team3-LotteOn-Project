package com.lotteon.repository.product;

import com.lotteon.entity.product.OptionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionItemRepository extends JpaRepository<OptionItem, Integer> {
}
