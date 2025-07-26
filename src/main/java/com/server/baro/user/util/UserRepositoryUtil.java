package com.server.baro.user.util;

import org.springframework.stereotype.Component;

import com.server.baro.common.ErrorCode;
import com.server.baro.common.exception.CustomException;
import com.server.baro.user.entity.User;
import com.server.baro.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserRepositoryUtil {
	private final UserRepository userRepository;

	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
	}

	public User getUserById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
	}

	public Boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	public User save(User user) {
		return userRepository.save(user);
	}
}
