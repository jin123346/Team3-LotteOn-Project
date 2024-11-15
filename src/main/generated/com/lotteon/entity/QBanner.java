package com.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBanner is a Querydsl query type for Banner
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBanner extends EntityPathBase<Banner> {

    private static final long serialVersionUID = 1319702205L;

    public static final QBanner banner = new QBanner("banner");

    public final StringPath ban_color = createString("ban_color");

    public final StringPath ban_edate = createString("ban_edate");

    public final StringPath ban_etime = createString("ban_etime");

    public final NumberPath<Integer> ban_id = createNumber("ban_id", Integer.class);

    public final StringPath ban_image = createString("ban_image");

    public final StringPath ban_link = createString("ban_link");

    public final StringPath ban_location = createString("ban_location");

    public final StringPath ban_name = createString("ban_name");

    public final StringPath ban_oname = createString("ban_oname");

    public final StringPath ban_sdate = createString("ban_sdate");

    public final StringPath ban_size = createString("ban_size");

    public final StringPath ban_stime = createString("ban_stime");

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public QBanner(String variable) {
        super(Banner.class, forVariable(variable));
    }

    public QBanner(Path<? extends Banner> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBanner(PathMetadata metadata) {
        super(Banner.class, metadata);
    }

}

