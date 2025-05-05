package com.lime.server.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.lime.server.auth.entity.Member;
import com.lime.server.auth.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void createMember() {
        Member member = memberService.createMember("username", "password", "nickname", "email");

        assertEquals("username", member.getUsername());
        assertEquals("nickname", member.getNickname());
        assertEquals("email", member.getEmail());
        assertTrue(passwordEncoder.matches("password", member.getPassword()));
    }
}
