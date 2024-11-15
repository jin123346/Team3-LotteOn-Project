package com.lotteon.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@ToString
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "headerInfo")
public class HeaderInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int hd_id;

    private String hd_title;
    private String hd_subtitle;
    private String hd_sName1;
    private String hd_sName2;
    private String hd_sName3;
    private String hd_oName1;
    private String hd_oName2;
    private String hd_oName3;



}
