package com.server.baro.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.server.baro.user.dto.response.UserRoleUpdateResponse;
import com.server.baro.user.entity.User;
import com.server.baro.user.util.UserRepositoryUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
	private final UserRepositoryUtil userRepositoryUtil;

	public UserRoleUpdateResponse updateUserRoleToAdmin(Long userId) {
		User user = userRepositoryUtil.getUserById(userId);
		user.updateUserRoleToAdmin();

		return UserRoleUpdateResponse.from(user);
	}
}
