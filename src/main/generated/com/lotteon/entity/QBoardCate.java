package com.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoardCate is a Querydsl query type for BoardCate
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardCate extends EntityPathBase<BoardCate> {

    private static final long serialVersionUID = -793053660L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoardCate boardCate = new QBoardCate("boardCate");

    public final NumberPath<Long> boardCateId = createNumber("boardCateId", Long.class);

    public final ListPath<BoardCate, QBoardCate> children = this.<BoardCate, QBoardCate>createList("children", BoardCate.class, QBoardCate.class, PathInits.DIRECT2);

    public final NumberPath<Integer> level = createNumber("level", Integer.class);

    public final StringPath name = createString("name");

    public final QBoardCate parent;

    public QBoardCate(String variable) {
        this(BoardCate.class, forVariable(variable), INITS);
    }

    public QBoardCate(Path<? extends BoardCate> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoardCate(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoardCate(PathMetadata metadata, PathInits inits) {
        this(BoardCate.class, metadata, inits);
    }

    public QBoardCate(Class<? extends BoardCate> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.parent = inits.isInitialized("parent") ? new QBoardCate(forProperty("parent"), inits.get("parent")) : null;
    }

}

