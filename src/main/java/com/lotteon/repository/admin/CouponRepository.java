package com.lotteon.repository.admin;

import com.lotteon.entity.admin.Coupon;
import com.lotteon.entity.product.Product;
import com.lotteon.repository.custom.CouponRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, String>, CouponRepositoryCustom  {

    Page<Coupon> findBySellerId(Long sellerId, Pageable pageable);
    List<Coupon> findByProduct_productId(Long productId);

    List<Coupon> findByProductIsNull();

    List<Coupon> findByCouponNameContaining(String couponName);
    List<Coupon> findAllByStatus(String status);
    List<Coupon> findBySellerCompanyContaining(String sellerCompany);

}
