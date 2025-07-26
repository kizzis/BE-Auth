package com.server.baro.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.server.baro.user.dto.request.UserLoginRequest;
import com.server.baro.user.dto.request.UserSignUpRequest;
import com.server.baro.user.dto.response.UserLoginResponse;
import com.server.baro.user.dto.response.UserSignUpResponse;
import com.server.baro.user.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<UserSignUpResponse> signUp(@RequestBody @Valid UserSignUpRequest request) {
		UserSignUpResponse response = authService.signUp(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/login")
	public ResponseEntity<UserLoginResponse> login(@RequestBody @Valid UserLoginRequest request) {
		UserLoginResponse response = authService.login(request);
		return ResponseEntity.ok(response);
	}
}
