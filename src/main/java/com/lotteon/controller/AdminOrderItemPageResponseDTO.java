package com.lotteon.controller;

import com.lotteon.dto.admin.AdminOrderDTO;
import com.lotteon.dto.order.OrderItemDTO;
import com.lotteon.dto.page.PageRequestDTO;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class AdminOrderItemPageResponseDTO {

    private List<OrderItemDTO> adminorderitemdtoList;

    private int pg;
    private int size;
    private int total;
    private int startNo;
    private int start, end;
    private boolean prev, next;

    private String type;
    private String keyword;

    @Builder
    public AdminOrderItemPageResponseDTO(PageRequestDTO pageRequestDTO, List<OrderItemDTO> adminorderitemdtoList, int total) {
        this.pg = pageRequestDTO.getPg();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.adminorderitemdtoList = adminorderitemdtoList;

        this.type = pageRequestDTO.getType();
        this.keyword = pageRequestDTO.getKeyword();

        this.startNo = total - ((pg - 1) * size); //첫번째 글 번호
        this.end = (int) (Math.ceil(this.pg / 10.0))* 10; //마지막 페이지 번호
        this.start = this.end - 9; //첫번째 페이지 번호

        int last = (int) (Math.ceil(total / (double)size)); // 필요한 페이지 갯수
        this.end = end > last ? last : end; // 마지막 글번호가 필요한 페이지 갯수보다 크면 필요한 페이지 갯수로 세팅
        this.prev = this.start > 1; // 1보다 크면 이전버튼
        this.next = total > this.end * this.size; //마지막 페이지번호보다 글갯수가 더 클때 다음 버튼

    }
}

