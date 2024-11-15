package com.lotteon.dto.product;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
@Builder
public class PageRequestDTO {
    @Builder.Default
    private int no=1;

    @Builder.Default
    private int page=1;

    @Builder.Default
    private int size=10;

    private Long categoryId;

    //검색
    private String type;
    private String keyword;
    private String sort="sold";
    private String searchMode;

    //
    private String uid;


    public Pageable getPageable(String sort, int size) {
        this.size=size;
            return PageRequest.of(this.page-1,this.size, Sort.by(sort).descending());

    }
    public Pageable getSortPageable(int size) {
        this.size=size;
        if(sort.equals("lowPrice")){
            this.sort="price";
            return PageRequest.of(this.page-1,this.size, Sort.by(this.sort).ascending());

        }else if(sort.equals("highPrice")){
            this.sort="price";
            return PageRequest.of(this.page-1,this.size, Sort.by(this.sort).descending());

        }else if(sort.equals("rating")){
            this.sort="rating";

        }else if(sort.equals("reviewCount")){
            this.sort = "reviewCount";
        }else if(sort.equals("recent")){
            this.sort="rdate";
        }else if(sort.equals("popular")){
            this.sort="hit";
        }else{
            this.sort="sold";
        }


        return PageRequest.of(this.page-1,this.size, Sort.by(this.sort).descending());

    }

}
