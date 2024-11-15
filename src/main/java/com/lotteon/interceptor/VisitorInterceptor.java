package com.lotteon.interceptor;

import com.lotteon.service.VisitorCountService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;

@Component
public class VisitorInterceptor implements HandlerInterceptor {

    private final VisitorCountService visitorCountService;
    private static final String VISITOR_COOKIE_NAME = "visited_today";

    public VisitorInterceptor(VisitorCountService visitorCountService) {
        this.visitorCountService = visitorCountService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 루트 경로 "/"에 접근할 때만 방문자 수를 증가시킴
        if ("/".equals(request.getRequestURI())) {
            boolean visitedToday = false;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (VISITOR_COOKIE_NAME.equals(cookie.getName())) {
                        visitedToday = true;
                        break;
                    }
                }
            }

            if (!visitedToday) {
                visitorCountService.incrementVisitorCount();

                Cookie visitCookie = new Cookie(VISITOR_COOKIE_NAME, "true");
                visitCookie.setMaxAge((int) Duration.ofDays(1).getSeconds()); // 24시간 쿠키 유지
                visitCookie.setPath("/"); // 모든 경로에 대해 쿠키 적용
                response.addCookie(visitCookie);
            }
        }
        return true;
    }
}
