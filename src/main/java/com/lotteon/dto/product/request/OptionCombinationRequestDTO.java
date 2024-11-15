package com.lotteon.dto.product.request;


import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OptionCombinationRequestDTO {

    private List<OptionGroupDTO> optionGroups;

    @Getter
    @Setter
    @ToString
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OptionGroupDTO {
        private String groupName;
        private List<OptionItemDTO> optionItems;
    }

    @Getter
    @Setter
    @ToString
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OptionItemDTO {
        private String optionName;
    }

}
