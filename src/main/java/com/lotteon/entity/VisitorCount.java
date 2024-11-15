package com.lotteon.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "VisitorCount")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class VisitorCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long count; // 전체 방문자 수
    private Long yesterdayCount; // 어제 방문자 수
    private Long todayCount; // 오늘 방문자 수

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date lastUpdated;
}
