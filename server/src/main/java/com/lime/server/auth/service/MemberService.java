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
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member createMember(String username, String password, String nickName, String email) {
        //패스워드 인코딩
        String encodedPassword = passwordEncoder.encode(password);

        Member member = Member.of(username, encodedPassword, nickName, email);
        memberRepository.save(member);

        return member;
    }
}
