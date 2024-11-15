package com.lotteon.dto.admin;

import com.lotteon.dto.order.CategoryOrderCountDTO;
import com.lotteon.dto.product.ProductCategoryDTO;
import lombok.*;

import java.util.List;


@Data
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MainDataResponseDTO {

    private List<Long> orderData;
    private List<Long> paymentData; // Populate based on your data requirements
    private List<Long> cancelData; // Populate based on your data requirements
    private List<String> labels;

    // Constructor, getters, setters, etc.
    public MainDataResponseDTO(List<Long> orderData, List<Long> paymentData,
                               List<Long> cancelData, List<String> labels) {
        this.orderData = orderData;
        this.paymentData = paymentData;
        this.cancelData = cancelData;
        this.labels = labels;
    }

}
