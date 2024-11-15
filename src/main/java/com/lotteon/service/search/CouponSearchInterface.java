package com.lotteon.service.search;

import com.lotteon.dto.admin.CouponDTO;
import com.lotteon.dto.admin.CouponIssuedDTO;
import com.lotteon.entity.admin.CouponIssued;

import java.util.List;

public interface CouponSearchInterface {

    List<CouponDTO> searchByCouponName(String query);
    List<CouponDTO> searchBySellerCompany(String query);
//    List<CouponDTO> searchByCouponId(String query);

    List<CouponIssuedDTO> issuedSearchByCouponCompany(String query);
    List<CouponIssuedDTO> issuedSearchByCouponType(String query);
    List<CouponIssuedDTO> issuedSearchByCouponName(String query);

}
