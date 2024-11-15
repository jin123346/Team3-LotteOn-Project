package com.lotteon.service.product;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lotteon.dto.product.ProductRedisDTO;
import com.lotteon.entity.product.Product;
import com.lotteon.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class BestProductService {
    private final RedisTemplate redisTemplate;
    private final ProductRepository productRepository;

    private final ObjectMapper objectMapper;

//    public void saveLongValue(String key, Long value) throws JsonProcessingException {
//        String jsonValue = objectMapper.writeValueAsString(value);
//        redisTemplate.opsForValue().set(key, jsonValue);
//    }
//
//    public Long getLongValue(String key) throws JsonProcessingException {
//        String jsonValue = (String) redisTemplate.opsForValue().get(key);
//        return objectMapper.readValue(jsonValue, Long.class);
//    }


    public List<ProductRedisDTO> getTop5ProductFromDB(){

        Pageable pageable =PageRequest.of(0,5, Sort.by("sold").descending());
        List<Product> products = productRepository.findAllByOrderBySoldDesc(pageable);
        List<ProductRedisDTO> productRedisDTOs = new ArrayList<>();
        for (Product product : products) {
            long originalPrice = product.getPrice();
            long discount = product.getDiscount();
            long finalPrice = finalPrice(originalPrice, discount);

            ProductRedisDTO productRedisDTO = ProductRedisDTO.builder()
                    .file230(product.getFile230())
                    .productName(product.getProductName())
                    .productId(product.getProductId())
                    .savedPath(product.getSavedPath())
                    .discount(product.getDiscount())
                    .originalPrice(product.getPrice())
                    .categoryId(product.getCategoryId())
                    .finalPrice(finalPrice)
                    .build();
            productRedisDTOs.add(productRedisDTO);
        }
        return productRedisDTOs;
    }


    public void updateSellingInRedis(ProductRedisDTO product,long quantity) {
        String key = "BestProduct:"+product.getProductId();
        if(redisTemplate.hasKey(key)) {

            redisTemplate.opsForHash().put(key,"productId",Long.toString(product.getProductId()));
            redisTemplate.opsForHash().put(key,"productName",(product.getProductName()));
            redisTemplate.opsForHash().put(key,"categoryId",Long.toString(product.getCategoryId()));
            redisTemplate.opsForHash().put(key,"originalPrice",Long.toString(product.getOriginalPrice()));
            redisTemplate.opsForHash().put(key,"discount",Long.toString(product.getDiscount()));
            redisTemplate.opsForHash().put(key,"finalPrice",Long.toString(product.getFinalPrice()));
            redisTemplate.opsForHash().put(key,"file230",product.getFile230());
            redisTemplate.opsForHash().put(key,"savedPath",product.getSavedPath());

            Long currentSold = (Long) redisTemplate.opsForHash().get(key,"sold");

            if (currentSold == null) {
                currentSold = 0L; // 기본 값을 0으로 설정
            }
            redisTemplate.opsForHash().put(key,"sold",currentSold+quantity);
            Long changedSold = (Long) redisTemplate.opsForHash().get(key,"sold");
            redisTemplate.opsForZSet().add("best_selling_products", key, changedSold);

        }else{
            Map<String,Object> productDatas  = new HashMap<>();

            productDatas.put("productId",product.getProductId());
            productDatas.put("productName",product.getProductName());
            productDatas.put("categoryId",product.getCategoryId());
            productDatas.put("originalPrice",product.getOriginalPrice());
            productDatas.put("discount",product.getDiscount());
            productDatas.put("finalPrice",product.getProductId());
            productDatas.put("file230",product.getFile230());
            productDatas.put("savedPath",product.getSavedPath());

            redisTemplate.opsForHash().putAll(key,productDatas);

            redisTemplate.opsForZSet().add("best_selling_products", key, quantity);


        }
    }


    public List<ProductRedisDTO> getBestProductOrderBySelling(){
        Set<String> top5Keys = redisTemplate.opsForZSet().reverseRange("best_selling_products", 0, 5);
        List<ProductRedisDTO> top5Products = new ArrayList<>();
        if(top5Keys != null && !top5Keys.isEmpty()) {
            for(String key : top5Keys) {
                Map<Object,Object> productData = (Map<Object,Object>)redisTemplate.opsForHash().entries(key);
                ProductRedisDTO product = redisToProductRedisDTO(productData);
                if(product != null) {
                    top5Products.add(product);
                }
            }
            return top5Products;

        }else{
            return getTop5ProductFromDB();
        }
    }


    public ProductRedisDTO redisToProductRedisDTO(Map<Object,Object> productData) {
       if(productData == null) {
           return null;
       }
       long productId = Long.parseLong(productData.get("productId").toString());
       long categoryId = Long.parseLong(productData.get("categoryId").toString());
       long originalPrice = Long.parseLong(productData.get("originalPrice").toString());
       long discount = Long.parseLong(productData.get("discount").toString());
       long finalPrice = finalPrice(originalPrice,discount);
       return ProductRedisDTO.builder()
               .productId(productId)
               .productName((String)productData.get("productName"))
               .categoryId(categoryId)
               .originalPrice((Long)productData.get("originalPrice"))
               .discount((Long)productData.get("discount"))
               .finalPrice(finalPrice)
               .savedPath((String)productData.get("savedPath"))
               .build();

    }

    private long finalPrice(long originalPrice,long discount) {
        long discountedPrice = (originalPrice * (100 - discount) / 100);
        discountedPrice = (discountedPrice / 10) * 10;
        return discountedPrice;
    }
}
