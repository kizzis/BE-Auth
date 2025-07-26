package com.server.baro.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.baro.common.ErrorCode;
import com.server.baro.common.ErrorResponse;
import com.server.baro.common.exception.CustomAuthenticationException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "AuthenticationEntryPoint")
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
	private final ObjectMapper objectMapper;

	@Override
	public void commence(
		HttpServletRequest request,
		HttpServletResponse response,
		AuthenticationException authException
	) throws IOException {
		response.setContentType("application/json; charset=utf-8");

		ErrorCode errorCode;
		if (authException instanceof CustomAuthenticationException e) {
			errorCode = e.getErrorCode();
			log.warn("인증 실패 - CustomAuthenticationException: {}", errorCode);
		} else {
			errorCode = ErrorCode.UNAUTHORIZED;
			log.error("일반 인증 실패 - AuthenticationException: {}", errorCode);
		}

		response.setStatus(errorCode.getHttpStatus().value());

		ErrorResponse errorResponse = ErrorResponse.of(
			errorCode.name(),
			errorCode.getErrorMessage()
		);

		objectMapper.writeValue(response.getWriter(), errorResponse);
		log.debug("인증 실패 응답 전송 완료. HTTP Status: {}, Error Code: {}", errorCode.getHttpStatus().value(),
			errorCode.name());
	}
}
