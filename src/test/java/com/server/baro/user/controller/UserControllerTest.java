package com.server.baro.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.server.baro.security.jwt.JwtUtil;
import com.server.baro.user.entity.User;
import com.server.baro.user.entity.UserRole;
import com.server.baro.user.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtUtil jwtUtil;

	private User adminUser;
	private User normalUser;
	private User targetUser;

	@BeforeEach
	void setUp() {
		adminUser = userRepository.save(User.builder()
			.username("admin1")
			.password("password")
			.nickname("관리자")
			.role(UserRole.ADMIN)
			.build());

		normalUser = userRepository.save(User.builder()
			.username("user1")
			.password("password")
			.nickname("일반 사용자")
			.role(UserRole.USER)
			.build());

		targetUser = userRepository.save(User.builder()
			.username("target")
			.password("password")
			.nickname("일반 사용자2")
			.role(UserRole.USER)
			.build());
	}

	// 관리자 권한 부여 API
	// - 관리자 권한을 가진 사용자가 요청할 때 정상 처리되는지 테스트
	@Test
	@DisplayName("성공 - 관리자 권한을 가진 사용자가 다른 사용자에게 관리자 권한 부여")
	void updateUserRoleToAdmin_ByAdmin_Success() throws Exception {
		String token = jwtUtil.createToken(adminUser.getUsername(), adminUser.getRole());

		mockMvc.perform(patch("/admin/users/" + targetUser.getId() + "/roles")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.username").value(targetUser.getUsername()))
			.andExpect(jsonPath("$.nickname").value(targetUser.getNickname()))
			.andExpect(jsonPath("$.roles[0].role").value("ADMIN"));

	}

	// - 일반 사용자가 요청할 때 권한 오류가 발생하는지 테스트
	@Test
	@DisplayName("실패 - 일반 사용자가 다른 사용자에게 관리자 권한 부여 시도")
	void updateUserRoleToAdmin_ByNormalUser_Forbidden() throws Exception {
		String token = jwtUtil.createToken(normalUser.getUsername(), normalUser.getRole());

		mockMvc.perform(patch("/admin/users/" + targetUser.getId() + "/roles")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
			.andExpect(status().isForbidden())
			.andExpect(jsonPath("$.error.code").value("ACCESS_DENIED"))
			.andExpect(jsonPath("$.error.message").value("접근 권한이 없습니다."));
	}

	// - 존재하지 않는 사용자에게 권한을 부여하려 할 때 적절한 오류 응답이 반환되는지 테스트
	@Test
	@DisplayName("실패 - 존재하지 않는 사용자 ID에 권한 부여 시도")
	void updateUserRoleToAdmin_TargetUserNotFound() throws Exception {
		String token = jwtUtil.createToken(adminUser.getUsername(), adminUser.getRole());

		Long nonExistentUserId = 9L;

		mockMvc.perform(patch("/admin/users/" + nonExistentUserId + "/roles")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.error.code").value("USER_NOT_FOUND"))
			.andExpect(jsonPath("$.error.message").value("해당 사용자는 존재하지 않습니다."));
	}
}
