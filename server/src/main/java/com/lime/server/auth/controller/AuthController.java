package com.lime.server.auth.controller;

import com.lime.server.auth.Member;
import com.lime.server.auth.dto.LoginResponseDto;
import com.lime.server.auth.dto.SignupRequestDto;
import com.lime.server.auth.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {
    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<LoginResponseDto> signup(@RequestBody SignupRequestDto request) {
        Member member = memberService.createMember(request.username(), request.password(), request.nickname(),
                request.email(), request.signupKey());

        return ResponseEntity.ok(new LoginResponseDto(member.getUsername(), member.getNickname(), member.getEmail()));
    }
}
