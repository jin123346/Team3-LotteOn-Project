package com.lotteon.controller.user;

import com.lotteon.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final EmailService emailService;

    private String verifyCode; //인증 코드를 저장할 필드

    @PostMapping("/sendVerifyCode")
    public String sendVerifyCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        verifyCode = emailService.sendVerifyEmail(email);
        return "이메일로 인증코드가 전송되었습니다.";
    }

    @PostMapping("/verifyCode")
    public String verifyCode(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        if (verifyCode.equals(code)) {
            return "인증 성공!";
        } else {
            return "인증코드가 올바르지 않습니다.";
        }
    }


}
