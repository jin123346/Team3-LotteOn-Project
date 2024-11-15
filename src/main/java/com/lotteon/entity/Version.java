package com.lotteon.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "version")
public class Version {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ver_id")
    private int verId; // ver_id를 verId로 변경

    private String ver_name;
    private String ver_writer;

    @CreationTimestamp
    private LocalDateTime rdate;

    private String ver_content;

}
