package com.philipp.manager.model.dto;

import java.util.ArrayList;
import java.util.List;

public class MachineLogHistoryDto {

	private NetworkInfoDto netInfo;

	private List<LogHistoryDto> logs;

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
}
