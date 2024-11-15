package com.lotteon.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProduct is a Querydsl query type for Product
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProduct extends EntityPathBase<Product> {

    private static final long serialVersionUID = 1825934975L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProduct product = new QProduct("product");

    public final NumberPath<Long> categoryFirstId = createNumber("categoryFirstId", Long.class);

    public final NumberPath<Long> categoryId = createNumber("categoryId", Long.class);

    public final NumberPath<Long> categorySecondId = createNumber("categorySecondId", Long.class);

    public final NumberPath<Integer> discount = createNumber("discount", Integer.class);

    public final StringPath file190 = createString("file190");

    public final StringPath file230 = createString("file230");

    public final StringPath file456 = createString("file456");

    public final SetPath<ProductFile, QProductFile> files = this.<ProductFile, QProductFile>createSet("files", ProductFile.class, QProductFile.class, PathInits.DIRECT2);

    public final NumberPath<Long> hit = createNumber("hit", Long.class);

    public final BooleanPath isCoupon = createBoolean("isCoupon");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final BooleanPath isSaled = createBoolean("isSaled");

    public final SetPath<ProductOptionCombination, QProductOptionCombination> optionCombinations = this.<ProductOptionCombination, QProductOptionCombination>createSet("optionCombinations", ProductOptionCombination.class, QProductOptionCombination.class, PathInits.DIRECT2);

    public final SetPath<OptionGroup, QOptionGroup> optionGroups = this.<OptionGroup, QOptionGroup>createSet("optionGroups", OptionGroup.class, QOptionGroup.class, PathInits.DIRECT2);

    public final ListPath<Option, QOption> options = this.<Option, QOption>createList("options", Option.class, QOption.class, PathInits.DIRECT2);

    public final NumberPath<Integer> point = createNumber("point", Integer.class);

    public final NumberPath<Long> price = createNumber("price", Long.class);

    public final StringPath productCode = createString("productCode");

    public final StringPath productDesc = createString("productDesc");

    public final QProductDetails productDetails;

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    public final StringPath productName = createString("productName");

    public final NumberPath<Double> productRating = createNumber("productRating", Double.class);

    public final DateTimePath<java.time.LocalDateTime> rdate = createDateTime("rdate", java.time.LocalDateTime.class);

    public final ListPath<Review, QReview> reviews = this.<Review, QReview>createList("reviews", Review.class, QReview.class, PathInits.DIRECT2);

    public final StringPath savedPath = createString("savedPath");

    public final StringPath sellerId = createString("sellerId");

    public final NumberPath<Long> sellerNo = createNumber("sellerNo", Long.class);

    public final NumberPath<Integer> shippingFee = createNumber("shippingFee", Integer.class);

    public final NumberPath<Integer> shippingTerms = createNumber("shippingTerms", Integer.class);

    public final NumberPath<Long> sold = createNumber("sold", Long.class);

    public final NumberPath<Long> stock = createNumber("stock", Long.class);

    public QProduct(String variable) {
        this(Product.class, forVariable(variable), INITS);
    }

    public QProduct(Path<? extends Product> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProduct(PathMetadata metadata, PathInits inits) {
        this(Product.class, metadata, inits);
    }

    public QProduct(Class<? extends Product> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.productDetails = inits.isInitialized("productDetails") ? new QProductDetails(forProperty("productDetails")) : null;
    }

}

