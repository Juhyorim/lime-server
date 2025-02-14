package com.lime.server.auth.service;

import com.lime.server.auth.Member;
import com.lime.server.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public void createMember(String username, String password, String nickName, String email) {
        Member member = Member.of(username, password, nickName, email);
        memberRepository.save(member);
    }
}
