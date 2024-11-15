package com.lotteon.security;

import com.lotteon.entity.User.User;
import com.lotteon.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 사용자가 입력한 아이디로 사용자 조회, 비밀번호에 대한 검증은 이전 컴포넌트인 AuthenticationProvider에서 수행
        Optional<User> optUser = userRepository.findById(username);

        if (optUser.isPresent()) {
            // 시큐리티 사용자 인증객체 생성 후 반환
            User user = optUser.get();
            log.info("log user getRole :" + user.getRole());
            log.info("Retrieved user1: {}", user);
            log.info("Seller associated with user1: {}", user.getSeller());
            if (!user.getRole().equals("BLACK") && !user.getRole().equals("LEAVE") && !user.getRole().equals("DORMANT")) {
                MyUserDetails myUserDetails = MyUserDetails.builder()
                        .user(user)
                        .member(user.getMember())
                        .build();
                return myUserDetails;
            } else if (user == null) {
                throw new UsernameNotFoundException("없는 ID: " + username);

            } else {
                throw new InactiveUserException("User_Role: " + user.getRole());
            }
        }

        // 사용자가 입력한 아이디가 없을 경우
        throw new UsernameNotFoundException("없는 ID: " + username);
    }
}
