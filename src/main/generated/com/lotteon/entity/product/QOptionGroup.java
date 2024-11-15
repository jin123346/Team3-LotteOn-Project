package com.lotteon.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOptionGroup is a Querydsl query type for OptionGroup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOptionGroup extends EntityPathBase<OptionGroup> {

    private static final long serialVersionUID = 347863194L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOptionGroup optionGroup = new QOptionGroup("optionGroup");

    public final StringPath groupName = createString("groupName");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final BooleanPath isRequired = createBoolean("isRequired");

    public final StringPath name = createString("name");

    public final NumberPath<Long> optionGroupId = createNumber("optionGroupId", Long.class);

    public final ListPath<OptionItem, QOptionItem> optionItems = this.<OptionItem, QOptionItem>createList("optionItems", OptionItem.class, QOptionItem.class, PathInits.DIRECT2);

    public final QProduct product;

    public QOptionGroup(String variable) {
        this(OptionGroup.class, forVariable(variable), INITS);
    }

    public QOptionGroup(Path<? extends OptionGroup> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOptionGroup(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOptionGroup(PathMetadata metadata, PathInits inits) {
        this(OptionGroup.class, metadata, inits);
    }

    public QOptionGroup(Class<? extends OptionGroup> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

