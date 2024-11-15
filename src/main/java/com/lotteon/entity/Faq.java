package com.lotteon.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Table
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Faq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int faqNo;

    private String faqtitle;
    private String faqcontent;
    private int faqhit;

    @CreationTimestamp
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private BoardCate cate;

}
