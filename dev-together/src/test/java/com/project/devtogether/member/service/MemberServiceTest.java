package com.project.devtogether.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.project.devtogether.member.domain.Member;
import com.project.devtogether.member.domain.MemberRepository;
import com.project.devtogether.member.dto.MemberRegisterRequest;
import com.project.devtogether.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("서비스 로직 테스트 - member")
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @DisplayName("회원 정보를 입력하면, 가입을 완료하고 해당 정보를 리턴한다.")
    @Test
    void registerSuccessTest() {
        //given
        MemberRegisterRequest request = createMember("제로");

        //when
        MemberResponse register = memberService.register(request);

        //then
        assertThat(register.email()).isEqualTo(request.email());
    }

    private MemberRegisterRequest createMember(String name) {
        return new MemberRegisterRequest(
                "asdf@naver.com",
                "1234",
                name,
                "날라",
                null
        );
    }

}