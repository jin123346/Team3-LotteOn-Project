package com.lotteon.repository.custom;

import com.lotteon.dto.page.PageRequestDTO;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminQnaRepositoryCustom {

    public Page<Tuple> selectAdminqnaAllForList(PageRequestDTO pageRequestDTO, Pageable pageable);
    public Page<Tuple> selectAdminqnBySellerForList(PageRequestDTO pageRequestDTO, Pageable pageable,int sellerUid);

    public Page<Tuple> selectAdminqnaForOption1(PageRequestDTO pagerequestDTO, Pageable pageable);

    public Page<Tuple> selectAdminqnaForOption2(PageRequestDTO pageRequestDTO, Pageable pageable);

    public Page<Tuple> selectAdminqnaForQnaWriter(PageRequestDTO pageRequestDTO, Pageable pageable);
}
