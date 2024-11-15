package com.lotteon.repository.Impl;
/*
    날짜 : 2024.10.20
    이름 : 하진희
    내용 : product CRUD
    ==================================
    추가내용 
    2024.10.26 - product List 사용 메서드 변경
 */


import com.lotteon.dto.product.*;
import com.lotteon.dto.product.request.ProductViewResponseDTO;
import com.lotteon.entity.User.QSeller;
import com.lotteon.entity.User.QUser;
import com.lotteon.entity.product.*;
import com.lotteon.repository.custom.ProductRepositoryCustom;
import com.lotteon.repository.user.SellerRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.Expressions;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.querydsl.jpa.JPAExpressions.select;


@RequiredArgsConstructor
@Log4j2
@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final SellerRepository sellerRepository;
    private QProduct qProduct = QProduct.product;
    private QOption qOption = QOption.option;
    private QProductDetails qProductDetails = QProductDetails.productDetails;
    private QUser qUser = QUser.user;
    private QProductFile qProductFile = QProductFile.productFile;
    private QSeller qSeller = QSeller.seller;
    private QReview qReview = QReview.review;
    private QueryProjection queryProjection;
    private QProductSummaryDTO qProductSummaryDTO;
    private QProductOptionCombination qProductOptionCombination=QProductOptionCombination.productOptionCombination;
    private QOptionGroup qOptionGroup = QOptionGroup.optionGroup;
    private QOptionItem qOptionItem = QOptionItem.optionItem;
    //admin product list에서 사용
    ;@Override
    public Page<Product> selectProductBySellerIdForList(String sellerId, PageRequestDTO pageRequest, Pageable pageable) {

        List<Product> products = queryFactory.select(qProduct)
                .from(qProduct)
                .where(qProduct.sellerId.eq(sellerId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(qProduct.count())
                .from(qProduct)
                .where(qProduct.sellerId.eq(sellerId))
                .fetchOne().longValue();


        return new PageImpl<>(products, pageable,total);
    }

    @Override
    public Page<Tuple> selectProductForList(PageRequestDTO pageRequest, Pageable pageable) {




        return null;
    }


    //main 3차 카테고리 선택시
    @Override
    public Page<ProductSummaryDTO> selectProductByCategory(PageRequestDTO pageRequest, Pageable pageable,String sort) {

        NumberExpression<Long> finalPrice = qProduct.price.subtract(qProduct.price.multiply(qProduct.discount).divide(100));

        // 정렬 조건 추가
        OrderSpecifier<?> orderSpecifier = switch (sort) {
            case "lowPrice" -> finalPrice.asc();
            case "highPrice" -> finalPrice.desc();
            case "rating" -> {
                NumberExpression<Double> avgRating = Expressions.numberTemplate(Double.class, "CAST({0} AS DOUBLE)", qReview.rating).avg();
                yield avgRating.desc();
            }
            case "reviews" -> qReview.count().desc(); // 리뷰 개수 내림차순
            case "recent" -> qProduct.rdate.desc();
            default -> // 판매많은순 (popularity)
                    qProduct.sold.desc(); // 판매량 내림차순
        };

        List<ProductSummaryDTO> products =  queryFactory.select(
                      new QProductSummaryDTO(
                              qProduct.categoryId,
                              qProduct.productId,
                              qProduct.productName,
                              qProduct.price,
                              qProduct.discount,
                              qProduct.shippingFee,
                              qProduct.shippingTerms,
                              qProduct.productDesc,
                              qProduct.file230,
                              qProduct.file190,
                              qSeller.id,
                              qSeller.user.uid,
                              qSeller.company,
                              qProduct.savedPath
                              ))
              .from(qProduct)
              .leftJoin(qSeller).on(qSeller.user.uid.eq(qProduct.sellerId))
              .leftJoin(qProduct.reviews, qReview)
              .where(qProduct.categoryId.eq(pageRequest.getCategoryId())
                .and(qProduct.isDeleted.isFalse())) // Add this condition
              .groupBy(qProduct.productId, qSeller.user.uid)
              .orderBy(orderSpecifier) // 정렬 조건 추가
              .offset(pageable.getOffset())
              .limit(pageable.getPageSize())
              .fetch();

        // 2. 각 상품에 대해 리뷰 리스트를 별도로 조회하여 DTO에 추가
        for (ProductSummaryDTO product : products) {
            List<Double> ratings = queryFactory.select(qReview.rating)
                    .from(qReview)
                    .where(qReview.product.productId.eq(product.getProductId())
                            .and(qProduct.isDeleted.isFalse())) // Add this condition
                    .fetch();

            // 필요한 경우 Double을 String으로 변환하여 설정
            product.setRating(ratings.stream()
                    .map(String::valueOf)  // Double을 String으로 변환
                    .collect(Collectors.toList()));  // List<String> 형태로 변환
        }


        log.info("did=dosidjflskdjfls : "+products);
        long total = queryFactory.select(qProduct.count())
                .from(qProduct)
                .where(qProduct.categoryId.eq(pageRequest.getCategoryId())
                        .and(qProduct.isDeleted.isFalse())) // Add this condition
                .fetchOne().longValue();

        log.info("totalllllllllllll:"+total);

        return new PageImpl<>(products, pageable,total);

    }

    @Override
    public Page<ProductSummaryDTO> getSearchByProductNameOrderBySort(PageRequestDTO pageRequest,String sort) {

        Pageable pageable = pageRequest.getPageable(sort,10);
        log.info("pageable!!!!"+pageable);
        String keyword= pageRequest.getKeyword();
        log.info("검색어!!!!!1"+keyword+sort);
        // Split keyword by spaces to get multiple keywords
        String[] keywords = keyword.split("\\s+");

        // Create a dynamic predicate for product name search
        BooleanBuilder keywordCondition = new BooleanBuilder();
        for (String key : keywords) {
            keywordCondition.or(qProduct.productName.containsIgnoreCase(key));
        }

        NumberExpression<Long> finalPrice =  qProduct.price.subtract(
                qProduct.price.multiply(qProduct.discount)
                        .divide(100)
                        .divide(10)
                        .multiply(10)
        );
        // 정렬 조건 추가

        // 정렬 조건 추가
        OrderSpecifier<?> orderSpecifier = switch (sort) {
            case "lowPrice" -> finalPrice.asc();
            case "highPrice" -> finalPrice.desc();
            case "rating" -> {
                NumberExpression<Double> avgRating = Expressions.numberTemplate(Double.class, "CAST({0} AS DOUBLE)", qReview.rating).avg();
                yield avgRating.desc();
            }
            case "reviews" -> qReview.count().desc(); // 리뷰 개수 내림차순
            case "recent" -> qProduct.rdate.desc();
            default -> // 판매많은순 (popularity)
                    qProduct.sold.desc(); // 판매량 내림차순
        };

        List<ProductSummaryDTO> products =  queryFactory.select(
                        new QProductSummaryDTO(
                                qProduct.categoryId,
                                qProduct.productId,
                                qProduct.productName,
                                qProduct.price,
                                qProduct.discount,
                                qProduct.shippingFee,
                                qProduct.shippingTerms,
                                qProduct.productDesc,
                                qProduct.file230,
                                qProduct.file190,
                                qSeller.id,
                                qSeller.user.uid,
                                qSeller.company,
                                qProduct.savedPath))
                .from(qProduct)
                .leftJoin(qSeller).on(qSeller.user.uid.eq(qProduct.sellerId))
                .leftJoin(qProduct.reviews, qReview)
                .where(keywordCondition
                        .and(qProduct.isDeleted.isFalse())) // Add this condition// Apply the dynamic keyword condition
                .groupBy(qProduct.productId, qSeller.user.uid)
                .orderBy(orderSpecifier) // 정렬 조건 적용
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 2. 각 상품에 대해 리뷰 리스트를 별도로 조회하여 DTO에 추가
        for (ProductSummaryDTO product : products) {
            List<Double> ratings = queryFactory.select(qReview.rating)
                    .from(qReview)
                    .where(qReview.product.productId.eq(product.getProductId())
                            .and(qProduct.isDeleted.isFalse())) // Add this condition
                    .fetch();

            // 필요한 경우 Double을 String으로 변환하여 설정
            product.setRating(ratings.stream()
                    .map(String::valueOf)  // Double을 String으로 변환
                    .collect(Collectors.toList()));  // List<String> 형태로 변환
        }


        log.info("did=dosidjflskdjfls : "+products);
        long total = queryFactory.select(qProduct.count())
                .from(qProduct)
                .where(keywordCondition)  // Apply the dynamic keyword condition
                .fetchOne().longValue();

        log.info("totalllllllllllll:"+total);

        return new PageImpl<>(products, pageable,total);
    }

    @Override
    public Page<ProductSummaryDTO> searchWithConditions(PageRequestDTO pageRequest, BooleanBuilder conditions,String sort) {
        Pageable pageable = pageRequest.getPageable(sort, 10);
        String keyword = pageRequest.getKeyword();

        // Split keyword by spaces to get multiple keywords
        String[] keywords = keyword.split("\\s+");

        // Create a dynamic predicate for product name search
        BooleanBuilder keywordCondition = new BooleanBuilder();
        for (String key : keywords) {
            keywordCondition.or(qProduct.productName.containsIgnoreCase(key));
        }
        String searchMode =pageRequest.getSearchMode();
        switch (searchMode) {
            case "exact":
                keywordCondition.and(qProduct.productName.eq(keyword));
                break;
            case "all":
                for (String key : keywords) {
                    keywordCondition.and(qProduct.productName.containsIgnoreCase(key));
                }
                break;
            case "any":
                for (String key : keywords) {
                    keywordCondition.or(qProduct.productName.containsIgnoreCase(key));
                }
                break;
        }

        conditions.and(keywordCondition);

        NumberExpression<Long> finalPrice = qProduct.price.subtract(
                qProduct.price.multiply(qProduct.discount)
                        .divide(100)
                        .divide(10)
                        .multiply(10)
        );

        // 정렬 조건 추가
        OrderSpecifier<?> orderSpecifier = switch (sort) {
            case "lowPrice" -> finalPrice.asc();
            case "highPrice" -> finalPrice.desc();
            case "rating" -> {
                NumberExpression<Double> avgRating = Expressions.numberTemplate(Double.class, "CAST({0} AS DOUBLE)", qReview.rating).avg();
                yield avgRating.desc();
            }
            case "reviews" -> qReview.count().desc();
            case "recent" -> qProduct.rdate.desc();
            default -> qProduct.sold.desc();
        };

        List<ProductSummaryDTO> products = queryFactory.select(
                        new QProductSummaryDTO(
                                qProduct.categoryId,
                                qProduct.productId,
                                qProduct.productName,
                                qProduct.price,
                                qProduct.discount,
                                qProduct.shippingFee,
                                qProduct.shippingTerms,
                                qProduct.productDesc,
                                qProduct.file230,
                                qProduct.file190,
                                qSeller.id,
                                qSeller.user.uid,
                                qSeller.company,
                                qProduct.savedPath))
                .from(qProduct)
                .leftJoin(qSeller).on(qSeller.user.uid.eq(qProduct.sellerId))
                .leftJoin(qProduct.reviews, qReview)
                .where(conditions
                        .and(qProduct.isDeleted.isFalse())) // Add this condition
                .groupBy(qProduct.productId, qSeller.user.uid)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 각 상품에 대해 리뷰 리스트를 별도로 조회하여 DTO에 추가
        for (ProductSummaryDTO product : products) {
            List<Double> ratings = queryFactory.select(qReview.rating)
                    .from(qReview)
                    .where(qReview.product.productId.eq(product.getProductId())
                            .and(qProduct.isDeleted.isFalse())) // Add this condition
                    .fetch();

            // Double을 String으로 변환하여 설정
            product.setRating(ratings.stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList()));
        }

        // 총 데이터 개수 조회
        long total = queryFactory.select(qProduct.count())
                .from(qProduct)
                .where(keywordCondition
                        .and(qProduct.isDeleted.isFalse())) // Add this condition
                .fetchOne();

        return new PageImpl<>(products, pageable, total);
    }

    @Override
    public ProductViewResponseDTO selectByProductId(Long productId) {
        log.info("Attempting to fetch product with ID: " + productId);
        // 필수 정보만 fetch join
        Product product = queryFactory.selectFrom(qProduct)
                .leftJoin(qProduct.productDetails, qProductDetails).fetchJoin()
                .leftJoin(qSeller).on(qSeller.user.uid.eq(qProduct.sellerId)).fetchJoin()
                .where(qProduct.productId.eq(productId)
                        .and(qProduct.isDeleted.isFalse())) // Add this condition
                .fetchOne();

        if (product == null) {
            log.warn("Product not found for ID: " + productId);
            return ProductViewResponseDTO.builder()
                    .build();
        }

        log.info("Product loaded successfully: " + product);

        // 필요할 때 각 컬렉션 로드
        Set<ProductFile> files = product.getFiles();  // 지연 로딩으로 별도 쿼리 발생
        Set<ProductOptionCombination> optionCombinations = product.getOptionCombinations();
        Set<OptionGroup> optionGroups = product.getOptionGroups();
        List<Review> reviews = product.getReviews();

        log.info("Product loaded successfully: " + product);

        log.info("producttttttttttttt"+product);



        return  ProductViewResponseDTO.builder()
                .files(files)
                .optionCombinations(optionCombinations)
                .optionGroups(optionGroups)
                .reviews(reviews)
                .build();
    }


}
