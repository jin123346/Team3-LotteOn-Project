package com.lotteon.interceptor;

import com.lotteon.dto.product.ProductCategoryDTO;
import com.lotteon.service.product.ProductCategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CategoryInterceptor implements HandlerInterceptor {

    private final ProductCategoryService productCategoryService;


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 모델이 존재하는 경우에만 카테고리 데이터를 추가
        if (modelAndView != null) {
            List<ProductCategoryDTO> categories = productCategoryService.getCategoriesWithCacheCheck(); // 캐시된 데이터 가져옴
            modelAndView.addObject("categories", categories);
        }
    }
}
