package com.lotteon.repository;

import com.lotteon.entity.BoardCate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository  extends JpaRepository<BoardCate, Long> {
    List<BoardCate> findByLevel(int level); // level entity에있는 이름

    @Query("SELECT b FROM BoardCate b WHERE b.parent.boardCateId = :parentId")
    List<BoardCate> findByParentId(@Param("parentId") Long parentId);

    BoardCate findByBoardCateId(Long boardCateId);

}
