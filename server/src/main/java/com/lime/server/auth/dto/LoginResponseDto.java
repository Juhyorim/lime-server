package com.lime.server.auth.dto;

public record LoginResponseDto(String username, String nickname, String email, String token) {
}
