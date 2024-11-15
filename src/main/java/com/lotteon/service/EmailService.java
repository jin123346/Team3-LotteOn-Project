package com.lotteon.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender emailSender;
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public String sendVerifyEmail(String toEmail) {
        String code = String.valueOf(new Random().nextInt(900000) + 100000); // 6자리 인증

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("woo24465@gmail.com");
        message.setTo(toEmail);
        message.setSubject("이메일 인증 코드");
        message.setText("다음 인증 코드를 입력하세요: " + code);

        logger.info("code: " + code);

        try {
            emailSender.send(message);
            logger.info("인증 코드 이메일이 성공적으로 발송되었습니다: {}", toEmail);
        } catch (Exception e) {
            logger.error("이메일 발송 실패: {}", e.getMessage());
            return null; // 또는 적절한 에러 메시지를 반환합니다.
        }

        return code;
    }
}
