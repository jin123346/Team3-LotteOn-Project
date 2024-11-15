package com.lotteon.dto.page;

import com.lotteon.dto.admin.PageRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPageResponseDTO<T> {

    private List<T> dtoList;
    private String cate;
    private int pg;
    private int size;
    private int total;
    private int startNo;
    private int start, end;
    private boolean prev,next;

    private String type;
    private String keyword;

    private String grp;
    private String cateNo;

    @Builder
    public OrderPageResponseDTO(PageRequestDTO pageRequestDTO, List<T> dtoList, int total) {
        this.cate = pageRequestDTO.getCateNo();
        this.pg = pageRequestDTO.getPg();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;

        this.type = pageRequestDTO.getType();
        this.keyword = pageRequestDTO.getKeyword();

        this.grp = pageRequestDTO.getGrp();
        this.cateNo = pageRequestDTO.getCateNo();

        this.startNo = total - ((pg - 1) * size);
        this.end = (int) (Math.ceil(this.pg / 10.0)) * 10;
        this.start = this.end - 9;

        int last = (int) (Math.ceil(total / (double)size));
        this.end = end > last ? last : end;
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;
    }
}
