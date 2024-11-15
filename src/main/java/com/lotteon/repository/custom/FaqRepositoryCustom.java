package com.lotteon.repository.custom;

import com.lotteon.dto.page.PageRequestDTO;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FaqRepositoryCustom {

    public Page<Tuple> selectFaqAllForList(PageRequestDTO pageRequestDTO, Pageable pageable);

    public Page<Tuple> selectFaqForOption1(PageRequestDTO pagerequestDTO, Pageable pageable);

    public Page<Tuple> selectFaqForOption2(PageRequestDTO pageRequestDTO, Pageable pageable);
}
