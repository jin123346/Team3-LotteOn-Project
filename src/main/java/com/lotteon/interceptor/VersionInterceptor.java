package com.lotteon.interceptor;

import com.lotteon.dto.VersionDTO;
import com.lotteon.service.VersionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Log4j2
@Component
public class VersionInterceptor implements HandlerInterceptor {

    private final VersionService versionService;

    public VersionInterceptor(VersionService versionService) {
        this.versionService = versionService;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            // Fetch the latest version from cache or database
            VersionDTO version = versionService.getVersionWithCacheCheck();
            modelAndView.addObject("versionInfo", version);
        }
    }
}
