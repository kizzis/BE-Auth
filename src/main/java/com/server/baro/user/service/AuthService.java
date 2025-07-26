package com.server.baro.user.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.server.baro.common.ErrorCode;
import com.server.baro.common.exception.CustomException;
import com.server.baro.security.jwt.JwtUtil;
import com.server.baro.user.dto.request.UserLoginRequest;
import com.server.baro.user.dto.request.UserSignUpRequest;
import com.server.baro.user.dto.response.UserLoginResponse;
import com.server.baro.user.dto.response.UserSignUpResponse;
import com.server.baro.user.entity.User;
import com.server.baro.user.entity.UserRole;
import com.server.baro.user.util.UserRepositoryUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private final UserRepositoryUtil userRepositoryUtil;
	@Value("${admin.secret.key}")
	private String adminSecretKey;

	public UserSignUpResponse signUp(UserSignUpRequest request) {
		if (userRepositoryUtil.existsByUsername(request.username())) {
			throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
		}

		UserRole role;
		if (request.adminToken() == null) {
			role = UserRole.USER;
		} else {
			if (!adminSecretKey.equals(request.adminToken())) {
				throw new CustomException(ErrorCode.INVALID_ADMIN_SECRET_KEY);
			}
			role = UserRole.ADMIN;
		}

		String encodedPassword = passwordEncoder.encode(request.password());

		User user = User.builder()
			.username(request.username())
			.password(encodedPassword)
			.nickname(request.nickname())
			.role(role)
			.build();

		userRepositoryUtil.save(user);
		return UserSignUpResponse.from(user);
	}

	public UserLoginResponse login(UserLoginRequest request) {
		User user = userRepositoryUtil.getUserByUsername(request.username());

		if (!passwordEncoder.matches(request.password(), user.getPassword())) {
			throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
		}

		String accessToken = jwtUtil.createToken(user.getUsername(), user.getRole());
		return UserLoginResponse.from(accessToken);
	}
}
