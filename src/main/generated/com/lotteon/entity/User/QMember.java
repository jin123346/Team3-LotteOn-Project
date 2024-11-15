package com.lotteon.entity.User;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -1514411700L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMember member = new QMember("member1");

    public final StringPath addr = createString("addr");

    public final StringPath addr2 = createString("addr2");

    public final ListPath<Delivery, QDelivery> deliveryList = this.<Delivery, QDelivery>createList("deliveryList", Delivery.class, QDelivery.class, PathInits.DIRECT2);

    public final StringPath email = createString("email");

    public final StringPath gender = createString("gender");

    public final EnumPath<com.lotteon.dto.User.Grade> grade = createEnum("grade", com.lotteon.dto.User.Grade.class);

    public final StringPath hp = createString("hp");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> lastDate = createDateTime("lastDate", java.time.LocalDateTime.class);

    public final StringPath name = createString("name");

    public final NumberPath<Double> point = createNumber("point", Double.class);

    public final ListPath<Point, QPoint> points = this.<Point, QPoint>createList("points", Point.class, QPoint.class, PathInits.DIRECT2);

    public final StringPath postcode = createString("postcode");

    public final DateTimePath<java.time.LocalDateTime> regDate = createDateTime("regDate", java.time.LocalDateTime.class);

    public final EnumPath<MemberStatus> status = createEnum("status", MemberStatus.class);

    public final NumberPath<Long> totalOrder = createNumber("totalOrder", Long.class);

    public final QUser user;

    public final StringPath userinfocol = createString("userinfocol");

    public QMember(String variable) {
        this(Member.class, forVariable(variable), INITS);
    }

    public QMember(Path<? extends Member> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMember(PathMetadata metadata, PathInits inits) {
        this(Member.class, metadata, inits);
    }

    public QMember(Class<? extends Member> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

