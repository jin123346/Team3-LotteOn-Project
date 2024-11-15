package com.lotteon.entity.User;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPoint is a Querydsl query type for Point
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPoint extends EntityPathBase<Point> {

    private static final long serialVersionUID = -45786978L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPoint point = new QPoint("point");

    public final NumberPath<Double> amount = createNumber("amount", Double.class);

    public final BooleanPath confirm = createBoolean("confirm");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> limitDate = createDateTime("limitDate", java.time.LocalDateTime.class);

    public final QMember member;

    public final NumberPath<Long> orderId = createNumber("orderId", Long.class);

    public final NumberPath<Long> orderItemId = createNumber("orderItemId", Long.class);

    public final NumberPath<Double> remainingPoints = createNumber("remainingPoints", Double.class);

    public final NumberPath<Double> usedPoint = createNumber("usedPoint", Double.class);

    public QPoint(String variable) {
        this(Point.class, forVariable(variable), INITS);
    }

    public QPoint(Path<? extends Point> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPoint(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPoint(PathMetadata metadata, PathInits inits) {
        this(Point.class, metadata, inits);
    }

    public QPoint(Class<? extends Point> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

