package com.lotteon.dto;


import com.lotteon.entity.NoticeType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NoticeDTO {


    private Long noticeNo;
    private String noticetitle;
    private NoticeType noticetype;
    private String noticecontent;
    private String date;
    private int noticehit;
}


