package com.lotteon.oauth2;


import com.lotteon.dto.User.Grade;
import com.lotteon.dto.User.MemberDTO;
import com.lotteon.entity.User.MemberStatus;
import com.lotteon.entity.User.User;
import com.lotteon.entity.User.Member;
import com.lotteon.repository.user.UserRepository;
import com.lotteon.repository.user.MemberRepository;
import com.lotteon.security.MyUserDetails;
import com.lotteon.service.user.MemberService;
import com.lotteon.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class MyOauth2UserService extends DefaultOAuth2UserService {


    private final UserRepository userRepository;
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {

            log.info("loadUser..1 : " + userRequest);

            String accessToken = userRequest.getAccessToken().getTokenValue();
            log.info("loadUser..2 accessToken : " + accessToken);

            String provider = userRequest.getClientRegistration().getRegistrationId();
            log.info("loadUser..3 provider : " + provider);

            OAuth2User oAuth2User = super.loadUser(userRequest);
            log.info("loadUser...4 : " + oAuth2User);

            Map<String, Object> attributes = oAuth2User.getAttributes();
            log.info("loadUser...5 : " + attributes);

            // 사용자 확인 및 회원가입 처리
            String email = (String) attributes.get("email");
            String uid = "GOOGLE_" + email.split("@")[0];
            String name = attributes.get("given_name").toString();

            Optional<User> optUser = userRepository.findByUid(uid);

            if (optUser.isPresent()) {
                // 회원 존재하면 시큐리티 인증처리(로그인)
                User user = optUser.get();

                return MyUserDetails.builder()
                        .user(user)
                        .member(user.getMember())
                        .attributes(attributes)
                        .build();
            } else {
                // 신규 회원일 경우
                MemberDTO memberDTO = MemberDTO.builder()
                        .name(name)
                        .email(email)
                        .regDate(LocalDate.now()) // LocalDate 사용
                        .grade(Grade.FAMILY) // 기본값 설정
                        .status(MemberStatus.ACTIVE)
                        .build();

                Member member = Member.builder()
                        .name(memberDTO.getName())
                        .email(memberDTO.getEmail())
                        .regDate(memberDTO.getRegDate().atStartOfDay()) // LocalDate -> LocalDateTime 변환
                        .grade(memberDTO.getGrade())
                        .status(memberDTO.getStatus())
                        .build();


                memberRepository.save(member);


                User user = User.builder()
                        .uid(uid)
                        .member(member) // Member 엔티티 연결
                        .role(User.Role.valueOf("MEMBER")) // 기본 역할 설정
                        .build();

                userRepository.save(user);

                return MyUserDetails.builder()
                        .user(user)
                        .member(user.getMember())
                        .attributes(attributes)
                        .build();
            }


        } else if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            log.info("Kakao 로그인...1 : " + userRequest);

            String accessToken = userRequest.getAccessToken().getTokenValue();
            log.info("Kakao 로그인...2 : " + accessToken);

            OAuth2User oAuth2User = super.loadUser(userRequest);
            log.info("Kakao 로그인...3 : " + oAuth2User);

            Map<String, Object> attributes = oAuth2User.getAttributes();
            log.info("Kakao 로그인...4 : " + attributes);

            // 카카오 응답에서 사용자 정보 가져오기
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

            // 이메일 정보 추출
            String email = (String) kakaoAccount.get("email");

            // 닉네임 또는 이름 정보 추출
            String name = (String) profile.get("nickname");

            // 이메일에서 사용자 uid 추출
            String uid = email != null ? "KAKAO_" + email.split("@")[0] : null;

            // 결과 출력
            System.out.println("이메일: " + email);
            System.out.println("이름: " + name);
            System.out.println("사용자 ID: " + uid);


            Optional<User> optUser = userRepository.findByUid(uid);

            if (optUser.isPresent()) {
                // 회원 존재하면 시큐리티 인증처리(로그인)
                User user = optUser.get();

                return MyUserDetails.builder()
                        .user(user)
                        .member(user.getMember())
                        .attributes(attributes)
                        .build();
            } else {
                // 신규 회원일 경우
                MemberDTO memberDTO = MemberDTO.builder()
                        .name(name)
                        .email(email)
                        .regDate(LocalDate.now()) // LocalDate 사용
                        .grade(Grade.FAMILY) // 기본값 설정
                        .status(MemberStatus.ACTIVE)
                        .build();

                Member member = Member.builder()
                        .name(memberDTO.getName())
                        .email(memberDTO.getEmail())
                        .regDate(memberDTO.getRegDate().atStartOfDay()) // LocalDate -> LocalDateTime 변환
                        .grade(memberDTO.getGrade())
                        .status(memberDTO.getStatus())
                        .build();


                memberRepository.save(member);


                User user = User.builder()
                        .uid(uid)
                        .member(member) // Member 엔티티 연결
                        .role(User.Role.valueOf("MEMBER")) // 기본 역할 설정
                        .build();

                userRepository.save(user);

                return MyUserDetails.builder()
                        .user(user)
                        .member(user.getMember())
                        .attributes(attributes)
                        .build();
            }
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            log.info("Naver 로그인...1 : " + userRequest);

            String accessToken = userRequest.getAccessToken().getTokenValue();
            log.info("Naver 로그인...2 : " + accessToken);

            OAuth2User oAuth2User = super.loadUser(userRequest);
            log.info("Naver 로그인...3 : " + oAuth2User);

            Map<String, Object> attributes = oAuth2User.getAttributes();
            log.info("Naver 로그인...4 : " + attributes);

            // 카카오 응답에서 사용자 정보 가져오기
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");

            // 이메일 정보 추출
            String email = (String) response.get("email");

            // 닉네임 또는 이름 정보 추출
            String name = (String) response.get("name");

            // 이메일에서 사용자 uid 추출
            String uid = email != null ? "NAVER_" + email.split("@")[0] : null;

            // 결과 출력
            System.out.println("이메일: " + email);
            System.out.println("이름: " + name);
            System.out.println("사용자 ID: " + uid);

            Optional<User> optUser = userRepository.findByUid(uid);

            if (optUser.isPresent()) {
                // 회원 존재하면 시큐리티 인증처리(로그인)
                User user = optUser.get();

                return MyUserDetails.builder()
                        .user(user)
                        .member(user.getMember())
                        .attributes(attributes)
                        .build();
            } else {
                // 신규 회원일 경우
                MemberDTO memberDTO = MemberDTO.builder()
                        .name(name)
                        .email(email)
//                        .gender(gender)
//                        .hp(hp)
                        .regDate(LocalDate.now()) // LocalDate 사용
                        .grade(Grade.FAMILY) // 기본값 설정
                        .status(MemberStatus.ACTIVE)
                        .build();

                Member member = Member.builder()
                        .name(memberDTO.getName())
                        .email(memberDTO.getEmail())
                        .regDate(memberDTO.getRegDate().atStartOfDay()) // LocalDate -> LocalDateTime 변환
                        .grade(memberDTO.getGrade())
                        .status(memberDTO.getStatus())
                        .build();


                memberRepository.save(member);


                User user = User.builder()
                        .uid(uid)
                        .member(member) // Member 엔티티 연결
                        .role(User.Role.valueOf("MEMBER")) // 기본 역할 설정
                        .build();

                userRepository.save(user);

                return MyUserDetails.builder()
                        .user(user)
                        .member(user.getMember())
                        .attributes(attributes)
                        .build();
            }

        }
        return null;
    }
}