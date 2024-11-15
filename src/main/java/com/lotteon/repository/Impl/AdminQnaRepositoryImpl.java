package com.lotteon.repository.Impl;

import com.lotteon.dto.page.PageRequestDTO;
import com.lotteon.entity.admin.QAdminqna;
import com.lotteon.repository.custom.AdminQnaRepositoryCustom;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Repository
public class AdminQnaRepositoryImpl implements AdminQnaRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private QAdminqna adminqna = QAdminqna.adminqna;

    @Override
    public Page<Tuple> selectAdminqnaAllForList(PageRequestDTO pageRequestDTO, Pageable pageable) {
        List<Tuple> content = queryFactory
                .select(adminqna.qnaNo, adminqna.qnatitle, adminqna.qnacontent, adminqna.qnawriter, adminqna.qna_status)
                .from(adminqna)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(adminqna.qnaNo.desc())
                .fetch();

        long total = queryFactory
                .select(adminqna.count())
                .from(adminqna)
                .fetchOne();

        //페이징 처리를 위해 page 객체 리턴 , 페이지 형태로 데이터를 반환 , 1. 한페이지에 보여지는 리스트,모든 데이터,pg-1,size,no내림차순
        return new PageImpl<Tuple>(content, pageable, total); //pageable : 요청한 페이지의 정보 ( 개수, 크기, 번호 정렬 방식을 위해 필요)

    }

    @Override
    public Page<Tuple> selectAdminqnBySellerForList(PageRequestDTO pageRequestDTO, Pageable pageable, int sellerid) {
        List<Tuple> content = queryFactory
                .select(adminqna.qnaNo, adminqna.qnatitle, adminqna.qnacontent, adminqna.qnawriter, adminqna.qna_status)
                .from(adminqna)
                .where(adminqna.sellerid.eq(sellerid))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(adminqna.qnaNo.desc())
                .fetch();

        long total = queryFactory
                .select(adminqna.count())
                .from(adminqna)
                .where(adminqna.sellerid.eq(sellerid))

                .fetchOne();

        //페이징 처리를 위해 page 객체 리턴 , 페이지 형태로 데이터를 반환 , 1. 한페이지에 보여지는 리스트,모든 데이터,pg-1,size,no내림차순
        return new PageImpl<Tuple>(content, pageable, total); //pageable
    }

    @Override
    public Page<Tuple> selectAdminqnaForOption1(PageRequestDTO pagerequestDTO, Pageable pageable) {
        BooleanExpression condition = adminqna.cate.parent.boardCateId.eq(pagerequestDTO.getParentId());

        List<Tuple> content = queryFactory
                .select(adminqna.qnaNo, adminqna.qnatitle, adminqna.qnacontent, adminqna.qnawriter, adminqna.qna_status)
                .from(adminqna)
                .where(condition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(adminqna.qnaNo.desc())
                .fetch();
        long total = queryFactory
                .select(adminqna.count())
                .from(adminqna)
                .where(condition)
                .fetchOne();

        //페이징 처리를 위해 page 객체 리턴 , 페이지 형태로 데이터를 반환 , 1. 한페이지에 보여지는 리스트,모든 데이터,pg-1,size,no내림차순
        return new PageImpl<Tuple>(content, pageable, total); //pageable : 요청한 페이지의 정보 ( 개수, 크기, 번호 정렬 방식을 위해 필요)

    }

    @Override
    public Page<Tuple> selectAdminqnaForOption2(PageRequestDTO pageRequestDTO, Pageable pageable) {
        BooleanExpression condition = adminqna.cate.boardCateId.eq(pageRequestDTO.getChildId());

        List<Tuple> content = queryFactory
                .select(adminqna.qnaNo, adminqna.qnatitle, adminqna.qnacontent, adminqna.qnawriter, adminqna.qna_status)
                .from(adminqna)
                .where(condition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(adminqna.qnaNo.desc())
                .fetch();
        long total = queryFactory
                .select(adminqna.count())
                .from(adminqna)
                .where(condition)
                .fetchOne();

        //페이징 처리를 위해 page 객체 리턴 , 페이지 형태로 데이터를 반환 , 1. 한페이지에 보여지는 리스트,모든 데이터,pg-1,size,no내림차순
        return new PageImpl<Tuple>(content, pageable, total); //pageable : 요청한 페이지의 정보 ( 개수, 크기, 번호 정렬 방식을 위해 필요)

    }

    @Override
    public Page<Tuple> selectAdminqnaForQnaWriter(PageRequestDTO pageRequestDTO, Pageable pageable) {
        BooleanExpression condition = adminqna.qnawriter.eq(pageRequestDTO.getQnawriter());

        List<Tuple> content = queryFactory
                .select(adminqna.qnaNo, adminqna.qnatitle, adminqna.qnacontent, adminqna.qnawriter, adminqna.qna_status)
                .from(adminqna)
                .where(condition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(adminqna.qnaNo.desc())
                .fetch();
        long total = queryFactory
                .select(adminqna.count())
                .from(adminqna)
                .where(condition)
                .fetchOne();

        //페이징 처리를 위해 page 객체 리턴 , 페이지 형태로 데이터를 반환 , 1. 한페이지에 보여지는 리스트,모든 데이터,pg-1,size,no내림차순
        return new PageImpl<Tuple>(content, pageable, total); //pageable : 요청한 페이지의 정보 ( 개수, 크기, 번호 정렬 방식을 위해 필요)

    }
}
