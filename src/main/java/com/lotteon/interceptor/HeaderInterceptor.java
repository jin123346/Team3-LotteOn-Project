package com.lotteon.interceptor;

import com.lotteon.dto.User.SellerDTO;
import com.lotteon.dto.admin.HeaderInfoDTO;
import com.lotteon.service.HeaderInfoService;
import com.lotteon.service.user.SellerService;
import com.lotteon.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Log4j2
@Component
public class HeaderInterceptor implements HandlerInterceptor {

    private final HeaderInfoService headerInfoService;
    private final UserService userService;
    private final SellerService sellerService;

    public HeaderInterceptor(HeaderInfoService headerInfoService, UserService userService, SellerService sellerService) {this.headerInfoService = headerInfoService;
        this.userService = userService;
        this.sellerService = sellerService;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        String memberName=null;
        if(authentication != null){
            String uid= authentication.getName();
            String role= authentication.getAuthorities().toString();
            if(role.contains("ROLE_ADMIN") || role.contains("ROLE_SELLER")){

                memberName = fetchSellerName(uid);


            }else if(role.contains("ROLE_MEMBER")){

                memberName = fetchMemberName(uid);
                log.info("userInterceptor!!!!!!"+memberName);
            }

        }

        if (modelAndView != null) {

            HeaderInfoDTO headerInfo = headerInfoService.fetchIfCacheEmpty();
            modelAndView.addObject("headerInfo", headerInfo);
            modelAndView.addObject("memberName", memberName);

            String imagePath = "/uploads/ConfigImg/headerLogo.jpg"; // 실제 이미지 경로로 변경하세요.
            modelAndView.addObject("logoImagePath", imagePath);

            String imagePath2 = "/uploads/ConfigImg/footerLogo.jpg"; // 실제 이미지 경로로 변경하세요.
            modelAndView.addObject("footerlogoImagePath", imagePath2);
        }
    }


    @Cacheable(value = "memberNameCache", key = "#uid")
    public String fetchSellerName(String uid) {
        SellerDTO seller = sellerService.getSeller(uid);
        return seller.getCompany();
    }

    @Cacheable(value = "memberNameCache", key = "#uid")
    public String fetchMemberName(String uid) {
        return userService.getMemberNameByUsername(uid);
    }

}
