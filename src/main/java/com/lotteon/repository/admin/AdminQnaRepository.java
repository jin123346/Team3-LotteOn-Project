package com.lotteon.repository.admin;

import com.lotteon.entity.QnA;
import com.lotteon.entity.admin.Adminqna;
import com.lotteon.entity.admin.QAdminqna;
import com.lotteon.repository.custom.AdminQnaRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminQnaRepository extends JpaRepository<Adminqna, Integer> , AdminQnaRepositoryCustom {
    // 특정 작성자(qna_writer)로 QnA 조회
    @Query("SELECT q FROM  Adminqna q WHERE q.qnawriter = :writer")
    List<Adminqna> findByQnaWriter(@Param("writer") String writer);

    // 특정 작성자(qnawriter)로 페이징 처리된 QnA 조회
    @Query("SELECT q FROM Adminqna q WHERE q.qnawriter = :writer")
    Page<Adminqna> findByQnaWriter(@Param("writer") String writer, Pageable pageable);

    // 특정 productId에 해당하는 QnA 조회
    @Query("SELECT q FROM Adminqna q WHERE q.productid = :productId")
    Page<Adminqna> findByProductId(@Param("productId") long productId, Pageable pageable);

}
