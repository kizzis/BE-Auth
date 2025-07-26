package com.server.baro.user.dto.response;

import java.util.List;

import com.server.baro.user.entity.User;
import com.server.baro.user.entity.UserRole;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UserRoleUpdateResponse", description = "권한 부여 성공 응답 DTO")
public record UserRoleUpdateResponse(
	@Schema(description = "사용자의 아이디", example = "admin1234")
	String username,

	@Schema(description = "사용자의 닉네임", example = "관리자12")
	String nickname,

	@Schema(description = "업데이트된 사용자의 권한 목록")
	List<UserRoleResponse> roles
) {
	public static UserRoleUpdateResponse from(User user) {
		return new UserRoleUpdateResponse(
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
