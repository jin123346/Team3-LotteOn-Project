package com.lotteon.config;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RedirectToLoginException.class)
    public String handleRedirectToLogin(RedirectToLoginException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "사용자가 인증되지 않았습니다.");

        return "redirect:/user/login"; // 로그인 페이지로 리다이렉트
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public String handleUserNotFound(UsernameNotFoundException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "사용자를 찾을 수 없습니다.");

        return "redirect:/user/login"; // 사용자 없음 시 로그인 페이지로 리다이렉트
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "알수 없는 애러가 발생했습니다.");


        return "error"; // 일반 에러 페이지로 리다이렉트
    }
}
