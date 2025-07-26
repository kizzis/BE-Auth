package com.server.baro.user.dto.response;

import java.util.List;

import com.server.baro.user.entity.User;
import com.server.baro.user.entity.UserRole;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UserSignUpResponse", description = "회원가입 성공 응답 DTO")
public record UserSignUpResponse(
	@Schema(description = "새로 생성된 사용자의 아이디", example = "user123")
	String username,

	@Schema(description = "새로 생성된 사용자의 닉네임", example = "유저123")
	String nickname,

	@Schema(description = "새로 생성된 사용자의 권한 목록")
	List<UserRoleResponse> roles
) {
	public static UserSignUpResponse from(User user) {
		return new UserSignUpResponse(
			user.getUsername(),
			user.getNickname(),
			List.of(UserRoleResponse.from(user.getRole()))
		);
	}

	@Schema(name = "UserRoleResponse", description = "사용자 권한 정보 DTO")
	public record UserRoleResponse(
		@Schema(description = "사용자 역할 (USER 또는 ADMIN)", example = "ADMIN")
		UserRole role
	) {
		public static UserRoleResponse from(UserRole role) {
			return new UserRoleResponse(role);
		}
	}
}
