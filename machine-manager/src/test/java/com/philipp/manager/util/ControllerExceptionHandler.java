package com.philipp.manager.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerExceptionHandler {
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public ErrorResult handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
		ErrorResult errorResult = new ErrorResult();

		for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
			errorResult.getFieldErrors()
					.add(new FieldValidationError(fieldError.getField(), fieldError.getDefaultMessage()));
		}

		return errorResult;
	}

	static class ErrorResult {
		private final List<FieldValidationError> fieldErrors = new ArrayList<>();

		public ErrorResult() {
		}

		public ErrorResult(String field, String message) {
			this.fieldErrors.add(new FieldValidationError(field, message));
		}

		public List<FieldValidationError> getFieldErrors() {
			return fieldErrors;
		}
	}

	static class FieldValidationError {
		private String field;
		private String message;

		public FieldValidationError() {
			this(null, null);
		}

		public FieldValidationError(String field, String message) {
			this.field = field;
			this.message = message;
		}

		public String getField() {
			return field;
		}

		public String getMessage() {
			return message;
		}
	}
}
