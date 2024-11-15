package com.lotteon.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QOption is a Querydsl query type for Option
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOption extends EntityPathBase<Option> {

    private static final long serialVersionUID = 582767845L;

    public static final QOption option = new QOption("option");

    public final NumberPath<Integer> additionalPrice = createNumber("additionalPrice", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final StringPath optionCode = createString("optionCode");

    public final StringPath optionDesc = createString("optionDesc");

    public final StringPath optionName = createString("optionName");

    public final NumberPath<Long> optionStock = createNumber("optionStock", Long.class);

    public final StringPath parentCode = createString("parentCode");

    public QOption(String variable) {
        super(Option.class, forVariable(variable));
    }

    public QOption(Path<? extends Option> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOption(PathMetadata metadata) {
        super(Option.class, metadata);
    }

}

