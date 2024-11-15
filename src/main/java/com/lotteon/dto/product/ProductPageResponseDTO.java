package com.lotteon.dto.product;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductPageResponseDTO {

    private List<ProductDTO> productDTOList;

    private String type;
    private int pg;
    private int size;
    private String sellerId;
    private int total;
    private int startNo;
    private int start, end;
    private boolean prev,next;

    private List<ProductFileDTO> productFileDTOList;


    @Builder
    public ProductPageResponseDTO(PageRequestDTO pageRequestDTO,List<ProductDTO> productDTOList,int total) {

        this.type = pageRequestDTO.getType();
        this.pg = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.productDTOList = productDTOList;

        this.startNo = total - ((pg-1)*size);
        this.end=(int)(Math.ceil(this.pg/ (double)size))*size;
        this.start= this.end - size-1;

        int last=(int)(Math.ceil(total/(double)size))*size;

        this.end = end > last ? last : end;
        if(this.start>this.end){
            this.end=this.start;
        }

        this.prev = this.start> 1;
        this.next = total > this.end * this.size;

    }


}
