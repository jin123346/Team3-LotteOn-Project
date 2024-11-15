package com.lotteon.entity.admin;

import com.lotteon.dto.admin.TermsDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "terms")
public class Terms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String title;  // 약관 제목

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String content;  // 약관 내용

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String htmlContent; // 변환된 HTML 내용

    @Column(length = 50)
    private String type;  // 약관 종류 필드

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();  // 생성 일자

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();  // 수정 일자

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum TermsType {
        BUYER,             // 구매 회원 약관
        SELLER,            // 판매 회원 약관
        ELECTRONIC_FINANCE, // 전자 금융거래 약관
        LOCATION_INFO,     // 위치정보 이용약관
        PRIVACY_POLICY     // 개인정보 처리방침
    }

    public TermsDto convertToDto(Terms terms) {
        TermsDto dto = new TermsDto();
        dto.setId(terms.getId());
        dto.setTitle(terms.getTitle());
        dto.setContent(terms.getContent());
        dto.setHtmlContent(terms.getHtmlContent());
        dto.setType(terms.getType());
        return dto;
    }

}
