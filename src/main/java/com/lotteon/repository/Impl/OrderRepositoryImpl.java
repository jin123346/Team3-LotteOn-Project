package com.lotteon.repository.Impl;

import com.lotteon.dto.page.PageRequestDTO;
import com.lotteon.entity.order.QOrder;
import com.lotteon.repository.custom.OrderRepositoryCustom;
import com.querydsl.core.Tuple;
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
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private QOrder order = QOrder.order;

    @Override
    public Page<Tuple> selectOrderAllForList(PageRequestDTO pageRequestDTO, Pageable pageable) {

        List<Tuple> content = queryFactory
                .select(order.orderId, order.uid, order.memberName, order.pay, order.orderDate, order.totalPrice, order.addr1, order.addr2, order.memberHp, order.hp, order.receiver, order.totalDiscount, order.totalShipping, order.totalOriginalPrice, order.orderStatus)
                .from(order)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()) //부터 10개까지(size 만큼까지)
                .orderBy(order.orderId.desc()) // 내림차순으로 정렬
                .fetch();

        //총 글 갯수
        long total = queryFactory
                .select(order.count())
                .from(order)
                .fetchOne();

        //페이징 처리를 위해 page 객체 리턴 , 페이지 형태로 데이터를 반환 , 1. 한페이지에 보여지는 리스트,모든 데이터,pg-1,size,no내림차순
        return new PageImpl<Tuple>(content, pageable, total); //pageable : 요청한 페이지의 정보 ( 개수, 크기, 번호 정렬 방식을 위해
    }

    @Override
    public Page<Tuple> selectOrderSearchForList(PageRequestDTO pageRequestDTO, Pageable pageable) {

        String type = pageRequestDTO.getType();
        String keyword = pageRequestDTO.getKeyword();
        log.info("typetype : " + type);

        // 검색 선택 조건에 따라 where 조건 표현식 생성
        BooleanExpression expression = null;

        if (type.equals("orderNumber")) {
            expression = order.orderId.stringValue().contains(keyword); // title like '%keyword'을 의미
            log.info("expression : " + expression);

        } else if (type.equals("orderUid")) {
            expression = order.uid.contains(keyword);

        } else if (type.equals("orderName")) {
            expression = order.memberName.contains(keyword);
        }

        List<Tuple> content = queryFactory
                .select(order.orderId, order.uid)
                .from(order)
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()) //부터 10개까지(size 만큼까지)
                .orderBy(order.orderId.desc()) // 내림차순으로 정렬
                .fetch();

        //총 글 갯수
        long total = queryFactory
                .select(order.count())
                .from(order)
                .where(expression)
                .fetchOne();

        //페이징 처리를 위해 page 객체 리턴 , 페이지 형태로 데이터를 반환 , 1. 한페이지에 보여지는 리스트,모든 데이터,pg-1,size,no내림차순
        return new PageImpl<Tuple>(content, pageable, total); //pageable : 요청한 페이지의 정보 ( 개수, 크기, 번호 정렬 방식을 위해
    }
}
