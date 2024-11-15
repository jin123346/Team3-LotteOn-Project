package com.lotteon.service;

import com.lotteon.dto.BoardCateDTO;
import com.lotteon.dto.QnaDTO; // DTO import
import com.lotteon.dto.adminQnaDTO;
import com.lotteon.entity.BoardCate;
import com.lotteon.entity.QnA; // Entity import
import com.lotteon.entity.admin.Adminqna;
import com.lotteon.repository.BoardRepository;
import com.lotteon.repository.QnaRepository; // Repository import
import com.lotteon.repository.admin.AdminQnaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CsService {

    private final QnaRepository qnaRepository; // 리포지토리 주입
    private final ModelMapper getModelMapper;
    private final BoardRepository boardRepository;
    private final AdminQnaRepository adminQnaRepository;

    // QnA 글 등록 메서드 (fetch로 변경시 void -> adminQna)
    public void writeQnA(adminQnaDTO adminqnaDTO) {

        BoardCate selectCate = boardRepository.findByBoardCateId(adminqnaDTO.getCategoryid());
        BoardCateDTO selectCated = getModelMapper.map(selectCate, BoardCateDTO.class);

        adminqnaDTO.setCategory(selectCated);
        Adminqna adminqna = new Adminqna();
        adminqna.setCate(selectCate);
        adminqna.setQnatitle(adminqnaDTO.getQnatitle());
        adminqna.setQnacontent(adminqnaDTO.getQnacontent());
        adminqna.setQnawriter(adminqnaDTO.getQnawriter());

        adminQnaRepository.save(adminqna);
//        // DTO에서 엔티티로 변환
//        QnA qna = QnA.builder()
//                .qna_type1(qnaDTO.getQna_type1())
//                .qna_type2(qnaDTO.getQna_type2())
//                .qna_title(qnaDTO.getQna_title())
//                .qna_writer(qnaDTO.getQna_writer()) // 현재 사용자 이름 설정
//                .qna_content(qnaDTO.getQna_content()) // 내용 추가
//                .iscompleted("N") // 기본값 설정
//                .build();
//
//        // 데이터베이스에 저장
//        qnaRepository.save(qna);
    }

    public Page<QnaDTO> getQnaWriter(String writer, Pageable pageable) {
        Page<QnA> qnas = qnaRepository.findByQnaWriter(writer, pageable);
        return qnas.map(qnA -> {
            QnaDTO dto = getModelMapper.map(qnA, QnaDTO.class);
            dto.setQna_status(qnA.getQna_status()); // ENUM을 직접 설정
            return dto;
        });
    }

    public Page<QnaDTO> getAllQnA(Pageable pageable) {
        // pageable에 최신 QnA 5개를 가져오도록 설정
        Pageable sortedByDate = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "rdate"));
        return qnaRepository.findAll(sortedByDate).map(qnA -> getModelMapper.map(qnA, QnaDTO.class));
    }


    public List<QnaDTO> getTop5QnAs() {
        Pageable topFive = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "rdate"));
        Page<QnA> qnas = qnaRepository.findAll(topFive);
        return qnas.getContent().stream()
                .map(qnA -> getModelMapper.map(qnA, QnaDTO.class))
                .collect(Collectors.toList());
    }

}