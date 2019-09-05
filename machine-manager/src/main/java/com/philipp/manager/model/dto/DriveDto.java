package com.philipp.manager.model.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class DriveDto {

	@NotEmpty
	@Size(max = 32, message = "Drive 'name' must be a maximum of 32 characters")
	private String name;

	@Min(0)
	private long availableSpace;

	@Min(0)
	private long totalSpace;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getAvailableSpace() {
		return availableSpace;
	}

	public void setAvailableSpace(long availableSpace) {
		this.availableSpace = availableSpace;
	}

	public long getTotalSpace() {
		return totalSpace;
	}

	public void setTotalSpace(long totalSpace) {
		this.totalSpace = totalSpace;
	}
}
