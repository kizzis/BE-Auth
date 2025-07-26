package com.server.baro.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.server.baro.common.ErrorCode;
import com.server.baro.common.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<?> handleCustomBaseException(CustomException e) {
		ErrorCode errorCode = e.getErrorCode();

		ErrorResponse errorResponse = ErrorResponse.of(errorCode.name(), errorCode.getErrorMessage());

		return ResponseEntity
			.status(errorCode.getHttpStatus())
			.body(errorResponse);
	}
}
