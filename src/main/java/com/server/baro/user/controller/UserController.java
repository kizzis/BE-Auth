package com.server.baro.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.baro.user.dto.response.UserRoleUpdateResponse;
import com.server.baro.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "USER APIs", description = "사용자/관리자 관련 APIs")
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
@RequestMapping("/admin/users")
public class UserController {
	private final UserService userService;

	@Operation(
		summary = "관리자 권한 부여",
		description = "일반 사용자에게 관리자 권한을 부여하는 API입니다."
	)
	@ApiResponse(responseCode = "200", description = "권한 부여 성공")
	@PatchMapping("/{userId}/roles")
	public ResponseEntity<UserRoleUpdateResponse> updateUserRoleToAdmin(@PathVariable Long userId) {
		UserRoleUpdateResponse response = userService.updateUserRoleToAdmin(userId);
		return ResponseEntity.ok(response);
	}
}
