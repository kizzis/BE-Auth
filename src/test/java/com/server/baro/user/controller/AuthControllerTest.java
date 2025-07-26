package com.server.baro.user.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.baro.common.ErrorCode;
import com.server.baro.user.dto.request.UserLoginRequest;
import com.server.baro.user.dto.request.UserSignUpRequest;
import com.server.baro.user.entity.User;
import com.server.baro.user.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepository;

	@Value("${admin.secret.key}")
	private String secretKey;

	// 회원가입
	// - 정상적인 사용자 정보와 이미 가입된 사용자 정보에 대해 테스트
	@Test
	@DisplayName("성공 - 사용자 회원가입")
	void signUp_Success() throws Exception {
		UserSignUpRequest req = new UserSignUpRequest(
			"abc123",
			"12341234",
			"KIM",
			secretKey
		);
		String json = objectMapper.writeValueAsString(req);

		mockMvc.perform(MockMvcRequestBuilders.post("/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
			)
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("실패 - 이미 가입된 사용자 회원가입")
	void signUp_DuplicateUser_Fail() throws Exception {
		userRepository.save(
			User.builder()
				.username("zzz123")
				.password("123456")
				.nickname("Park")
				.build()
		);

		UserSignUpRequest req = new UserSignUpRequest(
			"zzz123",
			"123456",
			"Park",
			""
		);
		String json = objectMapper.writeValueAsString(req);

		mockMvc.perform(MockMvcRequestBuilders.post("/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
			)
			.andExpect(status().isConflict());
	}

	// 로그인
	// - 올바른 자격 증명과 잘못된 자격 증명을 테스트하여 성공/실패 시의 응답 구조가 예상과 동일한지 확인
	@Test
	@DisplayName("성공 - 로그인")
	void login_Success() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(
					new UserSignUpRequest("user1", "1234", "p1", null)
				)))
			.andExpect(status().isOk());

		UserLoginRequest req = new UserLoginRequest("user1", "1234");
		String loginReqBody = objectMapper.writeValueAsString(req);

		mockMvc.perform(MockMvcRequestBuilders.post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(loginReqBody))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.token").isNotEmpty());
	}

	@Test
	@DisplayName("실패 - 잘못된 비밀번호로 로그인 시도")
	void login_Fail() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(
					new UserSignUpRequest("user1", "1234", "p1", null)
				)))
			.andExpect(status().isOk());

		UserLoginRequest req = new UserLoginRequest("user1", "1235");
		String loginReqBody = objectMapper.writeValueAsString(req);

		mockMvc.perform(MockMvcRequestBuilders.post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(loginReqBody))
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.error.code").value(ErrorCode.INVALID_CREDENTIALS.name()))
			.andExpect(jsonPath("$.error.message").value(ErrorCode.INVALID_CREDENTIALS.getErrorMessage()));
	}
}
