package com.philipp.manager.model.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class MachineLogHistoryDto {

	private NetworkInfoDto netInfo;

	private List<LogHistoryDto> logs;

	@JsonInclude(Include.NON_NULL)
	private String message;

	public MachineLogHistoryDto() {
		netInfo = new NetworkInfoDto();
		logs = new ArrayList<>();
	}

	public NetworkInfoDto getNetInfo() {
		return netInfo;
	}

	public void setNetInfo(NetworkInfoDto netInfo) {
		this.netInfo = netInfo;
	}

	public List<LogHistoryDto> getLogs() {
		return logs;
	}

	public void setLogs(List<LogHistoryDto> logs) {
		this.logs = logs;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
