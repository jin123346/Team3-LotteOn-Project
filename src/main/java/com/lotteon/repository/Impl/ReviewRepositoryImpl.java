package com.lotteon.repository.Impl;

import com.lotteon.dto.admin.PageRequestDTO;
import com.lotteon.entity.product.QReview;
import com.lotteon.entity.product.Review;
import com.lotteon.repository.custom.ReviewRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Repository
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private QReview qReview = QReview.review;

    @Override
    public Page<Review> selectReviewAllForList(PageRequestDTO pageRequestDTO, Pageable pageable) {
        log.info("Fetching reviews...");

        String productIdStr = pageRequestDTO.getType(); // type을 productId로 사용
        BooleanExpression expression = null;

        if (productIdStr != null) {
            Long productId = Long.parseLong(productIdStr);
            expression = qReview.product.productId.eq(productId); // productId 조건 추가
        }

        List<Review> content = queryFactory
                .selectFrom(qReview)
                .join(qReview.product).fetchJoin()
                .leftJoin(qReview.pReviewFiles).fetchJoin() // 변경: leftJoin 사용
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qReview.rdate.desc())
                .fetch();

        log.info("Fetched content size: " + content.size());

        long total = queryFactory
                .select(qReview.count())
                .from(qReview)
                .where(expression)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
