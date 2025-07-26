package com.server.baro.common;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	USER_ALREADY_EXISTS("이미 가입된 사용자입니다.", HttpStatus.CONFLICT),
	USER_NOT_FOUND("해당 사용자는 존재하지 않습니다.", HttpStatus.NOT_FOUND),
	INVALID_CREDENTIALS("아이디 또는 비밀번호가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED),
	ACCESS_DENIED("접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
	INVALID_ADMIN_SECRET_KEY("관리자 암호가 틀려 회원가입이 불가능합니다.", HttpStatus.FORBIDDEN),

	INVALID_TOKEN("유효하지 않은 인증 토큰입니다.", HttpStatus.UNAUTHORIZED),
	TOKEN_NOT_FOUND("토큰이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED),
	TOKEN_EXPIRED("토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
	UNAUTHORIZED("인증에 실패했습니다.", HttpStatus.UNAUTHORIZED);

	private final String errorMessage;
	private final HttpStatus httpStatus;

}
