package com.lotteon.service.admin;


import com.lotteon.dto.NoticeDTO;
import com.lotteon.dto.page.FaqPageResponseDTO;
import com.lotteon.dto.page.NoticePageResponseDTO;
import com.lotteon.dto.page.PageRequestDTO;
import com.lotteon.entity.Notice;
import com.lotteon.entity.NoticeType;
import com.lotteon.repository.admin.NoticeRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    private NoticeRepository noticeRepositoryy;

    public List<Notice> getAllNotices() {
        return noticeRepository.findAll();
    }

    public Optional<Notice> getNoticeById(Long no) {
        return noticeRepository.findById(no);
    }


    // 전체 공지사항을 페이지 형태로 가져오는 메서드 추가
    public Page<Notice> getNotices(Pageable pageable) {
        // 정렬된 Pageable 객체를 전달하여 최신순으로 데이터 가져오기
        Pageable sortedByDateDesc = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "date"));
        return noticeRepository.findAllByOrderByDateDesc(sortedByDateDesc);
    }

    // 전체 공지사항을 페이지 형태로 가져오는 메서드 추가
    public Page<Notice> getNoticesTop5() {
        // 정렬된 Pageable 객체를 전달하여 최신순으로 데이터 가져오기
        Pageable sortedByDateDesc = PageRequest.of(0,5, Sort.by(Sort.Direction.DESC, "date"));
        return noticeRepository.findAllByOrderByDateDesc(sortedByDateDesc);
    }

    // 최신 공지사항 5개를 가져오는 메서드 추가
    public List<NoticeDTO> getTop5Notices() {
        Pageable topFive = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "date"));
        return noticeRepository.findAll(topFive).getContent().stream()
                .map(notice -> modelMapper.map(notice, NoticeDTO.class)) // 엔티티를 DTO로 변환
                .collect(Collectors.toList());
    }

    //등록
    public Notice insertNotice(NoticeDTO noticeDTO) {
        return noticeRepository.save(modelMapper.map(noticeDTO, Notice.class));
    }

    //전체글목록조회
    public List<NoticeDTO> selectAllNotice() {
        List<Notice> noticeList = noticeRepository.findAll();
        return noticeList.stream()
                .map(notice -> modelMapper.map(notice, NoticeDTO.class)) // 엔티티를 DTO로 변환
                .collect(Collectors.toList());

    }

    //체크된 글 삭제
    public void deleteCheck(List<Long> data) {
        for (Long id : data) {
            noticeRepository.deleteById(id);
        }

    }

    //단일 삭제
    public void deleteNotice(Long no) {
        Optional<Notice> optNotice = noticeRepository.findById(no);
        if (optNotice.isPresent()) {
            Notice notice = optNotice.get();
            noticeRepository.delete(notice);
        }
    }

    //글보기
    public NoticeDTO selectNotice(Long no) {
        Optional<Notice> notice = noticeRepository.findById(no);
        if (notice.isPresent()) {
            NoticeDTO noticeDTO = modelMapper.map(notice.get(), NoticeDTO.class);
            return noticeDTO;
        }
        return null;
    }

    //글 수정
    public Notice UpdateNotice(NoticeDTO noticeDTO) {
        Optional<Notice> notice = noticeRepository.findById(noticeDTO.getNoticeNo());
        log.info("notice :" + notice);
        if (notice.isPresent()) {
            Notice notice1 = notice.get();

            if (noticeDTO.getNoticetype() == null) {
                notice1.setNoticetype(notice1.getNoticetype());
            } else {
                notice1.setNoticetype(noticeDTO.getNoticetype());
            }
            notice1.setNoticetitle(noticeDTO.getNoticetitle());
            notice1.setNoticecontent(noticeDTO.getNoticecontent());
            return noticeRepository.save(notice1);


        }
        return null;
    }

    //페이징 1
    public NoticePageResponseDTO selectNoticeListAll(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("no");
        Page<Tuple> pagenotice = null;
        if (pageRequestDTO.getNoticeType() == null) {
            pagenotice = noticeRepository.selectNoticeAllForList(pageRequestDTO, pageable);
        } else {
            pagenotice = noticeRepository.selectNoticeTypeList(pageRequestDTO, pageable);
        }

        List<NoticeDTO> noticeList = pagenotice.getContent().stream().map(tuple -> {
            Long id = tuple.get(0, Long.class);
            Notice notice = noticeRepository.findById(id).orElseThrow(() -> new RuntimeException("Notice not fonund with ID : " + id));
            NoticeDTO noticeDTO = modelMapper.map(notice, NoticeDTO.class);
            return noticeDTO;
        }).toList();

        int total = (int) pagenotice.getTotalElements();

        return NoticePageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .noticedtoList(noticeList)
                .total(total)
                .build();
    }

    public Page<Notice> getNoticesByType(NoticeType noticeType, Pageable pageable) {
        // 해당 noticetype에 맞는 공지사항을 가져옴
        return noticeRepository.findAllByNoticetype(noticeType, pageable);
    }

}
