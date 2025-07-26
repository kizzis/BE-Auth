package com.server.baro.user.dto.response;

import java.util.List;

import com.server.baro.user.entity.User;
import com.server.baro.user.entity.UserRole;

public record UserSignUpResponse(
	String username,
	String nickname,
	List<UserRoleResponse> roles
) {
	public static UserSignUpResponse from(User user) {
		return new UserSignUpResponse(
			user.getUsername(),
			user.getNickname(),
			List.of(UserRoleResponse.from(user.getRole()))
		);
	}

	public record UserRoleResponse(
		UserRole role
	) {
		public static UserRoleResponse from(UserRole role) {
			return new UserRoleResponse(role);
		}
	}
}
