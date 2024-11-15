package com.lotteon.dto.product;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class ProductCategoryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long parent_id;
    private String name;
    private int level;
    private String subcategory;
    private String disp_yn; //디스플레이 유무
    private String note;


    private List<ProductCategoryDTO> children;  // 추가된 필드



}
