package com.project.devtogether.member.service;

import com.project.devtogether.common.error.ErrorCode;
import com.project.devtogether.common.exception.ApiException;
import com.project.devtogether.common.security.util.SecurityUtil;
import com.project.devtogether.common.token.dto.TokenDto;
import com.project.devtogether.common.token.privider.JwtTokenProvider;
import com.project.devtogether.member.domain.Member;
import com.project.devtogether.member.domain.MemberRepository;
import com.project.devtogether.member.dto.MemberLoginRequest;
import com.project.devtogether.member.dto.MemberRegisterRequest;
import com.project.devtogether.member.dto.MemberResponse;
import com.project.devtogether.member.dto.MemberUpdateRequest;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberResponse register(MemberRegisterRequest memberRegisterRequest) {
        if (isDuplicatedEmail(memberRegisterRequest.email())) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "이메일은 중복될 수 없습니다.");
        }

        Member member = memberRegisterRequest.toEntity();
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        Member saveMember = memberRepository.save(member);
        return MemberResponse.of(saveMember);
    }

    public TokenDto login(MemberLoginRequest memberLoginRequest) {
        String email = memberLoginRequest.email();
        String password = memberLoginRequest.password();
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenDto token = jwtTokenProvider.issueToken(authentication);

        Member member = memberRepository.findFirstByEmail(email)
                .orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST));
        member.setLastLoginAt(LocalDateTime.now());
        return token;
    }

    @Transactional(readOnly = true)
    public MemberResponse readMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST));
        return MemberResponse.of(member);
    }

    @Transactional(readOnly = true)
    public MemberResponse readMe() {
        Member member = getMemberBySecurity();
        return MemberResponse.of(member);
    }

    public MemberResponse updateMe(MemberUpdateRequest memberUpdateRequest) {
        Member member = getMemberBySecurity();
        if (memberUpdateRequest.introduce() == null || memberUpdateRequest.introduce().isBlank()) {
            member.setNickName(memberUpdateRequest.nickName());
            return MemberResponse.of(member);
        }
        member.setNickName(memberUpdateRequest.nickName());
        member.setIntroduce(memberUpdateRequest.introduce());
        return MemberResponse.of(member);
    }

    private boolean isDuplicatedEmail(String email) {
        return memberRepository.findFirstByEmail(email).isPresent();
    }

    private Member getMemberBySecurity() {
        String currentUserEmail = SecurityUtil.getCurrentUserEmail();
        Member member = memberRepository.findFirstByEmail(currentUserEmail)
                .orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST));
        return member;
    }
}
