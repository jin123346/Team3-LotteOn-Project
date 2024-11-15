package com.lotteon.entity.admin;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAdminqna is a Querydsl query type for Adminqna
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAdminqna extends EntityPathBase<Adminqna> {

    private static final long serialVersionUID = -33883515L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAdminqna adminqna = new QAdminqna("adminqna");

    public final com.lotteon.entity.QBoardCate cate;

    public final DateTimePath<java.time.LocalDateTime> date = createDateTime("date", java.time.LocalDateTime.class);

    public final StringPath iscompleted = createString("iscompleted");

    public final NumberPath<Integer> productid = createNumber("productid", Integer.class);

    public final EnumPath<Adminqna.Status> qna_status = createEnum("qna_status", Adminqna.Status.class);

    public final StringPath qnacontent = createString("qnacontent");

    public final NumberPath<Integer> qnaNo = createNumber("qnaNo", Integer.class);

    public final StringPath qnareply = createString("qnareply");

    public final StringPath qnatitle = createString("qnatitle");

    public final StringPath qnawriter = createString("qnawriter");

    public final NumberPath<Integer> sellerid = createNumber("sellerid", Integer.class);

    public QAdminqna(String variable) {
        this(Adminqna.class, forVariable(variable), INITS);
    }

    public QAdminqna(Path<? extends Adminqna> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAdminqna(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAdminqna(PathMetadata metadata, PathInits inits) {
        this(Adminqna.class, metadata, inits);
    }

    public QAdminqna(Class<? extends Adminqna> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cate = inits.isInitialized("cate") ? new com.lotteon.entity.QBoardCate(forProperty("cate"), inits.get("cate")) : null;
    }

}

