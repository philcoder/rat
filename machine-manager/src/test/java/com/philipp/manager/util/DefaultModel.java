package com.philipp.manager.util;

import java.util.Arrays;

import org.modelmapper.ModelMapper;

import com.philipp.manager.model.LogHistory;
import com.philipp.manager.model.Machine;
import com.philipp.manager.model.dto.DriveDto;
import com.philipp.manager.model.dto.LogHistoryDto;
import com.philipp.manager.model.dto.MachineDto;
import com.philipp.manager.model.dto.NetworkInfoDto;

public final class DefaultModel {

	private static final ModelMapper modelMapper = new ModelMapper();

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

	public static LogHistory getLogHistory() {
		return modelMapper.map(getLogHistoryDto(), LogHistory.class);
	}

	public static LogHistoryDto getLogHistoryDto() {
		LogHistoryDto dto = new LogHistoryDto();
		dto.setCommands("dir C://");
		dto.setId(600);
		dto.setOutputs(Arrays.asList(
				"'5', '    229      20     3800       5144      28,56   3540   0 AppleMobileDeviceService",
				"'5', '    173       8     6688       8092       0,11   4076   5 bash"));
		return dto;
	}
}
