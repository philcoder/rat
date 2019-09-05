package com.philipp.manager.model.dto;

public class MachineResponseDto extends ResponseDto {
	private int id;

	public MachineResponseDto(int id, String message) {
		super(message);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
