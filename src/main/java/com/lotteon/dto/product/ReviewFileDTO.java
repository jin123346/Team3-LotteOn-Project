package com.lotteon.dto.product;

import com.lotteon.entity.product.Review;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Builder
public class ReviewFileDTO {


    private long fileId;
    private String sname;  // 파일 저장 이름 (업로드된 파일명)
    private String path;
    private long reviewId;


    public ReviewFileDTO(long fileId, String sname, String path, long reviewId) {
        this.fileId = fileId;
        this.sname = sname;
        this.path = path;
        this.reviewId = reviewId;
    }
}
