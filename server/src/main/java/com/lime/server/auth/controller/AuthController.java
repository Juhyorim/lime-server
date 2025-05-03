package com.lime.server.auth.controller;

import com.lime.server.auth.Member;
import com.lime.server.auth.dto.LoginRequestDto;
import com.lime.server.auth.dto.LoginResponseDto;
import com.lime.server.auth.dto.SignupRequestDto;
import com.lime.server.auth.service.AuthService;
import com.lime.server.auth.service.JwtService;
import com.lime.server.auth.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("${api_prefix}")
@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {
    private final MemberService memberService;
    private final JwtService jwtService;
    private final AuthService authService;

    @PostMapping("/join")
    public ResponseEntity<LoginResponseDto> signup(@RequestBody SignupRequestDto request) {
        Member member = memberService.createMember(request.username(), request.password(), request.nickname(),
                request.email(), request.signupKey());

        String token = jwtService.generateAccessToken(member);

        return ResponseEntity.ok(
                new LoginResponseDto(member.getUsername(), member.getNickname(), member.getEmail(), token));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        Member member = authService.login(request.username(), request.password());
        String token = jwtService.generateAccessToken(member);

        return ResponseEntity.ok(new LoginResponseDto(member.getUsername(), member.getNickname(), member.getEmail(), token));
    }
}
