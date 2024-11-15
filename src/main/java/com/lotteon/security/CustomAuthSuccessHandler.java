package com.lotteon.security;

import com.lotteon.entity.User.Member;
import com.lotteon.repository.user.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Log4j2
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {


        // 사용자 권한(Role) 확인
        boolean isMember = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MEMBER"));
        boolean isSeller = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SELLER"));
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        // 역할에 따라 리다이렉트
        if (isMember) {

            String uid = authentication.getName();
            log.info("Current UID {}",uid);

            Optional<Member> memberOptional = memberRepository.findByUser_Uid(uid);
            log.info("Current member {}",uid);

            if (memberOptional.isPresent()) {

                Member member = memberOptional.get();
                member.setLastDate(LocalDateTime.now()); // 현재 시간으로 업데이트
                memberRepository.save(member);
            }else {
                log.warn("Member not found for uid: {}", uid); // Member가 없을 경우 경고 로그
            }

            response.sendRedirect("/"); // 회원일 경우 이동할 경로
        } else if (isSeller || isAdmin) {
            response.sendRedirect("/admin/main"); // 판매자 또는 관리자인 경우 이동할 경로
        } else {
            response.sendRedirect("/seller/login?error=true"); // 권한 없는 사용자는 로그인 실패 페이지로 이동
        }

    }
}
