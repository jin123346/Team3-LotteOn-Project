package com.lotteon.repository.admin;

import com.lotteon.dto.admin.TermsDto;
import com.lotteon.entity.admin.Terms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TermsRepository extends JpaRepository<Terms, Long> {
    List<Terms> findByType(String type); // 반환 타입을 Terms로 수정

    List<Terms> findByTypeIn(List<String> types); // 여러 타입으로 검색
}
