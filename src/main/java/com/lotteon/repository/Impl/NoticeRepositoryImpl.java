package com.lotteon.repository.Impl;

import com.lotteon.dto.page.PageRequestDTO;
import com.lotteon.entity.NoticeType;
import com.lotteon.entity.QNotice;
import com.lotteon.repository.admin.NoticeRepository;
import com.lotteon.repository.custom.NoticeRepositoryCustom;
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
public class NoticeRepositoryImpl implements NoticeRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private QNotice notice = QNotice.notice;

    @Override
    public Page<Tuple> selectNoticeAllForList(PageRequestDTO pageRequestDTO, Pageable pageable) {
        // 출력 화면 표시
        List<Tuple> content = queryFactory
                .select(notice.noticeNo, notice.noticetitle, notice.noticehit, notice.date)
                .from(notice)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(notice.noticeNo.desc())
                .fetch();
        //총 글 갯수
        long total = queryFactory
                .select(notice.count())
                .from(notice)
                .fetchOne();
        log.info("asdfasdfdafasdfdsafadsfe++++++++++++++ : " + total);


        //페이징 처리를 위해 page 객체 리턴 , 페이지 형태로 데이터를 반환 , 1. 한페이지에 보여지는 리스트,모든 데이터,pg-1,size,no내림차순
        return new PageImpl<Tuple>(content, pageable, total); //pageable : 요청한 페이지의 정보 ( 개수, 크기, 번호 정렬 방식을 위해 필요)
    }

    @Override
    public Page<Tuple> selectNoticeTypeList(PageRequestDTO pageRequestDTO, Pageable pageable) {
        BooleanExpression condition = notice.noticetype.eq(NoticeType.valueOf(pageRequestDTO.getNoticeType()));
        //출력화면
        List<Tuple> content = queryFactory
                .select(notice.noticeNo, notice.noticetitle, notice.noticetype, notice.date, notice.noticehit)
                .from(notice)
                .where(condition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(notice.noticeNo.desc())
                .fetch();
        log.info("호호호호호호홓 : " + content);
        //총 글 갯수
        long total = queryFactory
                .select(notice.count())
                .from(notice)
                .where(condition)
                .fetchOne();


        //페이징 처리를 위해 page 객체 리턴 , 페이지 형태로 데이터를 반환 , 1. 한페이지에 보여지는 리스트,모든 데이터,pg-1,size,no내림차순
        return new PageImpl<Tuple>(content, pageable, total); //pageable : 요청한 페이지의 정보 ( 개수, 크기, 번호 정렬 방식을 위해 필요)
    }
}
