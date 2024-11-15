package com.lotteon.repository.custom;

import com.lotteon.dto.admin.PageRequestDTO;
import com.lotteon.entity.product.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {

    public Page<Review> selectReviewAllForList(PageRequestDTO pageRequestDTO, Pageable pageable);
}
