package com.lotteon.repository.Impl;

import com.lotteon.dto.order.CartOrderRequestDTO;
import com.lotteon.dto.order.CategoryOrderCountDTO;
import com.lotteon.dto.page.PageRequestDTO;
import com.lotteon.entity.order.OrderItem;
import com.lotteon.entity.order.QOrder;
import com.lotteon.entity.order.QOrderItem;
import com.lotteon.entity.product.QProduct;
import com.lotteon.repository.custom.OrderItemRepositoryCustom;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
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
public class OrderItemRepositoryImpl implements OrderItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private QOrderItem orderItem = QOrderItem.orderItem;
    private QProduct qProduct = QProduct.product;
    private QOrder qOrder = QOrder.order;

    @Override
    public Page<OrderItem> selectOrderSearchForList(PageRequestDTO pageRequestDTO, Pageable pageable) {

        String type = pageRequestDTO.getType();
        String keyword = pageRequestDTO.getKeyword();
        log.info("typetype : " + type);

        // 검색 선택 조건에 따라 where 조건 표현식 생성
        BooleanExpression expression = null;

        if (type.equals("orderNumber")) {
            expression = orderItem.orderItemId.stringValue().contains(keyword); // title like '%keyword'을 의미
            log.info("expression : " + expression);

        } else if (type.equals("orderUid")) {
            expression = orderItem.order.uid.contains(keyword);

        } else if (type.equals("orderName")) {
            expression = orderItem.order.memberName.contains(keyword);
        }

        List<OrderItem> content = queryFactory
                .select(orderItem)
                .from(orderItem)
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()) //부터 10개까지(size 만큼까지)
                .orderBy(orderItem.orderItemId.desc()) // 내림차순으로 정렬
                .fetch();

        //총 글 갯수
        long total = queryFactory
                .select(orderItem.count())
                .from(orderItem)
                .where(expression)
                .fetchOne();

        //페이징 처리를 위해 page 객체 리턴 , 페이지 형태로 데이터를 반환 , 1. 한페이지에 보여지는 리스트,모든 데이터,pg-1,size,no내림차순
        return new PageImpl<OrderItem>(content, pageable, total); //pageable : 요청한 페이지의 정보 ( 개수, 크기, 번호 정렬 방식을 위해
    }

    @Override
    public List<CategoryOrderCountDTO> selectCount() {

        return queryFactory
                .select(Projections.fields(
                        CategoryOrderCountDTO.class,
                        qProduct.categoryFirstId.as("categoryFirstId"), // Map field to DTO property name
                        Expressions.asNumber(orderItem.count()).as("orderCount") // Count as "orderCount"
                ))
                .from(orderItem)
                .join(orderItem.product, qProduct)
                .groupBy(qProduct.categoryFirstId)
                .fetch();
    }

    @Override
    public List<CategoryOrderCountDTO> selectCountByCancelGroupCategory() {
        return queryFactory
                .select(Projections.fields(
                        CategoryOrderCountDTO.class,
                        qProduct.categoryFirstId.as("categoryFirstId"), // Map field to DTO property name
                        Expressions.asNumber(orderItem.count()).as("orderCount") // Count as "orderCount"
                ))
                .from(orderItem)
                .join(orderItem.product, qProduct)
                .join(orderItem.order, qOrder) // Join with Order
                .where(qOrder.orderStatus.eq("canceled")) // Filter for completed orders
                .groupBy(qProduct.categoryFirstId)
                .fetch();


    }

    @Override
    public List<CategoryOrderCountDTO> selectCountByCompletedGroupCategory() {
        return queryFactory
                .select(Projections.fields(
                        CategoryOrderCountDTO.class,
                        qProduct.categoryFirstId.as("categoryFirstId"), // Map field to DTO property name
                        Expressions.asNumber(orderItem.count()).as("orderCount") // Count as "orderCount"
                ))
                .from(orderItem)
                .join(orderItem.product, qProduct)
                .join(orderItem.order, qOrder) // Join with Order
                .where(qOrder.orderStatus.eq("completed")) // Filter for completed orders
                .groupBy(qProduct.categoryFirstId)
                .fetch();

    }


}
