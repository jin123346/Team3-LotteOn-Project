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
@Table(name = "footerinfo")
public class FooterInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ft_id;

    private String ft_company;
    private String ft_ceo;
    private String ft_bo;
    private String ft_mo;
    private String ft_addr1;
    private String ft_addr2;
    private String ft_hp;
    private String ft_time;
    private String ft_email;
    private String ft_troublehp;
    private String ft_copyright;

}
