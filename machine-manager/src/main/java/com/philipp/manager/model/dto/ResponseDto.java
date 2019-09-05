package com.philipp.manager.model.dto;

public class ResponseDto {
	private String message;

	public ResponseDto() {
		this("");
	}

	public ResponseDto(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}