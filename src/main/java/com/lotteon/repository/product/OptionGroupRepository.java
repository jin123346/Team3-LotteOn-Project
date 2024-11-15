package com.lotteon.repository.product;

import com.lotteon.entity.product.OptionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionGroupRepository extends JpaRepository<OptionGroup, Integer> {
}
