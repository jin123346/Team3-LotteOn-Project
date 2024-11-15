package com.lotteon.entity.order;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductDelivery is a Querydsl query type for ProductDelivery
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductDelivery extends EntityPathBase<ProductDelivery> {

    private static final long serialVersionUID = -1070818382L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductDelivery productDelivery = new QProductDelivery("productDelivery");

    public final StringPath addr = createString("addr");

    public final StringPath addr2 = createString("addr2");

    public final StringPath deliveryCompany = createString("deliveryCompany");

    public final StringPath deliveryMessage = createString("deliveryMessage");

    public final QOrderItem orderItem;

    public final StringPath postcode = createString("postcode");

    public final NumberPath<Long> productDeliveryId = createNumber("productDeliveryId", Long.class);

    public final StringPath receiver = createString("receiver");

    public final StringPath trackingnumber = createString("trackingnumber");

    public QProductDelivery(String variable) {
        this(ProductDelivery.class, forVariable(variable), INITS);
    }

    public QProductDelivery(Path<? extends ProductDelivery> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductDelivery(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductDelivery(PathMetadata metadata, PathInits inits) {
        this(ProductDelivery.class, metadata, inits);
    }

    public QProductDelivery(Class<? extends ProductDelivery> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.orderItem = inits.isInitialized("orderItem") ? new QOrderItem(forProperty("orderItem"), inits.get("orderItem")) : null;
    }

}

