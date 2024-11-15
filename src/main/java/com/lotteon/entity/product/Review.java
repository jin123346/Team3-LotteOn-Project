package com.lotteon.entity.product;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lotteon.dto.product.ProductDTO;
import com.lotteon.dto.product.ReviewDTO;
import com.lotteon.dto.product.ReviewFileDTO;
import com.lotteon.entity.User.User;
import com.lotteon.entity.order.OrderItem;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="review")
public class Review {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer") // User 엔티티와 연결
    private User writer; // User 객체로 변경

    private LocalDateTime rdate;
    private String title;
    private String content;
    private double rating;  //상품평점

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonBackReference // 순환 참조 방지를 위해 사용
    private List<ReviewFile> pReviewFiles = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonBackReference // 순환 참조 방지를 위해 사용
    private Product product;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderItemId")
    @JsonBackReference
    private OrderItem orderItem;

    public ReviewDTO ToDTO(Review review) {
        ProductDTO productDTO = null;
        if (review.getProduct() != null) {
            productDTO = ProductDTO.builder()
                    .productId(review.getProduct().getProductId())
                    .categoryId(review.getProduct().getCategoryId())  // categoryId가 null이 아닌지 확인
                    .productName(review.getProduct().getProductName())
                    .build();
        }

        // Map ReviewFile to ReviewFileDTO
        List<ReviewFileDTO> reviewFileDTOS = review.getPReviewFiles().stream()
                .map(file -> new ReviewFileDTO(file.getFileId(), file.getSname(), file.getPath(),file.getFileId()))
                .collect(Collectors.toList());

        return ReviewDTO.builder()
                .reviewId(review.getReviewId())
                .writer(String.valueOf(review.getWriter()))
                .rdate(review.getRdate())
                .content(review.getContent())
                .rating(Double.parseDouble(String.valueOf(review.getRating())))
                .reviewFileDTOS(reviewFileDTOS) // Use ReviewFileDTO list
                .productId(review.getProduct() != null ? review.getProduct().getProductId() : null)
                .product(productDTO)
                .build();
    }

    @Override
    public String toString() {
        return "Review{" +
                "reviewId=" + reviewId +
                ", writer='" + writer + '\'' +
                ", rdate=" + rdate +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", rating='" + rating + '\'' +
                // Exclude pReviewFiles and product to avoid recursion
                '}';
    }
}
