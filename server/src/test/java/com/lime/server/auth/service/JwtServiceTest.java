package com.lime.server.auth.service;

import static org.junit.jupiter.api.Assertions.*;

import com.lime.server.auth.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@Slf4j
@DisplayName("JWT 토큰 단위테스트")
@ExtendWith(MockitoExtension.class)
class JwtServiceTest {
    @InjectMocks
    private JwtService jwtService;

    Member member;

    @BeforeEach
    void setUp() {
        //secret key field 값 생성 - 테스트 전용값, 실제값 아님 참고
        ReflectionTestUtils.setField(jwtService, "SECRET_KEY", "15462948404D633166546A576E5A7234A53678214125442A472D4B614E748263");

        member = Member.of("이름", "암호화된비밀번호", "닉네임", "이메일");
    }

    @Test
    void createToken() {
        assertDoesNotThrow(() -> {
            String s = jwtService.generateAccessToken(member);
            log.info(s);
        });
    }
}