package com.lotteon.dto.page;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PointPageRequestDTO {
    @Builder.Default
    private int pg = 1; // 페이지 번호 (기본값 1)

    private String type; // 요청 타입 (예: ADMIN, SELLER 등)

    @Builder.Default
    private int size = 10; // 페이지 크기 (기본값 10)

    private String cate;  // 카테고리
    private String keyword;  // 검색 키워드
    private LocalDate startDate;  // 시작 날짜
    private LocalDate endDate;    // 종료 날짜

    // Pageable 객체로 변환
    public Pageable toPageable() {
        return PageRequest.of(this.pg -1, this.size);
    }
}
