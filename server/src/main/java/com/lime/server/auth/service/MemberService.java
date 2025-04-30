package com.lime.server.auth.service;

import com.lime.server.auth.Member;
import com.lime.server.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {
    //임시 회원가입 코드
    private static final String tempSignupKey = "cuteguru";

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member createMember(String username, String password, String nickName, String email, String signupKey) {
        if (!signupKey.equals(tempSignupKey)) {
            throw new IllegalArgumentException("signupKey not match");
        }

        //패스워드 인코딩
        String encodedPassword = passwordEncoder.encode(password);

        Member member = Member.of(username, encodedPassword, nickName, email);
        memberRepository.save(member);

        return member;
    }
}
