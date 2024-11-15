package com.lotteon.repository.product;

import com.lotteon.entity.product.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/*
    날짜 : 2024-10-29
    이름 : 최영진
    내용 : option 삭제
*/
@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {

}
