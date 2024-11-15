package com.lotteon.repository;

import com.lotteon.entity.product.Review;
import com.lotteon.repository.custom.ReviewRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> , ReviewRepositoryCustom {

    List<Review> findTop3ByWriter_UidOrderByRdateDesc(String uid);
    int countByProduct_ProductId(Long productId);
    List<Review> findAllByProduct_ProductId(Long productId);

    Page<Review> findByWriter_Uid(String uid, Pageable pageable);

}
