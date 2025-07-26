package com.server.baro.user.dto.response;

public record UserLoginResponse(
	String token
) {
	public static UserLoginResponse from(String token) {
		return new UserLoginResponse(token);
	}
}
