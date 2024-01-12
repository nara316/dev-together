package com.project.devtogether.member.service;

import com.project.devtogether.member.domain.Member;
import com.project.devtogether.member.domain.MemberRepository;
import com.project.devtogether.member.dto.MemberLoginRequest;
import com.project.devtogether.member.dto.MemberRegisterRequest;
import com.project.devtogether.member.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponse register(MemberRegisterRequest memberRegisterRequest) {
        Member member = memberRegisterRequest.toEntity();
        Member saveMember = memberRepository.save(member);
        return MemberResponse.of(saveMember);
    }

    @Transactional(readOnly = true)
    public MemberResponse login(MemberLoginRequest memberLoginRequest) {
        Member member = memberRepository.findFirstByEmailAndPassword(
                memberLoginRequest.email(), memberLoginRequest.password());
        return MemberResponse.of(member);
    }
}
