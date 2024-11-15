package com.lotteon.entity.admin;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCoupon is a Querydsl query type for Coupon
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCoupon extends EntityPathBase<Coupon> {

    private static final long serialVersionUID = 1998350966L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCoupon coupon = new QCoupon("coupon");

    public final StringPath benefit = createString("benefit");

    public final StringPath couponId = createString("couponId");

    public final ListPath<CouponIssued, QCouponIssued> couponIssued = this.<CouponIssued, QCouponIssued>createList("couponIssued", CouponIssued.class, QCouponIssued.class, PathInits.DIRECT2);

    public final StringPath couponName = createString("couponName");

    public final StringPath couponType = createString("couponType");

    public final DatePath<java.time.LocalDate> endDate = createDate("endDate", java.time.LocalDate.class);

    public final NumberPath<Integer> issuedCount = createNumber("issuedCount", Integer.class);

    public final StringPath notes = createString("notes");

    public final com.lotteon.entity.product.QProduct product;

    public final DatePath<java.time.LocalDate> rdate = createDate("rdate", java.time.LocalDate.class);

    public final StringPath restrictions = createString("restrictions");

    public final com.lotteon.entity.User.QSeller seller;

    public final StringPath sellerCompany = createString("sellerCompany");

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public final StringPath status = createString("status");

    public final NumberPath<Integer> usedCount = createNumber("usedCount", Integer.class);

    public QCoupon(String variable) {
        this(Coupon.class, forVariable(variable), INITS);
    }

    public QCoupon(Path<? extends Coupon> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCoupon(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCoupon(PathMetadata metadata, PathInits inits) {
        this(Coupon.class, metadata, inits);
    }

    public QCoupon(Class<? extends Coupon> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new com.lotteon.entity.product.QProduct(forProperty("product"), inits.get("product")) : null;
        this.seller = inits.isInitialized("seller") ? new com.lotteon.entity.User.QSeller(forProperty("seller"), inits.get("seller")) : null;
    }

}

