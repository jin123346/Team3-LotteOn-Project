package com.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QVersion is a Querydsl query type for Version
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVersion extends EntityPathBase<Version> {

    private static final long serialVersionUID = -1350336665L;

    public static final QVersion version = new QVersion("version");

    public final DateTimePath<java.time.LocalDateTime> rdate = createDateTime("rdate", java.time.LocalDateTime.class);

    public final StringPath ver_content = createString("ver_content");

    public final StringPath ver_name = createString("ver_name");

    public final StringPath ver_writer = createString("ver_writer");

    public final NumberPath<Integer> verId = createNumber("verId", Integer.class);

    public QVersion(String variable) {
        super(Version.class, forVariable(variable));
    }

    public QVersion(Path<? extends Version> path) {
        super(path.getType(), path.getMetadata());
    }

    public QVersion(PathMetadata metadata) {
        super(Version.class, metadata);
    }

}

