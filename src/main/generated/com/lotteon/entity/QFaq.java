package com.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFaq is a Querydsl query type for Faq
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFaq extends EntityPathBase<Faq> {

    private static final long serialVersionUID = 596479269L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFaq faq = new QFaq("faq");

    public final QBoardCate cate;

    public final DateTimePath<java.time.LocalDateTime> date = createDateTime("date", java.time.LocalDateTime.class);

    public final StringPath faqcontent = createString("faqcontent");

    public final NumberPath<Integer> faqhit = createNumber("faqhit", Integer.class);

    public final NumberPath<Integer> faqNo = createNumber("faqNo", Integer.class);

    public final StringPath faqtitle = createString("faqtitle");

    public QFaq(String variable) {
        this(Faq.class, forVariable(variable), INITS);
    }

    public QFaq(Path<? extends Faq> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFaq(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFaq(PathMetadata metadata, PathInits inits) {
        this(Faq.class, metadata, inits);
    }

    public QFaq(Class<? extends Faq> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cate = inits.isInitialized("cate") ? new QBoardCate(forProperty("cate"), inits.get("cate")) : null;
    }

}

