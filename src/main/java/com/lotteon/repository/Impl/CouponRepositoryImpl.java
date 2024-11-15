package com.lotteon.repository.Impl;

import com.lotteon.dto.admin.CouponListResponseDTO;
import com.lotteon.entity.User.QSeller;
import com.lotteon.entity.admin.QCoupon;
import com.lotteon.repository.custom.CouponRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Log4j2
@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private QCoupon qCoupon = QCoupon.coupon;
    private QSeller qSeller = QSeller.seller;

    // 기본 셀러 조회
    @Override
    public Page<CouponListResponseDTO> selectCouponByUserIdForList(Long  sellerId, Pageable pageable) {

        List<CouponListResponseDTO> coupons = queryFactory
                .select(Projections.constructor(CouponListResponseDTO.class,
                        qCoupon.couponId,
                        qCoupon.couponName,
                        qCoupon.couponType,
                        qCoupon.benefit,
                        qCoupon.startDate,
                        qCoupon.endDate,
                        qCoupon.notes,
                        qCoupon.issuedCount,
                        qCoupon.usedCount,
                        qCoupon.status,
                        qCoupon.rdate,
                        qCoupon.sellerCompany
                ))
                .from(qCoupon)
                .join(qCoupon.seller, qSeller)
                .where(qSeller.id.eq(sellerId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(qCoupon.count())
                .from(qCoupon)
                .join(qCoupon.seller, qSeller)
                .where(qSeller.id.eq(sellerId))
                .fetchOne();

        return new PageImpl<>(coupons, pageable, total);
    }

    // 어드민 조회
    @Override
    public Page<CouponListResponseDTO> selectAllCouponsForAdmin(Pageable pageable) {
        List<CouponListResponseDTO> coupons = queryFactory
                .select(Projections.constructor(CouponListResponseDTO.class,
                        qCoupon.couponId,
                        qCoupon.couponName,
                        qCoupon.couponType,
                        qCoupon.benefit,
                        qCoupon.startDate,
                        qCoupon.endDate,
                        qCoupon.notes,
                        qCoupon.issuedCount,
                        qCoupon.usedCount,
                        qCoupon.status,
                        qCoupon.rdate,
                        qCoupon.sellerCompany
                ))
                .from(qCoupon)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(qCoupon.count())
                .from(qCoupon)
                .fetchOne();

        return new PageImpl<>(coupons, pageable, total);
    }

    @Override
    public Page<CouponListResponseDTO> searchCoupons(Long  sellerId, Pageable pageable, String searchType, String searchValue) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(qSeller.id.eq(sellerId));

        if(searchType.equals("couponName") && searchValue != null) {
            builder.and(qCoupon.couponName.contains(searchValue)); // 쿠폰명 검색
        }else if (searchType.equals("sellerCompany") && searchValue != null) {
            builder.and(qCoupon.sellerCompany.contains(searchValue)); // 발급자 검색
        }else if (searchType.equals("couponId") && searchValue != null) {
            builder.and(qCoupon.couponId.contains(searchValue)); // 쿠폰 번호 검색
        }

        List<CouponListResponseDTO> coupons = queryFactory
                .select(Projections.constructor(CouponListResponseDTO.class,
                        qCoupon.couponId,
                        qCoupon.couponName,
                        qCoupon.couponType,
                        qCoupon.benefit,
                        qCoupon.startDate,
                        qCoupon.endDate,
                        qCoupon.notes,
                        qCoupon.issuedCount,
                        qCoupon.usedCount,
                        qCoupon.status,
                        qCoupon.rdate,
                        qCoupon.sellerCompany // 셀러 회사명
                ))
                .from(qCoupon)
                .join(qCoupon.seller, qSeller) // 쿠폰과 셀러 조인
                .where(qSeller.id.eq(sellerId)) // UID로 필터링
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(qCoupon.count())
                .from(qCoupon)
                .join(qCoupon.seller, qSeller)
                .where(qSeller.id.eq(sellerId)) // UID로 필터링
                .fetchOne();

        return  new PageImpl<>(coupons, pageable, total);
    }


}
