package com.lotteon.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
public class BoardCateDTO {

    private Long boardCateId;
    private String name;
    private int level;
    private BoardCateDTO parent;
    private BoardCateDTO child;

}
