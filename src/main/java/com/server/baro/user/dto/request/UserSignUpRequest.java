package com.server.baro.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(name = "UserSignUpRequest", description = "회원가입 요청 Dto")
public record UserSignUpRequest(
	@Schema(description = "아이디 (필수)", example = "user123")
	@NotBlank(message = "아이디는 필수입니다.")
	@Size(min = 4, max = 15, message = "아이디는 4자 이상 15자 이하로 입력해주세요.")
	String username,

	@Schema(description = "비밀번호 (필수)", example = "1234Abcd!@")
	@NotBlank(message = "비밀번호는 필수입니다.")
	@Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
	@Pattern(
		regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
		message = "비밀번호는 대소문자, 숫자, 특수문자를 각각 최소 1개 포함해야 합니다."
	)
	String password,

	@Schema(description = "닉네임 (필수)", example = "토마토")
	@NotBlank(message = "닉네임은 필수입니다.")
	@Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하로 입력해주세요.")
	String nickname,

	@Schema(
		description = "관리자 토큰 (관리자 계정 생성 시 필요, 일반 사용자는 불필요)",
		example = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC",
		required = false
	)
	String adminToken
) {
}
