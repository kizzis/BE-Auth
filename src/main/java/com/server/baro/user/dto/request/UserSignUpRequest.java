package com.server.baro.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserSignUpRequest(
	@NotBlank(message = "아이디는 필수입니다.")
	String username,
	@NotBlank(message = "패스워드는 필수입니다.")
	String password,
	@NotBlank(message = "닉네임은 필수입니다.")
	String nickname,
	String adminToken
) {
}
