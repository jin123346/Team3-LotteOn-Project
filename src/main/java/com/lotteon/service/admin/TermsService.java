package com.lotteon.service.admin;

import com.lotteon.dto.admin.TermsDto;
import com.lotteon.entity.admin.Terms;
import com.lotteon.repository.admin.TermsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class TermsService {

    private final TermsRepository termsRepository;

    public TermsDto convertToDto(Terms terms) {
        TermsDto dto = new TermsDto();
        dto.setId(terms.getId());
        dto.setTitle(terms.getTitle());
        dto.setContent(terms.getContent());
        dto.setType(terms.getType());
        return dto;
    }

    // 모든 약관을 조회하여 반환하는 메서드
    public List<Terms> findAllTerms() {
        List<Terms> allTerms = termsRepository.findAll();
//        System.out.println("Fetched Terms from DB: " + allTerms); // DB에서 가져온 데이터 출력
        return allTerms;
    }
    public List<Terms> getFilteredTerms(String userType) {
        List<Terms> allTerms = termsRepository.findAll(); // 모든 약관 가져오기

        log.info("allTerms: {}", allTerms);
        System.out.println("allTerms: " + allTerms);

        // 사용자 타입에 따라 필터링
        if ("member".equalsIgnoreCase(userType)) {
            return allTerms.stream()
                    .filter(terms -> List.of(1L, 3L, 4L, 5L).contains(terms.getId())) // ID 1, 3, 4, 5
                    .collect(Collectors.toList());
        } else if ("seller".equalsIgnoreCase(userType)) {
            return allTerms.stream()
                    .filter(terms -> List.of(2L, 3L, 4L).contains(terms.getId())) // ID 2, 3, 4
                    .collect(Collectors.toList());
        }
        return List.of(); // 빈 리스트 반환
    }

    // 특정 타입의 약관 조회 메서드
    public List<TermsDto> findTermsByType(List<String> types) {
        // 타입에 맞는 약관을 조회
        List<Terms> termsList = termsRepository.findByTypeIn(types);

        // DTO로 변환하여 반환
        return termsList.stream()
                .map(this::convertToDto) // Terms -> TermsDto 변환
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateTerms(TermsDto termsDto) {
        Terms terms = termsRepository.findById(termsDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 약관이 존재하지 않습니다."));

        // 엔티티의 필드 업데이트
        terms.setContent(termsDto.getContent());
        // 필요한 경우 다른 필드도 업데이트
        termsRepository.save(terms);
    }

    @Transactional
    public void updateTermsContent(TermsDto termsDto) {
        Optional<Terms> optionalTerms = termsRepository.findById(termsDto.getId());
        if (optionalTerms.isPresent()) {
            Terms terms = optionalTerms.get();  // Optional 객체에서 값 추출
            terms.setContent(termsDto.getContent());  // 내용만 업데이트
            termsRepository.save(terms);  // 변경된 엔티티 저장
        } else {
            throw new IllegalArgumentException("해당 ID의 약관이 존재하지 않습니다.");
        }
    }

    public List<Terms> findAllTermsByType(String type) {

        return termsRepository.findByType(type);
    }

    public List<Terms> getAllTerms() {
        return termsRepository.findAll();
    }

    public String getTermsDetails(Long termsId) {
        String termsDetail = termsRepository.findById(termsId)
                .map(Terms::getContent) // Terms 엔티티에서 내용 가져오기
                .orElse(null);

        // 여기서 변환 처리
        if (termsDetail != null) {
            termsDetail = termsDetail.replaceAll("제(\\d+)조 \\((.*?)\\)", "<h2>제$1조 ($2)</h2>");
            termsDetail = termsDetail.replaceAll("제\\s*(\\d+)장\\s*(\\((.*?)\\))?\\s*(.*)", "<h3>제 $1장$2 $4</h3>");
            termsDetail = termsDetail.replaceAll("(?<=</h2>|</h3>)(\\n|\\r)?", "<p></p>");
            termsDetail = termsDetail.replace("\n", "<br>");
            termsDetail = termsDetail.replaceAll("(?<=</p>)(\\n|\\r)?", "<p></p>");
        }
        return termsDetail;
    }
}
