package com.lotteon.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProductFile is a Querydsl query type for ProductFile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductFile extends EntityPathBase<ProductFile> {

    private static final long serialVersionUID = -763518821L;

    public static final QProductFile productFile = new QProductFile("productFile");

    public final StringPath oName = createString("oName");

    public final NumberPath<Integer> p_fno = createNumber("p_fno", Integer.class);

    public final StringPath path = createString("path");

    public final StringPath sName = createString("sName");

    public final StringPath type = createString("type");

    public QProductFile(String variable) {
        super(ProductFile.class, forVariable(variable));
    }

    public QProductFile(Path<? extends ProductFile> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProductFile(PathMetadata metadata) {
        super(ProductFile.class, metadata);
    }

}

