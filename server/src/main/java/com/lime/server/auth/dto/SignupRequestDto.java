package com.lime.server.auth.dto;

public record SignupRequestDto(String username, String password, String nickname, String email, String signupKey) {
}
