package com.lotteon.dto.admin;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CouponListResponseDTO {
    private List<CouponDTO> couponDTOList; // 쿠폰 DTO 리스트
    private String type; // 요청 타입
    private int pg; // 현재 페이지
    private int size; // 페이지당 쿠폰 수
    private int total; // 전체 쿠폰 수
    private int startNo; // 시작 번호
    private int start, end; // 페이지 시작과 끝
    private boolean prev, next; // 이전 및 다음 페이지 여부

    @Builder
    public CouponListResponseDTO(CouponListRequestDTO pageRequestDTO, List<CouponDTO> couponDTOList, int total) {
        this.type = pageRequestDTO.getType();
        this.pg = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.couponDTOList = couponDTOList;

        // 시작 번호 계산
        this.startNo = total - ((pg - 1) * size);

        // 페이지 시작과 끝 번호 계산
        this.end = Math.min((int) (Math.ceil(pg / (double) size) * size), total);
        this.start = Math.max(1, end - (size - 1));

        // 이전 및 다음 페이지 여부
        this.prev = this.start > 1;
        this.next = total > end;
    }
}
