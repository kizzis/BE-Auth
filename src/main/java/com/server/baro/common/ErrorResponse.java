package com.server.baro.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {
	private final ErrorDetail error;

	public static ErrorResponse of(String code, String message) {
		return new ErrorResponse(new ErrorDetail(code, message));
	}

	@Getter
	@RequiredArgsConstructor
	public static class ErrorDetail {
		private final String code;
		private final String message;
	}
}
