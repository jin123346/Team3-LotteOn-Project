package com.lotteon.dto.page;

import com.lotteon.dto.NoticeDTO;
import com.lotteon.entity.NoticeType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NoticePageResponseDTO {

    private List<NoticeDTO> noticedtoList;

    private int pg;
    private int size;
    private int total;
    private int startNo;
    private int start, end;
    private boolean prev, next;

    private String noticeType;

//    private String keyword;

    @Builder
    public NoticePageResponseDTO(PageRequestDTO pageRequestDTO, List<NoticeDTO> noticedtoList, int total) {
        this.pg = pageRequestDTO.getPg();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.noticedtoList = noticedtoList;
//        this.keyword = pageRequestDTO.getKeyword();
        this.noticeType = pageRequestDTO.getNoticeType();
        this.startNo = total - ((pg - 1) * size); //첫번째 글 번호
        this.end = (int) (Math.ceil(this.pg / 10.0))* 10; //마지막 페이지 번호
        this.start = this.end - 9; //첫번째 페이지 번호

        int last = (int) (Math.ceil(total / (double)size)); //필요한 페이지 갯수
        this.end = end > last ? last : end; // 마지막 페이지번호가 필요한 페이지 갯수보다 크면 필요한 페이지 갯수로 세팅
        this.prev = this.start > 1; // 1보다 크면 이전버튼
        this.next = total > this.end * this.size; //마지막 페이지번호보다 글갯수가 더 클때 다음 버튼


    }
}
