package com.lotteon.dto.product.request;

import com.lotteon.dto.product.*;
import com.lotteon.entity.product.*;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
public class ProductViewResponseDTO {

    private Product product;
    private ProductDetails productDetails;
    private Set<OptionGroup> optionGroups;
    private Set<ProductFile> files;
    private List<Review> reviews;
    private  Set<ProductOptionCombination> optionCombinations;
    public List<Option> options;



    @Builder
    public ProductViewResponseDTO(Product product,Set<OptionGroup> optionGroups,Set<ProductFile> files,List<Review> reviews, Set<ProductOptionCombination> optionCombinations,List<Option> options) {
        this.productDetails = product.getProductDetails();
        this.product = product;
        this.optionGroups = optionGroups;
        this.files = files;
        this.reviews = reviews;
        this.optionCombinations = optionCombinations;
        this.options = options;


    }

}
