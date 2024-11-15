package com.lotteon.config;

import com.lotteon.dto.product.ProductDTO;
import com.lotteon.entity.product.Product;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {


    @Bean
    public AppInfo getAppInfo() {
        return  new AppInfo();
    }


    @Bean
    public ModelMapper getModelMapper(){
        //dto entity간 변환을 위한 model mapper 설정
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true);

        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.typeMap(Product.class, ProductDTO.class)
                .addMappings(mapper -> mapper.skip(ProductDTO::setOptionCombinations));

        return modelMapper;
    }
}
