package com.lotteon.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "Notice")
@Entity
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeNo;

    private String noticetitle;

    @Enumerated(EnumType.STRING)  // Enum을 문자열로 저장
    private NoticeType noticetype;

    private String noticecontent;

    @CreationTimestamp
    private LocalDateTime date;

    private int noticehit;

}
