package com.lotteon.repository.custom;

import com.lotteon.dto.admin.CouponListResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponRepositoryCustom {

   Page<CouponListResponseDTO> selectCouponByUserIdForList (Long  sellerId , Pageable pageable);


   Page<CouponListResponseDTO> searchCoupons(Long  sellerId, Pageable pageable, String searchType, String searchValue); // 검색 기능 추가
   Page<CouponListResponseDTO> selectAllCouponsForAdmin(Pageable pageable);

}
