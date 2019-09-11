package com.philipp.manager.util;

import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.philipp.manager.model.LogHistory;
import com.philipp.manager.model.Machine;
import com.philipp.manager.model.dto.DriveDto;
import com.philipp.manager.model.dto.ExecuteCommandDto;
import com.philipp.manager.model.dto.InputDto;
import com.philipp.manager.model.dto.LogHistoryDto;
import com.philipp.manager.model.dto.MachineDto;
import com.philipp.manager.model.dto.MachineLogHistoryDto;
import com.philipp.manager.model.dto.NetworkInfoDto;

public final class DefaultModel {

	private static final ModelMapper modelMapper = new ModelMapper();

	private static final ObjectMapper objectMapper = new ObjectMapper();

	private DefaultModel() {
	}

	public static MachineDto getMachineDto() {
		MachineDto dto = new MachineDto();
		dto.setId(12345);
		dto.setNetworkInfoDto(getNetworkInfoDto());

		dto.setAntivirus(true);
		dto.setFirewall(false);
		dto.setWindowsVersion("Windows X");
		dto.setDotNetVersion("4.0.30319.42000");
		dto.getDrives().add(getDriveDto());
		return dto;
	}

	public static Machine getMachine() {
		return convertToEntity(getMachineDto());
	}

	public static NetworkInfoDto getNetworkInfoDto() {
		NetworkInfoDto dto = new NetworkInfoDto();
		dto.setHostname("PHIL");
		dto.setIp("192.168.0.200");
		dto.setPort(12345);
		return dto;
	}

	public static DriveDto getDriveDto() {
		DriveDto dto = new DriveDto();
		dto.setName("X:\\");
		dto.setAvailableSpace(4770430976L);
		dto.setTotalSpace(25958948864L);
		return dto;
	}

	public static Machine convertToEntity(MachineDto machineDto) {
		Machine machine = modelMapper.map(machineDto, Machine.class);

		machine.setOnline(true);
		machine.setHostname(machineDto.getNetworkInfoDto().getHostname());
		machine.setIp(machineDto.getNetworkInfoDto().getIp());
		machine.setPort(machineDto.getNetworkInfoDto().getPort());

		return machine;
	}

	public static LogHistory getLogHistory(Machine machine) {
		LogHistory logHistory = modelMapper.map(getLogHistoryDto(), LogHistory.class);
		logHistory.setId(0);
		logHistory.setMachine(machine);
		return logHistory;
	}

	public static LogHistory getLogHistory() {
		return modelMapper.map(getLogHistoryDto(), LogHistory.class);
	}

	public static LogHistoryDto getLogHistoryDto() {
		LogHistoryDto dto = new LogHistoryDto();
		dto.setCommands("dir C://");
		dto.setId(600);
		dto.setOutputs(getOutputs());
		return dto;
	}

	public static List<String> getOutputs() {
		return Arrays.asList("'5', '    229      20     3800       5144      28,56   3540   0 AppleMobileDeviceService",
				"'5', '    173       8     6688       8092       0,11   4076   5 bash");
	}

	public static ExecuteCommandDto getExecuteCommandDto() {
		ExecuteCommandDto dto = new ExecuteCommandDto();
		String cmds = "powershell Get-process";
		dto.setCommands(cmds);
		dto.setOutputs(getOutputs());
		return dto;
	}

	public static String getExecuteCommandDtoToJsonString() throws JsonProcessingException {
		return getJsonStringFromObject(getExecuteCommandDto());
	}

	public static String getJsonStringFromObject(Object obj) throws JsonProcessingException {
		return objectMapper.writeValueAsString(obj);
	}

	public static InputDto getInputDto() {
		InputDto dto = new InputDto();
		dto.setCommands("cd c:// && dir");
		dto.getMachineIdsList().add(1);
		dto.getMachineIdsList().add(2);

		return dto;
	}

	public static MachineLogHistoryDto getMachineLogHistoryDto() {
		MachineLogHistoryDto dto = new MachineLogHistoryDto();
		dto.setNetInfo(getNetworkInfoDto());
		LogHistoryDto historyDto = new LogHistoryDto();
		historyDto.setCommands("cd c:// && dir");
		historyDto.getOutputs().add("02/07/2017  23:33    <DIR>          MATS");
		historyDto.getOutputs().add("04/09/2019  11:46    <DIR>          Program Files (x86)");
		dto.getLogs().add(historyDto);
		historyDto = new LogHistoryDto();
		historyDto.setCommands("cd c:// && dir");
		historyDto.getOutputs().add("04/09/2019  11:46    <DIR>          Program Files (x86)");
		historyDto.getOutputs().add("11/06/2019  13:59    <DIR>          Windows.old");
		dto.getLogs().add(historyDto);

		return dto;
	}
}
