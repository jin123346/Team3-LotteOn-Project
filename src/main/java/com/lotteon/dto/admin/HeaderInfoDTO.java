package com.lotteon.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HeaderInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long hd_id;

    private String hd_title;
    private String hd_subtitle;
    private String hd_sName1;
    private String hd_sName2;
    private String hd_sName3;
    private String hd_oName1;
    private String hd_oName2;
    private String hd_oName3;

    private MultipartFile file1;
    private MultipartFile file2;
    private MultipartFile file3;


}
