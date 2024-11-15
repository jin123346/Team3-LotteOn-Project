package com.lotteon.dto.product;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "pg")
public class ProductListPageResponseDTO {

    @JsonManagedReference
    private List<ProductDTO> productDTOs;
    @JsonManagedReference
    private List<ProductSummaryDTO> productSummaryDTOs;
    private int pg;
    private int size;
    private String sellerId;
    private long total;
    private int startNo;
    private int start, end;
    private boolean prev,next;
    private String type;
    private String keyword;


    @Builder
    public ProductListPageResponseDTO(PageRequestDTO pageRequestDTO, List<ProductSummaryDTO> productSummaryDTOs, List<ProductDTO> ProductDTOs, long total) {

        this.pg = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.type=pageRequestDTO.getType();
        this.keyword = pageRequestDTO.getKeyword();

        // `ProductDTOs`가 `null`일 경우 빈 리스트로 초기화
        this.productDTOs = (ProductDTOs == null || ProductDTOs.isEmpty()) ? new ArrayList<>() : ProductDTOs;

        // `productSummaryDTOs`가 비어 있지 않으면 초기화
        if (productSummaryDTOs != null && !productSummaryDTOs.isEmpty()) {
            this.productSummaryDTOs = productSummaryDTOs;
        } else {
            this.productSummaryDTOs = new ArrayList<>();
        }

//        this.startNo = total - ((pg - 1) * size);
//        this.end = (int) (Math.ceil(this.pg / (double) size)) * size;
//        this.start = this.end - size - 1;
//
//        int last = (int) (Math.ceil(total / (double) size)) * size;
//
//        this.end = Math.min(end, last);
//        if (this.start > this.end) {
//            this.end = this.start;
//        }
//
//        this.prev = this.start > 1;
//        this.next = total > this.end * this.size;



        this.startNo = (int) (total - ((pg - 1) * size)); //첫번째 글 번호
        this.end = (int) (Math.ceil(this.pg / 10.0))* 10; //마지막 페이지 번호
        this.start = this.end - 9; //첫번째 페이지 번호

        int last = (int) (Math.ceil(total / (double)size)); // 필요한 페이지 갯수
        this.end = end > last ? last : end; // 마지막 글번호가 필요한 페이지 갯수보다 크면 필요한 페이지 갯수로 세팅
        this.prev = this.start > 1; // 1보다 크면 이전버튼
        this.next = total > this.end * this.size; //마지막 페이지번호보다 글갯수가 더 클때 다음 버튼
    }
}
