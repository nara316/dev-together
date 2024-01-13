package com.project.devtogether.common.security.service;

import com.project.devtogether.common.error.ErrorCode;
import com.project.devtogether.common.exception.ApiException;
import com.project.devtogether.common.security.user.CustomUserDetail;
import com.project.devtogether.member.domain.Member;
import com.project.devtogether.member.domain.MemberRepository;
import java.util.Collection;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findFirstByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new ApiException(ErrorCode.SERVER_ERROR, "해당ㄹ하는 유저를 찾을 수 없습니다."));
    }

    private CustomUserDetail createUserDetails(Member member) {
        Collection<? extends GrantedAuthority> authorities =
                Collections.singleton(new SimpleGrantedAuthority(member.getRole().toString()));
        return new CustomUserDetail(
                member.getEmail(),
                member.getPassword(),
                authorities,
                member.getId()
        );
    }
}
