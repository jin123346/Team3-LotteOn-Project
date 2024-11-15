package com.lotteon.entity.admin;


import com.lotteon.entity.BoardCate;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Table (name = "adminqna")
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Adminqna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int qnaNo;

    private String qnatitle;
    private String qnacontent;
    private String qnawriter;
    private String qnareply;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Status qna_status = Status.review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private BoardCate cate;

    @CreationTimestamp
    private LocalDateTime date;

    private String iscompleted;
    private int sellerid;
    private int productid;


    public enum Status {
        review,     //검토중,
        completed  // 답변완료
    }
}
