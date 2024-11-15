package com.lotteon.entity.admin;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCouponIssued is a Querydsl query type for CouponIssued
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCouponIssued extends EntityPathBase<CouponIssued> {

    private static final long serialVersionUID = -337602367L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCouponIssued couponIssued = new QCouponIssued("couponIssued");

    public final StringPath benefit = createString("benefit");

    public final QCoupon coupon;

    public final StringPath couponId = createString("couponId");

    public final StringPath couponName = createString("couponName");

    public final StringPath couponType = createString("couponType");

    public final DatePath<java.time.LocalDate> endDate = createDate("endDate", java.time.LocalDate.class);

    public final StringPath issuanceNumber = createString("issuanceNumber");

    public final com.lotteon.entity.User.QMember member;

    public final StringPath memberName = createString("memberName");

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    public final StringPath productName = createString("productName");

    public final StringPath restrictions = createString("restrictions");

    public final StringPath sellerCompany = createString("sellerCompany");

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public final StringPath status = createString("status");

    public final DateTimePath<java.time.LocalDateTime> usageDate = createDateTime("usageDate", java.time.LocalDateTime.class);

    public final StringPath usageStatus = createString("usageStatus");

    public final DateTimePath<java.time.LocalDateTime> usedDate = createDateTime("usedDate", java.time.LocalDateTime.class);

    public QCouponIssued(String variable) {
        this(CouponIssued.class, forVariable(variable), INITS);
    }

    public QCouponIssued(Path<? extends CouponIssued> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCouponIssued(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCouponIssued(PathMetadata metadata, PathInits inits) {
        this(CouponIssued.class, metadata, inits);
    }

    public QCouponIssued(Class<? extends CouponIssued> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.coupon = inits.isInitialized("coupon") ? new QCoupon(forProperty("coupon"), inits.get("coupon")) : null;
        this.member = inits.isInitialized("member") ? new com.lotteon.entity.User.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

