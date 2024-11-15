package com.lotteon.dto.admin;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BannerDTO {

    private int ban_id;

    private String ban_name;
    private String ban_size;
    private String ban_image;
    private String ban_link;
    private String ban_location;
    private String ban_oname;

    private String ban_type;

    private String ban_sdate;
    private String ban_edate;
    private String ban_stime;
    private String ban_etime;

    private String ban_color;
    private int status;

    private MultipartFile file;
}
