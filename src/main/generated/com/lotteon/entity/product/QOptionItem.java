package com.lotteon.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOptionItem is a Querydsl query type for OptionItem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOptionItem extends EntityPathBase<OptionItem> {

    private static final long serialVersionUID = 288377240L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOptionItem optionItem = new QOptionItem("optionItem");

    public final NumberPath<Long> additionalPrice = createNumber("additionalPrice", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final NumberPath<Long> item_id = createNumber("item_id", Long.class);

    public final QOptionGroup optionGroup;

    public final StringPath optionName = createString("optionName");

    public QOptionItem(String variable) {
        this(OptionItem.class, forVariable(variable), INITS);
    }

    public QOptionItem(Path<? extends OptionItem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOptionItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOptionItem(PathMetadata metadata, PathInits inits) {
        this(OptionItem.class, metadata, inits);
    }

    public QOptionItem(Class<? extends OptionItem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.optionGroup = inits.isInitialized("optionGroup") ? new QOptionGroup(forProperty("optionGroup"), inits.get("optionGroup")) : null;
    }

}

