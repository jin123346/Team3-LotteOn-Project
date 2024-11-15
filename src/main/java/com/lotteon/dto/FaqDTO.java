package com.lotteon.dto;

import com.lotteon.entity.BoardCate;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FaqDTO {

    private int faqNo;
    private BoardCateDTO category;
    private String faqtitle;
    private String faqcontent;
    private int faqhit;
    private String date;

    private Long categoryid;



}

