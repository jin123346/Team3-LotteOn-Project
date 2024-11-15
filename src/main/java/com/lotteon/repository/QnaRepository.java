package com.lotteon.repository;

import com.lotteon.dto.adminQnaDTO;
import com.lotteon.entity.QnA;
import com.lotteon.entity.admin.QAdminqna;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
   날짜 : 2024/00/00
   이름 : 박수정
   내용 : Qna 레퍼지토리 생성
 */

@Repository
public interface QnaRepository extends JpaRepository<QnA, Integer> {

    // 특정 작성자(qna_writer)로 QnA 조회
    @Query("SELECT q FROM  QnA q WHERE q.qna_writer = :writer")
    List<QnA> findByQnaWriter(@Param("writer") String writer);

    // 특정 작성자(qna_writer)로 페이징 처리된 QnA 조회
    @Query("SELECT q FROM QnA q WHERE q.qna_writer = :writer")
    Page<QnA> findByQnaWriter(@Param("writer") String writer, Pageable pageable);

    // 특정 카테고리(qna_type1)로 페이징 처리된 QnA 조회
    @Query("SELECT q FROM  QnA q WHERE q.qna_type1 = :type1")
    Page<QnA> findByQna_type1(String type1, Pageable pageable);

}
