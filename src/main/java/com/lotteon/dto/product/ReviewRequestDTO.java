package com.lotteon.dto.product;


import com.lotteon.dto.order.OrderItemDTO;
import com.lotteon.entity.product.ReviewFile;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ReviewRequestDTO {

    private long reviewId;
    private String writer;
    private LocalDateTime rdate;
    private String title;
    private String content;
    private String rating;  //상품평점
    private List<MultipartFile> pReviewFiles;
    private List<ReviewFile> savedReviewFiles;
    private List<ReviewFileDTO> reviewFileDTOS;
    private Long productId;
    private ProductDTO product; // Product 정보를 포함
    private List<OrderItemDTO> orderItems = new ArrayList<>();



}
