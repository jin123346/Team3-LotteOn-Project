package com.lotteon.repository.product;


import com.lotteon.entity.product.ProductFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductFileRepository extends JpaRepository<ProductFile, Long> {
}
