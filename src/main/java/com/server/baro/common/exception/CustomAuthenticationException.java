package com.server.baro.common.exception;

import org.springframework.security.core.AuthenticationException;

import com.server.baro.common.ErrorCode;

import lombok.Getter;

@Getter
public class CustomAuthenticationException extends AuthenticationException {
	private final ErrorCode errorCode;

	public CustomAuthenticationException(ErrorCode errorCode) {
		super(errorCode.getErrorMessage());
		this.errorCode = errorCode;
	}
}
