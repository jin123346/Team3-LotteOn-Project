package com.lotteon.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReview is a Querydsl query type for Review
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReview extends EntityPathBase<Review> {

    private static final long serialVersionUID = 658555848L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReview review = new QReview("review");

    public final StringPath content = createString("content");

    public final com.lotteon.entity.order.QOrderItem orderItem;

    public final ListPath<ReviewFile, QReviewFile> pReviewFiles = this.<ReviewFile, QReviewFile>createList("pReviewFiles", ReviewFile.class, QReviewFile.class, PathInits.DIRECT2);

    public final QProduct product;

    public final NumberPath<Double> rating = createNumber("rating", Double.class);

    public final DateTimePath<java.time.LocalDateTime> rdate = createDateTime("rdate", java.time.LocalDateTime.class);

    public final NumberPath<Long> reviewId = createNumber("reviewId", Long.class);

    public final StringPath title = createString("title");

    public final com.lotteon.entity.User.QUser writer;

    public QReview(String variable) {
        this(Review.class, forVariable(variable), INITS);
    }

    public QReview(Path<? extends Review> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReview(PathMetadata metadata, PathInits inits) {
        this(Review.class, metadata, inits);
    }

    public QReview(Class<? extends Review> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.orderItem = inits.isInitialized("orderItem") ? new com.lotteon.entity.order.QOrderItem(forProperty("orderItem"), inits.get("orderItem")) : null;
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product"), inits.get("product")) : null;
        this.writer = inits.isInitialized("writer") ? new com.lotteon.entity.User.QUser(forProperty("writer"), inits.get("writer")) : null;
    }

}

