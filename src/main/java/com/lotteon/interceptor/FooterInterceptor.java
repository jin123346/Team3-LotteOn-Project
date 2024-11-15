package com.lotteon.interceptor;

import com.lotteon.dto.FooterInfoDTO;
import com.lotteon.service.FooterInfoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class FooterInterceptor implements HandlerInterceptor {

   private final FooterInfoService footerInfoService;

    public FooterInterceptor(FooterInfoService footerInfoService) {
        this.footerInfoService = footerInfoService;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            // 캐시 확인 메서드로 변경하여 캐시가 없을 경우 DB에서 가져오도록 설정
            FooterInfoDTO footerInfo = footerInfoService.getFooterInfoWithCacheCheck();
            modelAndView.addObject("footerInfo", footerInfo);
            modelAndView.addObject("footerInfo", footerInfo);
        }
    }
}
