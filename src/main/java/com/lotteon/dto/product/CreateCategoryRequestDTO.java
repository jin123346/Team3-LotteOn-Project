package com.lotteon.dto.product;


import com.lotteon.entity.product.ProductCategory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateCategoryRequestDTO {

    private String name;       // Name of the category
    private Long parentId;     // Parent category ID (nullable for root-level categories)
    private int level;         // Level of the category (1st, 2nd, or 3rd level)
    private String subcategory; // Subcategory information
    private String dispYn = "y"; // Display flag (default "y")
    private String note;       // Additional notes about the category


}
