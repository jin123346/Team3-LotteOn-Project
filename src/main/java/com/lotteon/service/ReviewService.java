package com.lotteon.service;

import com.lotteon.dto.User.UserDTO;
import com.lotteon.dto.admin.PageRequestDTO;
import com.lotteon.dto.admin.PageResponseDTO;
import com.lotteon.dto.order.OrderItemDTO;
import com.lotteon.dto.product.ReviewDTO;
import com.lotteon.dto.product.ReviewRequestDTO;
import com.lotteon.entity.User.User;
import com.lotteon.entity.order.OrderItem;
import com.lotteon.entity.product.Product;
import com.lotteon.entity.product.Review;
import com.lotteon.entity.product.ReviewFile;
import com.lotteon.repository.ReviewRepository;
import com.lotteon.repository.order.OrderItemRepository;
import com.lotteon.repository.product.ProductRepository;
import com.lotteon.repository.user.UserRepository;
import groovy.util.logging.Log4j2;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Log4j2
@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final FileService fileService;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;

    @PostConstruct
    public void init() {
//        modelMapper.addMappings(new PropertyMap<Review, ReviewDTO>() {
//            @Override
//            protected void configure() {
//                skip(destination.getPReviewFiles()); // pReviewFiles 필드를 무시
//                skip(destination.getSavedReviewFiles()); // savedReviewFiles도 무시
//                // 필요한 다른 매핑 설정 추가
//            }
//        });
    }

    public boolean saveReview(ReviewRequestDTO reviewDTO) {
        Long productId = reviewDTO.getProductId(); // ReviewDTO에서 productId 가져오기
        Product product = productRepository.findById(productId).orElse(null);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName(); // 현재 로그인된 사용자 이름

        // 사용자 정보 가져오기
        User currentUser = userRepository.findById(currentUsername).orElse(null);


        if (product == null || currentUser == null) {
            return false; // Product가 없거나 User가 없을 경우 저장 중단
        }

        Review review = new Review();
        review.setProduct(product);
        review.setRating(Double.parseDouble(reviewDTO.getRating())); // DTO에서 rating 가져오기
        review.setContent(reviewDTO.getContent()); // DTO에서 content 가져오기
        review.setWriter(currentUser); // User 객체를 writer에 설정
        review.setRdate(LocalDateTime.now());

        // 파일 업로드 로직 호출
        ReviewRequestDTO uploadedReviewDTO = fileService.uploadReviewFiles(reviewDTO);

        // 업로드된 파일 정보를 Review 엔티티에 설정
        List<ReviewFile> reviewFiles = uploadedReviewDTO.getSavedReviewFiles();

        // 리뷰 파일을 Review 객체에 추가
        for (ReviewFile reviewFile : reviewFiles) {
            reviewFile.setReview(review); // Review와의 관계 설정
        }
        review.setPReviewFiles(reviewFiles); // 수정: reviewFiles에 올바르게 설정

        // DB에 Review 저장
        reviewRepository.save(review);

        return true;
    }
    public List<Review> getRecentReviewsByUser(String uid) {
        return reviewRepository. findTop3ByWriter_UidOrderByRdateDesc(uid);
    }


    public List<Review> getAllReviews() {
        return reviewRepository.findAll(Sort.by(Sort.Direction.DESC, "rdate")); // 최신 순으로 정렬
    }

    public PageResponseDTO<ReviewDTO> getReviewsByUser(PageRequestDTO pageRequestDTO, String uid) {
        Pageable pageable = pageRequestDTO.getPageable("reviewId");

        Page<Review> pageReview = reviewRepository.findByWriter_Uid(uid, pageable);
        if (pageReview.getContent().isEmpty()) {
            // 리뷰가 없다면 적절한 처리 (예: "리뷰 없음" 메시지)
        }

        List<ReviewDTO> reviewList = pageReview.stream()
                .map(review -> {
                    ReviewDTO reviewDTO = review.ToDTO(review); // DTO 변환
                    reviewDTO.setWriter(reviewDTO.getMaskedWriter()); // 마스킹된 아이디 설정
                    return reviewDTO;
                })
                .collect(Collectors.toList());

        int total = (int) pageReview.getTotalElements();

        return PageResponseDTO.<ReviewDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(reviewList)
                .total(total)
                .build();
    }

    public PageResponseDTO<ReviewDTO> getAllReviewsss(PageRequestDTO pageRequestDTO, Long productId) {
        // `productId`를 `type` 필드로 설정
        pageRequestDTO.setType(String.valueOf(productId));

        Pageable pageable = pageRequestDTO.getPageable("no");
        Page<Review> pageReview = reviewRepository.selectReviewAllForList(pageRequestDTO, pageable);

        // 리뷰 엔티티를 DTO로 변환 및 마스킹 처리
        List<ReviewDTO> reviewList = pageReview.stream()
                .map(review -> {
                    ReviewDTO reviewDTO = review.ToDTO(review);
                    reviewDTO.setWriter(reviewDTO.getMaskedWriter());

                    // 이미지 파일 리스트가 null인 경우 빈 리스트로 초기화
                    if (reviewDTO.getSavedReviewFiles() == null) {
                        reviewDTO.setSavedReviewFiles(Collections.emptyList());
                    }

                    return reviewDTO;
                })
                .collect(Collectors.toList());

        log.info("페이지 리뷰 수: {}", pageReview.getTotalElements());

        log.info("리뷰 리스트 크기: {}", reviewList.size());
        log.info("리뷰 리스트 내용: {}", reviewList);

        int total = (int) pageReview.getTotalElements();

        return PageResponseDTO.<ReviewDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(reviewList)
                .total(total)
                .build();
    }

    public List<Review> getAllReviewsByProductId(Long productId) {
        return reviewRepository.findAllByProduct_ProductId(productId);
    }

}
