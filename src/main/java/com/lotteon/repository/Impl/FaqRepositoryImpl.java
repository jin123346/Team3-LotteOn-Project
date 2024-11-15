package com.lotteon.repository.Impl;

import com.lotteon.dto.page.PageRequestDTO;
import com.lotteon.entity.Faq;
import com.lotteon.entity.QFaq;
import com.lotteon.repository.custom.FaqRepositoryCustom;
import com.querydsl.core.QueryFactory;
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
public class FaqRepositoryImpl implements FaqRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private QFaq faq = QFaq.faq;


    @Override
    public Page<Tuple> selectFaqAllForList(PageRequestDTO pageRequestDTO, Pageable pageable) {

        // 출력 화면 표시
        List<Tuple> content = queryFactory
                .select(faq.faqNo, faq.faqtitle, faq.faqhit, faq.date)
                .from(faq)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()) //부터 10개까지(size 만큼까지)
                .orderBy(faq.faqNo.desc()) // 내림차순으로 정렬
                .fetch(); // 1개의 리스트(10개의튜플)이 담겨진 content값
        //총 글 갯수
        long total = queryFactory
                .select(faq.count())
                .from(faq)
                .fetchOne();


        //페이징 처리를 위해 page 객체 리턴 , 페이지 형태로 데이터를 반환 , 1. 한페이지에 보여지는 리스트,모든 데이터,pg-1,size,no내림차순
        return new PageImpl<Tuple>(content, pageable, total); //pageable : 요청한 페이지의 정보 ( 개수, 크기, 번호 정렬 방식을 위해 필요)
    }

    @Override
    public Page<Tuple> selectFaqForOption1(PageRequestDTO pagerequestDTO, Pageable pageable) {
        BooleanExpression condition = faq.cate.parent.boardCateId.eq(pagerequestDTO.getParentId());
        //출력화면표시
        List<Tuple> content = queryFactory
                .select(faq.faqNo, faq.faqtitle, faq.faqhit, faq.date, faq.cate)
                .from(faq)
                .where(condition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()) //부터 10개까지(size 만큼까지)
                .orderBy(faq.faqNo.desc()) // 내림차순으로 정렬
                .fetch(); // 1개의 리스트(10개의튜플)이 담겨진 content값
        log.info("1번째 콘텐츠 : " + content);
        long total = queryFactory
                .select(faq.count())
                .from(faq)
                .where(condition)
                .fetchOne();

        //페이징 처리를 위해 page 객체 리턴 , 페이지 형태로 데이터를 반환 , 1. 한페이지에 보여지는 리스트,모든 데이터,pg-1,size,no내림차순
        return new PageImpl<Tuple>(content, pageable, total); //pageable : 요청한 페이지의 정보 ( 개수, 크기, 번호 정렬 방식을 위해 필요)
    }

    @Override
    public Page<Tuple> selectFaqForOption2(PageRequestDTO pagerequestDTO, Pageable pageable) {

        BooleanExpression condition = faq.cate.boardCateId.eq(pagerequestDTO.getChildId());
        //출력화면표시
        List<Tuple> content = queryFactory
                .select(faq.faqNo, faq.faqtitle, faq.faqhit, faq.date, faq.cate)
                .from(faq)
                .where(condition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()) //부터 10개까지(size 만큼까지)
                .orderBy(faq.faqNo.desc()) // 내림차순으로 정렬
                .fetch(); // 1개의 리스트(10개의튜플)이 담겨진 content값
        log.info("2번째 콘텐츠 : " + content);
        long total = queryFactory
                .select(faq.count())
                .from(faq)
                .where(condition)
                .fetchOne();


        //페이징 처리를 위해 page 객체 리턴 , 페이지 형태로 데이터를 반환 , 1. 한페이지에 보여지는 리스트,모든 데이터,pg-1,size,no내림차순
        return new PageImpl<Tuple>(content, pageable, total); //pageable : 요청한 페이지의 정보 ( 개수, 크기, 번호 정렬 방식을 위해 필요)
    }
}

