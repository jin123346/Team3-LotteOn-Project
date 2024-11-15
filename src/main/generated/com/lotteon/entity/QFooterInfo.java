package com.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFooterInfo is a Querydsl query type for FooterInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFooterInfo extends EntityPathBase<FooterInfo> {

    private static final long serialVersionUID = 874948602L;

    public static final QFooterInfo footerInfo = new QFooterInfo("footerInfo");

    public final StringPath ft_addr1 = createString("ft_addr1");

    public final StringPath ft_addr2 = createString("ft_addr2");

    public final StringPath ft_bo = createString("ft_bo");

    public final StringPath ft_ceo = createString("ft_ceo");

    public final StringPath ft_company = createString("ft_company");

    public final StringPath ft_copyright = createString("ft_copyright");

    public final StringPath ft_email = createString("ft_email");

    public final StringPath ft_hp = createString("ft_hp");

    public final NumberPath<Integer> ft_id = createNumber("ft_id", Integer.class);

    public final StringPath ft_mo = createString("ft_mo");

    public final StringPath ft_time = createString("ft_time");

    public final StringPath ft_troublehp = createString("ft_troublehp");

    public QFooterInfo(String variable) {
        super(FooterInfo.class, forVariable(variable));
    }

    public QFooterInfo(Path<? extends FooterInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFooterInfo(PathMetadata metadata) {
        super(FooterInfo.class, metadata);
    }

}

