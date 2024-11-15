package com.lotteon.entity.User;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSeller is a Querydsl query type for Seller
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSeller extends EntityPathBase<Seller> {

    private static final long serialVersionUID = -1342656975L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSeller seller = new QSeller("seller");

    public final StringPath addr = createString("addr");

    public final StringPath addr2 = createString("addr2");

    public final StringPath bno = createString("bno");

    public final StringPath brand = createString("brand");

    public final StringPath ceo = createString("ceo");

    public final StringPath company = createString("company");

    public final StringPath fax = createString("fax");

    public final StringPath grade = createString("grade");

    public final StringPath hp = createString("hp");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isAdmin = createBoolean("isAdmin");

    public final StringPath mo = createString("mo");

    public final StringPath postcode = createString("postcode");

    public final DateTimePath<java.time.LocalDateTime> regDate = createDateTime("regDate", java.time.LocalDateTime.class);

    public final StringPath status = createString("status");

    public final QUser user;

    public QSeller(String variable) {
        this(Seller.class, forVariable(variable), INITS);
    }

    public QSeller(Path<? extends Seller> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSeller(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSeller(PathMetadata metadata, PathInits inits) {
        this(Seller.class, metadata, inits);
    }

    public QSeller(Class<? extends Seller> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

