package com.lotteon.repository.admin;

import com.lotteon.entity.Notice;
import com.lotteon.entity.NoticeType;
import com.lotteon.repository.custom.NoticeRepositoryCustom;
import org.springframework.data.domain.Page;  // 올바른 Page 클래스를 import
import org.springframework.data.domain.Pageable; // Pageable도 올바르게 import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeRepositoryCustom {
    Page<Notice> findAllByOrderByDateDesc(Pageable pageable); // 날짜 기준 내림차순 정렬
    Page<Notice> findAllByNoticetype(NoticeType noticeType, Pageable pageable); // 카테고리별 조회
}
