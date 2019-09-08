package com.philipp.manager.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class LogHistoryDto extends ExecuteCommandDto {
	@JsonInclude(Include.NON_NULL)
	private Integer id;

	@JsonInclude(Include.NON_NULL)
	private String dateTime;

	public LogHistoryDto() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
}
