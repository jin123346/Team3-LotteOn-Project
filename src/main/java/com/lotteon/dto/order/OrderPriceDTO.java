package com.lotteon.dto.order;


import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Getter
@Setter
@ToString
@Log4j2
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderPriceDTO {

    private String sellerUid;
    private long totalOriginalPrice;
    private long totalFinalPrice;
    private long totalDiscount;
    private long totalShipping;

}
