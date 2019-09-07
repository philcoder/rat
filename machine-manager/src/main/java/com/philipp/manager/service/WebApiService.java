package com.philipp.manager.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.philipp.manager.model.Drive;
import com.philipp.manager.model.Machine;
import com.philipp.manager.model.dto.MachineDto;
import com.philipp.manager.model.dto.NetworkInfoDto;

@Service
public class WebApiService {
	@Autowired
	private MachineService machineService;

	@Autowired
	private ModelMapper modelMapper;

	public List<MachineDto> showOnlineMachines() {
		List<Machine> online = machineService.findOnlineMachines();
		List<MachineDto> list = new ArrayList<>();

		online.forEach(elem -> {
			list.add(convertToDto(elem));
		});

		return list;
	}

	private MachineDto convertToDto(Machine machine) {
		MachineDto machineDto = modelMapper.map(machine, MachineDto.class);
		NetworkInfoDto net = new NetworkInfoDto();
		net.setHostname(machine.getHostname());
		net.setIp(machine.getIp());
		net.setPort(machine.getPort());
		machineDto.setNetworkInfoDto(net);
		machineDto.setStatus(machine.isOnline() ? "online" : "offline");
		aggregateVolumeDrives(machineDto, machine.getDrives());
		return machineDto;
	}

	private void aggregateVolumeDrives(MachineDto dto, List<Drive> drives) {
		long totalBytes = 0L;
		long availableBytes = 0L;
		for (Drive drive : drives) {
			totalBytes += drive.getTotalSpace();
			availableBytes += drive.getAvailableSpace();
		}
		dto.setDiskTotal(humanReadableByteCount(totalBytes, true));
		dto.setDiskAvailable(humanReadableByteCount(availableBytes, true));
	}

	private String humanReadableByteCount(long bytes, boolean si) {
		int unit = si ? 1000 : 1024;
		if (bytes < unit)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
}
