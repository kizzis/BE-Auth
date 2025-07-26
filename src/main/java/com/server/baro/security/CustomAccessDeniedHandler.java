package com.server.baro.security;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.baro.common.ErrorCode;
import com.server.baro.common.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "AccessDeniedHandler")
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
	private final ObjectMapper objectMapper;

	@Override
	public void handle(
		HttpServletRequest request,
		HttpServletResponse response,
		AccessDeniedException accessDeniedException
	) throws IOException {
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType("application/json; charset=utf-8");

		ErrorCode errorCode = ErrorCode.ACCESS_DENIED;

		ErrorResponse errorResponse = ErrorResponse.of(
			errorCode.name(),
			errorCode.getErrorMessage()
		);

		objectMapper.writeValue(response.getWriter(), errorResponse);
		log.debug("인가 실패 응답 전송 완료. HTTP Status: {}, Error Code: {}", errorCode.getHttpStatus().value(),
			errorCode.name());
	}
}
