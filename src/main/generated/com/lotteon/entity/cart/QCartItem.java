package com.lotteon.entity.cart;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCartItem is a Querydsl query type for CartItem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCartItem extends EntityPathBase<CartItem> {

    private static final long serialVersionUID = 1180173552L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCartItem cartItem = new QCartItem("cartItem");

    public final NumberPath<Long> calcShippingCost = createNumber("calcShippingCost", Long.class);

    public final QCart cart;

    public final NumberPath<Long> cartItemId = createNumber("cartItemId", Long.class);

    public final NumberPath<Long> deliveryFee = createNumber("deliveryFee", Long.class);

    public final NumberPath<Integer> discount = createNumber("discount", Integer.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final com.lotteon.entity.product.QOption option;

    public final NumberPath<Long> optionGroupId = createNumber("optionGroupId", Long.class);

    public final StringPath optionName = createString("optionName");

    public final NumberPath<Integer> points = createNumber("points", Integer.class);

    public final NumberPath<Long> price = createNumber("price", Long.class);

    public final com.lotteon.entity.product.QProduct product;

    public final StringPath productName = createString("productName");

    public final com.lotteon.entity.product.QProductOptionCombination productOptionCombination;

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public final NumberPath<Long> totalPrice = createNumber("totalPrice", Long.class);

    public QCartItem(String variable) {
        this(CartItem.class, forVariable(variable), INITS);
    }

    public QCartItem(Path<? extends CartItem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCartItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCartItem(PathMetadata metadata, PathInits inits) {
        this(CartItem.class, metadata, inits);
    }

    public QCartItem(Class<? extends CartItem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cart = inits.isInitialized("cart") ? new QCart(forProperty("cart"), inits.get("cart")) : null;
        this.option = inits.isInitialized("option") ? new com.lotteon.entity.product.QOption(forProperty("option")) : null;
        this.product = inits.isInitialized("product") ? new com.lotteon.entity.product.QProduct(forProperty("product"), inits.get("product")) : null;
        this.productOptionCombination = inits.isInitialized("productOptionCombination") ? new com.lotteon.entity.product.QProductOptionCombination(forProperty("productOptionCombination"), inits.get("productOptionCombination")) : null;
    }

}

