package com.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QHeaderInfo is a Querydsl query type for HeaderInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHeaderInfo extends EntityPathBase<HeaderInfo> {

    private static final long serialVersionUID = 1431899116L;

    public static final QHeaderInfo headerInfo = new QHeaderInfo("headerInfo");

    public final NumberPath<Integer> hd_id = createNumber("hd_id", Integer.class);

    public final StringPath hd_oName1 = createString("hd_oName1");

    public final StringPath hd_oName2 = createString("hd_oName2");

    public final StringPath hd_oName3 = createString("hd_oName3");

    public final StringPath hd_sName1 = createString("hd_sName1");

    public final StringPath hd_sName2 = createString("hd_sName2");

    public final StringPath hd_sName3 = createString("hd_sName3");

    public final StringPath hd_subtitle = createString("hd_subtitle");

    public final StringPath hd_title = createString("hd_title");

    public QHeaderInfo(String variable) {
        super(HeaderInfo.class, forVariable(variable));
    }

    public QHeaderInfo(Path<? extends HeaderInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHeaderInfo(PathMetadata metadata) {
        super(HeaderInfo.class, metadata);
    }

}

