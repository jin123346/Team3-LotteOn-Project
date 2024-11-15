package com.lotteon.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@AllArgsConstructor
@ToString
@Builder
@Setter
@NoArgsConstructor
@Entity
@Table(name="Banner")
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ban_id;

    private String ban_name;
    private String ban_size;
    private String ban_image;
    private String ban_link;
    private String ban_location;
    private String ban_oname;

    private String ban_sdate;
    private String ban_edate;
    private String ban_stime;
    private String ban_etime;

    private String ban_color;

    //status가 0이면 활성화 1이면 비활성화
    private int status;


//    @OneToMany(mappedBy = "bno")
//    private List<FileEntity> fileList;


}
