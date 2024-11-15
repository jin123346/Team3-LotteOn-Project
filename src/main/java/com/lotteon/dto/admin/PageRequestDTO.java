package com.lotteon.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageRequestDTO {

    @Builder.Default
    private int no = 1;

    @Builder.Default
    private int pg = 1;

    @Builder.Default
    private int size = 10;

    private String type;
    private String keyword;
    private String sortType;

    private String grp;
    private String cateNo;


    public Pageable getPageable(String sort) {
        return PageRequest.of(this.pg -1, this.size, Sort.by(sort).descending());
    }

}
