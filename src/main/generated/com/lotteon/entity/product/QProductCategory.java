package com.lotteon.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductCategory is a Querydsl query type for ProductCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductCategory extends EntityPathBase<ProductCategory> {

    private static final long serialVersionUID = 1379385501L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductCategory productCategory = new QProductCategory("productCategory");

    public final ListPath<ProductCategory, QProductCategory> children = this.<ProductCategory, QProductCategory>createList("children", ProductCategory.class, QProductCategory.class, PathInits.DIRECT2);

    public final StringPath disp_yn = createString("disp_yn");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> level = createNumber("level", Integer.class);

    public final StringPath name = createString("name");

    public final StringPath note = createString("note");

    public final QProductCategory parent;

    public final StringPath subcategory = createString("subcategory");

    public QProductCategory(String variable) {
        this(ProductCategory.class, forVariable(variable), INITS);
    }

    public QProductCategory(Path<? extends ProductCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductCategory(PathMetadata metadata, PathInits inits) {
        this(ProductCategory.class, metadata, inits);
    }

    public QProductCategory(Class<? extends ProductCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.parent = inits.isInitialized("parent") ? new QProductCategory(forProperty("parent"), inits.get("parent")) : null;
    }

}

