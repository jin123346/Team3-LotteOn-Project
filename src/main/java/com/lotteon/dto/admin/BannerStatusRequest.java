package com.lotteon.dto.admin;


import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BannerStatusRequest {
    private int id;
    private String status;


}
