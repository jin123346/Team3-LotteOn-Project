package com.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QQnA is a Querydsl query type for QnA
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQnA extends EntityPathBase<QnA> {

    private static final long serialVersionUID = 596490195L;

    public static final QQnA qnA = new QQnA("qnA");

    public final StringPath iscompleted = createString("iscompleted");

    public final NumberPath<Integer> productid = createNumber("productid", Integer.class);

    public final StringPath qna_content = createString("qna_content");

    public final NumberPath<Integer> qna_id = createNumber("qna_id", Integer.class);

    public final EnumPath<QnA.Status> qna_status = createEnum("qna_status", QnA.Status.class);

    public final StringPath qna_title = createString("qna_title");

    public final StringPath qna_type1 = createString("qna_type1");

    public final StringPath qna_type2 = createString("qna_type2");

    public final StringPath qna_writer = createString("qna_writer");

    public final DateTimePath<java.time.LocalDateTime> rdate = createDateTime("rdate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> sellerid = createNumber("sellerid", Integer.class);

    public QQnA(String variable) {
        super(QnA.class, forVariable(variable));
    }

    public QQnA(Path<? extends QnA> path) {
        super(path.getType(), path.getMetadata());
    }

    public QQnA(PathMetadata metadata) {
        super(QnA.class, metadata);
    }

}

