package com.lotteon.dto.page;

import com.lotteon.entity.User.Point;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PointPageResponseDTO {
    private List<Point> pointList;  // 포인트 목록
    private int pg;  // 현재 페이지
    private int size;  // 페이지 크기
    private long total;  // 전체 항목 수 (long으로 수정)

    private int startNo;  // 시작 번호 (페이지에 표시할 첫 번째 항목 번호)
    private int start;    // 시작 페이지 번호
    private int end;      // 끝 페이지 번호
    private boolean prev, next;  // 이전/다음 페이지 존재 여부

    private String type;  // 검색 타입 (예: 'order', 'point')
    private String keyword;  // 검색 키워드

    // 생성자에서 페이지 정보와 포인트 목록, 전체 항목 수 계산
    public PointPageResponseDTO(PointPageRequestDTO requestDTO, List<Point> pointList, long elements, int i, long totalElements) {
        this.pg = requestDTO.getPg();
        this.size = requestDTO.getSize();
        this.total = totalElements;
        this.pointList = pointList;

        // 시작 번호 계산: (현재 페이지 - 1) * 페이지 크기 + 1
        this.startNo = (this.pg - 1) * this.size + 1;

        // 페이지 범위 계산 (최소 1, 최대 총 페이지 수)
        this.end = (int) Math.ceil((double) totalElements / size); // 끝 페이지 계산
        this.start = Math.max(1, this.pg - 5); // 시작 페이지 번호 (현재 페이지 - 5, 최소 1)
        this.end = Math.min(this.start + 9, this.end); // 끝 페이지 번호 (시작 + 9, 최대 끝 페이지)

        // 이전/다음 페이지 여부 설정
        this.prev = this.pg > 1; // 이전 페이지 존재 여부
        this.next = this.pg < this.end; // 다음 페이지 존재 여부

        this.type = requestDTO.getType(); // 타입
        this.keyword = requestDTO.getKeyword(); // 검색 키워드
    }
}
