package com.lotteon.dto.admin;

import lombok.*;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CouponIssuedRequestDTO {

    private String issuedNumber;
    private String usageStatus;
    private String status;

}
