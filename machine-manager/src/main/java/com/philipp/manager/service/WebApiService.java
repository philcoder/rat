package com.philipp.manager.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.philipp.manager.model.Drive;
import com.philipp.manager.model.LogHistory;
import com.philipp.manager.model.Machine;
import com.philipp.manager.model.dto.LogHistoryDto;
import com.philipp.manager.model.dto.MachineDto;
import com.philipp.manager.model.dto.MachineLogHistoryDto;
import com.philipp.manager.model.dto.NetworkInfoDto;
import com.philipp.manager.model.dto.OutputDto;

@Service
public class WebApiService {
	@Autowired
	private LogHistoryService logHistoryService;

	@Autowired
	private MachineService machineService;

	@Autowired
	private ModelMapper modelMapper;

	public List<MachineDto> showOnlineMachines() {
		return convertMachineListToListDto(machineService.findOnlineMachines());
	}

	public List<MachineDto> showAllMachines() {
		return convertMachineListToListDto(machineService.findAll());
	}

	public MachineLogHistoryDto showMachineLogs(int id) {
		List<LogHistory> logs = logHistoryService.findAllByMachine(new Machine(id));
		List<LogHistoryDto> logDtos = convertLogHistoryListToListDto(logs);

		MachineLogHistoryDto machineLogHistory = new MachineLogHistoryDto();
		if (logDtos.isEmpty()) {
			Optional<Machine> machine = machineService.findById(id);
			if (!machine.isEmpty()) {
				machineLogHistory.setNetInfo(convertMachineToNetworkInfoDto(machine.get()));
			}
		} else {
			machineLogHistory.setLogs(logDtos);
			machineLogHistory.setNetInfo(convertMachineToNetworkInfoDto(logs.get(0).getMachine()));
		}

		return machineLogHistory;
	}

	public OutputDto showMachineLogOutput(int id) {
		OutputDto output = new OutputDto();
		Optional<LogHistory> findLog = logHistoryService.findById(id);
		if (!findLog.isEmpty()) {
			output.setCommand(findLog.get().getCommand());
			output.setOutputs(findLog.get().getOutputs());
		}

		return output;
	}

	private List<LogHistoryDto> convertLogHistoryListToListDto(List<LogHistory> logs) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		List<LogHistoryDto> list = new ArrayList<>();
		logs.forEach(elem -> {
			LogHistoryDto dto = modelMapper.map(elem, LogHistoryDto.class);
			dto.setDateTime(elem.getDateTime().format(formatter));
			list.add(dto);
		});
		return list;
	}

	private List<MachineDto> convertMachineListToListDto(List<Machine> machines) {
		List<MachineDto> list = new ArrayList<>();
		machines.forEach(elem -> {
			list.add(convertMachineToMachineDto(elem));
		});
		return list;
	}

	private MachineDto convertMachineToMachineDto(Machine machine) {
		MachineDto machineDto = modelMapper.map(machine, MachineDto.class);
		machineDto.setNetworkInfoDto(convertMachineToNetworkInfoDto(machine));
		machineDto.setStatus(machine.isOnline() ? "online" : "offline");
		aggregateVolumeDrives(machineDto, machine.getDrives());
		machineDto.getDrives().clear();
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

	private NetworkInfoDto convertMachineToNetworkInfoDto(Machine machine) {
		NetworkInfoDto net = new NetworkInfoDto();
		net.setHostname(machine.getHostname());
		net.setIp(machine.getIp());
		net.setPort(machine.getPort());
		return net;
	}
}
