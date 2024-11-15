package com.lotteon.entity.order;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrder is a Querydsl query type for Order
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrder extends EntityPathBase<Order> {

    private static final long serialVersionUID = -855198787L;

    public static final QOrder order = new QOrder("order1");

    public final StringPath addr1 = createString("addr1");

    public final StringPath addr2 = createString("addr2");

    public final StringPath couponId = createString("couponId");

    public final NumberPath<Long> expectedPoint = createNumber("expectedPoint", Long.class);

    public final StringPath hp = createString("hp");

    public final BooleanPath isCoupon = createBoolean("isCoupon");

    public final StringPath memberHp = createString("memberHp");

    public final StringPath memberName = createString("memberName");

    public final DateTimePath<java.time.LocalDateTime> orderDate = createDateTime("orderDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> orderId = createNumber("orderId", Long.class);

    public final ListPath<OrderItem, QOrderItem> orderProducts = this.<OrderItem, QOrderItem>createList("orderProducts", OrderItem.class, QOrderItem.class, PathInits.DIRECT2);

    public final StringPath orderStatus = createString("orderStatus");

    public final StringPath pay = createString("pay");

    public final StringPath postcode = createString("postcode");

    public final NumberPath<Long> productDiscount = createNumber("productDiscount", Long.class);

    public final StringPath productName = createString("productName");

    public final StringPath receiver = createString("receiver");

    public final StringPath sellerCompany = createString("sellerCompany");

    public final StringPath shippingInfo = createString("shippingInfo");

    public final NumberPath<Long> totalDiscount = createNumber("totalDiscount", Long.class);

    public final NumberPath<Long> totalOriginalPrice = createNumber("totalOriginalPrice", Long.class);

    public final NumberPath<Long> totalPrice = createNumber("totalPrice", Long.class);

    public final NumberPath<Long> totalQuantity = createNumber("totalQuantity", Long.class);

    public final NumberPath<Long> totalShipping = createNumber("totalShipping", Long.class);

    public final StringPath uid = createString("uid");

    public final NumberPath<Long> usedCoupon = createNumber("usedCoupon", Long.class);

    public final NumberPath<Long> usedPoint = createNumber("usedPoint", Long.class);

    public QOrder(String variable) {
        super(Order.class, forVariable(variable));
    }

    public QOrder(Path<? extends Order> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOrder(PathMetadata metadata) {
        super(Order.class, metadata);
    }

}

