package com.lotteon.security;

import com.lotteon.entity.User.Member;
import com.lotteon.entity.User.Seller;
import com.lotteon.entity.User.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@ToString(exclude = "seller")
@Builder
public class MyUserDetails implements UserDetails, OAuth2User {
    // User 엔티티 선언
    private User user;
    private Member member;
    private Seller seller;
    // OAuth 인증에 사용되는 속성
    private Map<String, Object> attributes;
    private String provider;

    @Builder
    public MyUserDetails(User user, Member member, Seller seller, Map<String, Object> attributes, String provider) {
        this.user = user;
        this.member = member;
        this.seller = seller;
        this.attributes = attributes;
        this.provider = provider;
    }

    @Override
    public String getName() {
        return member != null ? member.getName() : user.getUid(); // 방어적으로 member가 null일 경우를 처리
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole())); // 계정 권한 앞에 접두어 ROLE_ 붙여야 됨
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPass();
    }

    @Override
    public String getUsername() {
        return user.getUid();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // UserDetails와 OAuth2User에서 제공하는 정보를 활용
    public String getId() {
        return user.getUid();
    }

    public Member getMember() {
        return member; // Member 객체 반환
    }
}
