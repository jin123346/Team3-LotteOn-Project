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
public class VersionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private int verId;

    private String ver_name;
    private String ver_writer;

    private String rdate;

    private String ver_content;
}
