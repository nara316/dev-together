package com.project.devtogether.member.service;

import com.project.devtogether.common.error.MemberErrorCode;
import com.project.devtogether.common.exception.ApiException;
import com.project.devtogether.common.redis.service.RedisService;
import com.project.devtogether.common.security.user.CustomUserDetail;
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
import java.time.Duration;
import java.time.LocalDateTime;
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

    private final RedisService redisService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberResponse register(MemberRegisterRequest memberRegisterRequest) {
        if (isDuplicatedEmail(memberRegisterRequest.email())) {
            throw new ApiException(MemberErrorCode.EMAIL_NOT_DUPLICATED);
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

        TokenDto token = new TokenDto(
                jwtTokenProvider.issueToken(authentication),
                jwtTokenProvider.issueRefreshToken(authentication)
        );

        Member member = memberRepository.findFirstByEmail(email)
                .orElseThrow(() -> new ApiException(MemberErrorCode.MEMBER_NOT_FOUND));
        member.setLastLoginAt(LocalDateTime.now());
        return token;
    }

    @Transactional(readOnly = true)
    public MemberResponse readMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ApiException(MemberErrorCode.MEMBER_NOT_FOUND));
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

    public void logout(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveToken(request);
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);
        jwtTokenProvider.validateToken(refreshToken);
        //redis Key 가져와야함
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
        String email = userDetail.getUsername();
        String redisRefreshToken = redisService.getValues(email);
        if (redisService.checkExistsValue(redisRefreshToken)) {
            redisService.deleteValues(email);
        }
        //로그아웃시 24시간동안 해당 유저의 accessToken을 블랙리스트에 추가하고 추후 해당 토큰으로 요청시 에러 반환
        redisService.setValues(accessToken, "logout", Duration.ofHours(24));

    }

    private boolean isDuplicatedEmail(String email) {
        return memberRepository.findFirstByEmail(email).isPresent();
    }

    private Member getMemberBySecurity() {
        String currentUserEmail = SecurityUtil.getCurrentUserEmail();
        return memberRepository.findFirstByEmail(currentUserEmail)
                .orElseThrow(() -> new ApiException(MemberErrorCode.MEMBER_NOT_FOUND));
    }
}
