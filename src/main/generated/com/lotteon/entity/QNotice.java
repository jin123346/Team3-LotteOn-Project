package com.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QNotice is a Querydsl query type for Notice
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNotice extends EntityPathBase<Notice> {

    private static final long serialVersionUID = 1676355177L;

    public static final QNotice notice = new QNotice("notice");

    public final DateTimePath<java.time.LocalDateTime> date = createDateTime("date", java.time.LocalDateTime.class);

    public final StringPath noticecontent = createString("noticecontent");

    public final NumberPath<Integer> noticehit = createNumber("noticehit", Integer.class);

    public final NumberPath<Long> noticeNo = createNumber("noticeNo", Long.class);

    public final StringPath noticetitle = createString("noticetitle");

    public final EnumPath<NoticeType> noticetype = createEnum("noticetype", NoticeType.class);

    public QNotice(String variable) {
        super(Notice.class, forVariable(variable));
    }

    public QNotice(Path<? extends Notice> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNotice(PathMetadata metadata) {
        super(Notice.class, metadata);
    }

}

