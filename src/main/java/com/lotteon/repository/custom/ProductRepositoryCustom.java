package com.lotteon.repository.custom;

/*
    날짜 : 2024.10.20
    이름 : 하진희
    내용 : product jpql custom
    =========================
    추가내용
    2024.10.26 하진희 - product view query custom
 */

import com.lotteon.dto.order.CategoryOrderCountDTO;
import com.lotteon.dto.product.PageRequestDTO;
import com.lotteon.dto.product.ProductDTO;
import com.lotteon.dto.product.ProductListDTO;
import com.lotteon.dto.product.ProductSummaryDTO;
import com.lotteon.dto.product.request.ProductViewResponseDTO;
import com.lotteon.entity.product.Product;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface ProductRepositoryCustom {
    //admin List
    public Page<Product> selectProductBySellerIdForList(String sellerId, PageRequestDTO pageRequest, Pageable pageable );

    //main list
    public Page<Tuple> selectProductForList( PageRequestDTO pageRequest,Pageable pageable );
    public Page<ProductSummaryDTO> selectProductByCategory(PageRequestDTO pageRequest, Pageable pageable,String sort);
    public Page<ProductSummaryDTO> getSearchByProductNameOrderBySort(PageRequestDTO pageRequest,String sort);
    public Page<ProductSummaryDTO> searchWithConditions(PageRequestDTO pageRequest, BooleanBuilder conditions,String sort);
    public ProductViewResponseDTO selectByProductId(Long productId);





    //main view


}
