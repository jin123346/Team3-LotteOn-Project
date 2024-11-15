package com.lotteon.dto.product;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.io.IOException;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class OptionDTO {
    private String value;


    public OptionDTO(String value) {
        this.value = value;
    }

    // Getter & Setter
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    // 커스텀 디시리얼라이저
    public static class OptionDTODeserializer extends JsonDeserializer<OptionDTO> {
        @Override
        public OptionDTO deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new OptionDTO(p.getText());
        }
    }

    private long id;
    private long parent_id;    //부모상품
    private String optionName;     //프로덕트 서브네임
    private String optionDesc;
    private long optionStock;
    private String optionCode;
    private String parentCode;
    private String combination;

    private OptionGroupDTO optionGroup;
    private String name;
    private long additionalPrice;

}
