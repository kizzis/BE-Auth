package com.server.baro.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UserLoginResponse", description = "로그인 성공 응답 DTO")
public record UserLoginResponse(
	@Schema(description = "인증에 사용될 JWT 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
	String token
) {
	public static UserLoginResponse from(String token) {
		return new UserLoginResponse(token);
	}
}
