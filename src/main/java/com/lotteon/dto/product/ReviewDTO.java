package com.lotteon.dto.product;


import com.lotteon.entity.product.Product;
import com.lotteon.entity.product.ReviewFile;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ReviewDTO {

    private long reviewId;
    private String writer;
    private LocalDateTime rdate;
    private String title;
    private String content;
    private double rating;  //상품평점
    private List<ReviewFile> savedReviewFiles;
    private List<ReviewFileDTO> reviewFileDTOS;
    private Long productId;
    private ProductDTO product; // Product 정보를 포함

    // 리뷰 작성자의 아이디를 마스킹하는 메소드
    public String getMaskedWriter() {
        return maskId(writer);
    }

    // 아이디 마스킹 로직
    private String maskId(String userId) {
        if (userId == null || userId.length() < 3) {
            return userId; // 아이디가 너무 짧으면 그대로 반환
        }
        StringBuilder maskedId = new StringBuilder();
        maskedId.append(userId.substring(0, 3)); // 처음 3글자
        for (int i = 0; i < userId.length() - 3; i++) {
            maskedId.append("*"); // 나머지 글자는 *
        }
        return maskedId.toString();
    }




}
