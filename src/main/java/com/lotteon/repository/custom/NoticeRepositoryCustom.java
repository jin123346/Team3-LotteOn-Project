package com.lotteon.repository.custom;

import com.lotteon.dto.page.PageRequestDTO;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {

    Page<Tuple> selectNoticeAllForList(PageRequestDTO pageRequestDTO, Pageable pageable);
    Page<Tuple> selectNoticeTypeList(PageRequestDTO pageRequestDTO, Pageable pageable);
}
