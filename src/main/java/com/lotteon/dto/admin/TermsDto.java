package com.lotteon.dto.admin;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TermsDto {
    private Long id;      // 약관 ID
    private String title; // 약관 제목
    private String content; // 약관 내용
    private String htmlContent;
    private String type;  // 약관 종류

}


