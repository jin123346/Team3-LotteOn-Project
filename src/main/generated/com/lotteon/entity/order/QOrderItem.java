package com.lotteon.entity.order;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrderItem is a Querydsl query type for OrderItem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderItem extends EntityPathBase<OrderItem> {

    private static final long serialVersionUID = -1090552720L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrderItem orderItem = new QOrderItem("orderItem");

    public final StringPath combination = createString("combination");

    public final NumberPath<Long> combinationId = createNumber("combinationId", Long.class);

    public final StringPath company = createString("company");

    public final StringPath customerId = createString("customerId");

    public final StringPath customerName = createString("customerName");

    public final NumberPath<Long> deliveryId = createNumber("deliveryId", Long.class);

    public final StringPath optionDesc = createString("optionDesc");

    public final NumberPath<Long> optionId = createNumber("optionId", Long.class);

    public final QOrder order;

    public final NumberPath<Long> orderItemId = createNumber("orderItemId", Long.class);

    public final NumberPath<Long> orderPrice = createNumber("orderPrice", Long.class);

    public final NumberPath<Long> point = createNumber("point", Long.class);

    public final NumberPath<Long> price = createNumber("price", Long.class);

    public final com.lotteon.entity.product.QProduct product;

    public final NumberPath<Long> savedDiscount = createNumber("savedDiscount", Long.class);

    public final NumberPath<Long> savedPrice = createNumber("savedPrice", Long.class);

    public final StringPath sellerUid = createString("sellerUid");

    public final NumberPath<Long> shippingFees = createNumber("shippingFees", Long.class);

    public final EnumPath<com.lotteon.dto.order.DeliveryStatus> status = createEnum("status", com.lotteon.dto.order.DeliveryStatus.class);

    public final NumberPath<Long> stock = createNumber("stock", Long.class);

    public final StringPath traceNumber = createString("traceNumber");

    public QOrderItem(String variable) {
        this(OrderItem.class, forVariable(variable), INITS);
    }

    public QOrderItem(Path<? extends OrderItem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrderItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrderItem(PathMetadata metadata, PathInits inits) {
        this(OrderItem.class, metadata, inits);
    }

    public QOrderItem(Class<? extends OrderItem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.order = inits.isInitialized("order") ? new QOrder(forProperty("order")) : null;
        this.product = inits.isInitialized("product") ? new com.lotteon.entity.product.QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

