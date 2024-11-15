package com.lotteon.dto;

import com.lotteon.entity.admin.Adminqna;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class adminQnaDTO {

    private int qnaNo;
    private BoardCateDTO category;
    private String qnatitle;
    private String qnacontent;
    private String date;
    private String qnawriter;
    private Adminqna.Status qna_status;
    private String qnareply;

    private Long categoryid;
}
