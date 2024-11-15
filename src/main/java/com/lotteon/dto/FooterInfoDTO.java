package com.lotteon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FooterInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long ft_id;

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
