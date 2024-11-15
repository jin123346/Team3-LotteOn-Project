package com.lotteon.dto.product;

import com.lotteon.entity.product.Option;
import com.lotteon.entity.product.OptionItem;
import com.lotteon.entity.product.Product;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
public class ProductResponseDTO{
    private ProductDTO product;
    private List<OptionDTO> options;
    private ProductDetailsDTO productDetails;

    private List<OptionItemDTO> optionItems;
    private List<OptionGroupDTO> optionGroups;
    private List<ProductOptionCombinationDTO> productOptionCombinations;
    private MultiValueMap<String, MultipartFile> images;


    @Builder
    public ProductResponseDTO(ProductRequestDTO productRequest,List<MultipartFile> files) {
        log.info("여기 ");
        this.product = ProductDTO.builder()
                .categoryFirstId(Long.parseLong(productRequest.getFirstLevelCategory()))
                .categorySecondId(Long.parseLong(productRequest.getSecondLevelCategory()))
                .categoryId(Long.parseLong(productRequest.getThirdLevelCategory()))
                .productName(productRequest.getProductName())
                .productDesc(productRequest.getProductDesc())
                .price(productRequest.getPrice())
                .stock(productRequest.getStock())
                .discount(productRequest.getDiscount())
                .shippingFee(productRequest.getShippingFee())
                .shippingTerms(productRequest.getShippingTerms())
                .sellerId(productRequest.getSellerId())
                .build();
        this.optionGroups = new ArrayList<>();

        List<OptionGroupDTO> optionGroupDTOS = productRequest.getOptions();
        for(OptionGroupDTO optionGroupDTO : optionGroupDTOS){
            log.info("optionGroup setting,,,");
            List<OptionItemDTO> optionItemDTOS = new ArrayList<>();
           for(String optionItem : optionGroupDTO.getItems()){
               OptionItemDTO optionItemDTO = OptionItemDTO.builder()
                       .optionName(optionItem)
                       .build();
               optionItemDTOS.add(optionItemDTO);
           }
           optionGroupDTO.setOptionItems(optionItemDTOS);
           this.optionGroups.add(optionGroupDTO);
        }

        this.productOptionCombinations = productRequest.getCombinations();

        List<ProductOptionCombinationDTO> productOptionCombinationDTOS = productRequest.getCombinations();





//        log.info("여기2");
//        this.options= new ArrayList<>();
//        List<String> optionNames = productRequest.getOptionName();
//        List<String> optionDesc = productRequest.getOptionDesc();
//        List<Integer> optionStocks = productRequest.getOptionStock();
//        for(int i=0; i<optionNames.size(); i++) {
//            if(optionNames.get(i)==null){
//                continue;
//            }
//            OptionDTO optionDTO = new OptionDTO();
//            optionDTO.setOptionName(optionNames.get(i));
//            optionDTO.setOptionDesc(optionDesc.get(i));
//            optionDTO.setOptionStock(optionStocks.get(i));
//
//            this.options.add(optionDTO);
//        }
        if (files != null) {
            int size = files.size();
            if(size !=0 ){
                this.images = new LinkedMultiValueMap<>();
                for(int i=0; i<size; i++) {
                    MultipartFile file = files.get(i);
                    if (i == 0) {
                        images.add("190", file);
                    } else if (i == 1) {
                        images.add("230", file);
                    } else if (i == 2) {
                        images.add("456", file);
                    } else {
                        images.add("940", file);
                    }
                }
            }
        }


        this.productDetails = ProductDetailsDTO.builder()
                .condition(productRequest.getCondition())
                .tax(productRequest.getTax())
                .receiptIssuance(productRequest.getReceiptIssuance())
                .manufactureCountry(productRequest.getManufactureCountry())
                .busniesstype(productRequest.getBusniesstype())
                .build();



    }


}
