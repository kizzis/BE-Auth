package com.server.baro.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.baro.user.dto.response.UserRoleUpdateResponse;
import com.server.baro.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
@RequestMapping("/admin/users")
public class UserController {
	private final UserService userService;

	@PatchMapping("/{userId}/roles")
	public ResponseEntity<UserRoleUpdateResponse> updateUserRoleToAdmin(@PathVariable Long userId) {
		UserRoleUpdateResponse response = userService.updateUserRoleToAdmin(userId);
		return ResponseEntity.ok(response);
	}
}
