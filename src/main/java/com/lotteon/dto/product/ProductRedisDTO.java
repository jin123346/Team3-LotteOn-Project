package com.lotteon.dto.product;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProductRedisDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    private long productId;
    private String productName;
    private long categoryId;
    private long originalPrice;
    private long discount;
    private long finalPrice;
    private String file230;
    private String savedPath;
}
