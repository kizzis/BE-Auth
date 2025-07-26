package com.server.baro.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.server.baro.user.dto.request.UserLoginRequest;
import com.server.baro.user.dto.request.UserSignUpRequest;
import com.server.baro.user.dto.response.UserLoginResponse;
import com.server.baro.user.dto.response.UserSignUpResponse;
import com.server.baro.user.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Auth(SignUp/Login) APIs", description = "회원가입/로그인 APIs")
@RestController
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@Operation(
		summary = "회원가입",
		description = "새로운 사용자 회원가입 API입니다. " +
			"adminToken은 관리자 등록 시 필수이며, 일반 사용자는 불필요합니다."
	)
	@ApiResponse(responseCode = "201", description = "회원가입 성공")
	@PostMapping("/signup")
	public ResponseEntity<UserSignUpResponse> signUp(@RequestBody @Valid UserSignUpRequest request) {
		UserSignUpResponse response = authService.signUp(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Operation(
		summary = "로그인",
		description = "로그인 API입니다. (username과 password 활용)"
	)
	@ApiResponse(responseCode = "200", description = "로그인 성공")
	@PostMapping("/login")
	public ResponseEntity<UserLoginResponse> login(@RequestBody @Valid UserLoginRequest request) {
		UserLoginResponse response = authService.login(request);
		return ResponseEntity.ok(response);
	}
}
