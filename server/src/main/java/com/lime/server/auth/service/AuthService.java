package com.lime.server.auth.service;

import com.lime.server.auth.entity.Member;
import com.lime.server.auth.repository.MemberRepository;
import com.lime.server.error.LoginUserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member login(String username, String password) throws LoginUserNotFoundException {
        Member findMember = memberRepository.findByUsername(username)
                .orElseThrow(() -> new LoginUserNotFoundException("찾을 수 없는 사용자"));

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            password
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("비밀번호가 틀렸습니다");
        }

        return findMember;
    }
}
