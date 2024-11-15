package com.lotteon.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProductDetails is a Querydsl query type for ProductDetails
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductDetails extends EntityPathBase<ProductDetails> {

    private static final long serialVersionUID = -1724547389L;

    public static final QProductDetails productDetails = new QProductDetails("productDetails");

    public final StringPath afterSalseManager = createString("afterSalseManager");

    public final StringPath busniesstype = createString("busniesstype");

    public final NumberPath<Integer> detailId = createNumber("detailId", Integer.class);

    public final StringPath handlingPrecautions = createString("handlingPrecautions");

    public final StringPath manufactureCountry = createString("manufactureCountry");

    public final StringPath manufactureImporter = createString("manufactureImporter");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final StringPath Productcondition = createString("Productcondition");

    public final StringPath qualityWarranty = createString("qualityWarranty");

    public final StringPath receiptIssuance = createString("receiptIssuance");

    public final StringPath shippingType = createString("shippingType");

    public final StringPath tax = createString("tax");

    public QProductDetails(String variable) {
        super(ProductDetails.class, forVariable(variable));
    }

    public QProductDetails(Path<? extends ProductDetails> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProductDetails(PathMetadata metadata) {
        super(ProductDetails.class, metadata);
    }

}

