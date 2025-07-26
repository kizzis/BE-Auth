package com.server.baro.user.dto.response;

import java.util.List;

import com.server.baro.user.entity.User;
import com.server.baro.user.entity.UserRole;

public record UserRoleUpdateResponse(
	String username,
	String nickname,
	List<UserRoleResponse> roles
) {
	public static UserRoleUpdateResponse from(User user) {
		System.out.println("User in from method: " + user.getUsername() + ", " + user.getRole()); // 디버깅용

		return new UserRoleUpdateResponse(
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
