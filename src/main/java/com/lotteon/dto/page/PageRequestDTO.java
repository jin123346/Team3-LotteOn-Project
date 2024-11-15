package com.lotteon.dto.page;

import com.lotteon.entity.NoticeType;
import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    @Builder.Default
    private int no = 1;

    @Builder.Default
    private int pg = 1;

    @Builder.Default
    private int size = 10;

    private String type;
    private String keyword;

    private String noticeType;

    private Long parentId;

    private Long childId;

    private String qnawriter;

    public Pageable getPageable(String sort) {
        return PageRequest.of(this.pg - 1, this.size, Sort.by(sort).descending()); // 페이지네이션 정렬정보 담고 있음
    }
}
