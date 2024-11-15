package com.lotteon.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductOptionCombination is a Querydsl query type for ProductOptionCombination
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductOptionCombination extends EntityPathBase<ProductOptionCombination> {

    private static final long serialVersionUID = -1927901605L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductOptionCombination productOptionCombination = new QProductOptionCombination("productOptionCombination");

    public final NumberPath<Long> additionalPrice = createNumber("additionalPrice", Long.class);

    public final StringPath combination = createString("combination");

    public final NumberPath<Long> combinationId = createNumber("combinationId", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final StringPath optionCode = createString("optionCode");

    public final QProduct product;

    public final NumberPath<Long> stock = createNumber("stock", Long.class);

    public QProductOptionCombination(String variable) {
        this(ProductOptionCombination.class, forVariable(variable), INITS);
    }

    public QProductOptionCombination(Path<? extends ProductOptionCombination> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductOptionCombination(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductOptionCombination(PathMetadata metadata, PathInits inits) {
        this(ProductOptionCombination.class, metadata, inits);
    }

    public QProductOptionCombination(Class<? extends ProductOptionCombination> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

