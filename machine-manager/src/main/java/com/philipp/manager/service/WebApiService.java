package com.philipp.manager.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.philipp.manager.exception.ExecuteRemoteCommandException;
import com.philipp.manager.exception.NotFoundLogHistoryException;
import com.philipp.manager.exception.NotFoundMachineException;
import com.philipp.manager.model.Drive;
import com.philipp.manager.model.LogHistory;
import com.philipp.manager.model.Machine;
import com.philipp.manager.model.dto.InputDto;
import com.philipp.manager.model.dto.LogHistoryDto;
import com.philipp.manager.model.dto.MachineDto;
import com.philipp.manager.model.dto.MachineLogHistoryDto;
import com.philipp.manager.model.dto.NetworkInfoDto;

@Service
public class WebApiService {
	@Autowired
	private LogHistoryService logHistoryService;

	@Autowired
	private MachineService machineService;

	@Autowired
	private RestClientService restClientService;

	@Autowired
	private ModelMapper modelMapper;

	public List<MachineDto> showOnlineMachines() {
		return convertMachineListToListDto(machineService.findOnlineMachines());
	}

	public List<MachineLogHistoryDto> executeCommands(InputDto inputDto) {
		List<MachineLogHistoryDto> response = new ArrayList<>();

		for (Integer id : inputDto.getMachineIdsList()) {
			MachineLogHistoryDto machineLogHistory = new MachineLogHistoryDto();

			Machine machine = null;
			LogHistory logHistory = null;
			try {
				machine = machineService.findById(id);
				if (machine.isOnline()) {
					logHistory = restClientService.executeRemoteCommand(inputDto.getCommands(), machine);
					logHistoryService.save(logHistory);
				} else {
					machineLogHistory.setMessage("Machine " + machine + " is offline");
				}
			} catch (NotFoundMachineException e) {
				machineLogHistory.setMessage(e.getMessage());
				machine = new Machine("NONE", "0.0.0.0", 0);
			} catch (ExecuteRemoteCommandException e) {
				machineLogHistory.setMessage(e.getMessage());
			}

			if (logHistory == null) {
				logHistory = new LogHistory();
				logHistory.setCommands(inputDto.getCommands());
			}

			machineLogHistory.getLogs().add(modelMapper.map(logHistory, LogHistoryDto.class));
			machineLogHistory.setNetInfo(convertMachineToNetworkInfoDto(machine));
			response.add(machineLogHistory);
		}
		return response;

	}

	public List<MachineDto> historyShowAllMachines() {
		return convertMachineListToListDto(machineService.findAll());
	}

	public MachineLogHistoryDto historyShowMachineLogs(int id) throws NotFoundMachineException {
		List<LogHistory> logs = logHistoryService.findAllByMachine(new Machine(id));
		List<LogHistoryDto> logDtos = convertLogHistoryListToListDto(logs);

		MachineLogHistoryDto machineLogHistory = new MachineLogHistoryDto();
		if (!logDtos.isEmpty()) {
			machineLogHistory.setLogs(logDtos);
			machineLogHistory.setNetInfo(convertMachineToNetworkInfoDto(logs.get(0).getMachine()));
		} else {
			Machine machine = machineService.findById(id);
			machineLogHistory.setNetInfo(convertMachineToNetworkInfoDto(machine));
		}

		return machineLogHistory;
	}

	public LogHistoryDto historyShowMachineLogOutput(int id) throws NotFoundLogHistoryException {
		LogHistoryDto output = new LogHistoryDto();
		LogHistory findLog = logHistoryService.findById(id);
		output.setCommands(findLog.getCommands());
		output.setOutputs(findLog.getOutputs());
		return output;
	}

	private List<LogHistoryDto> convertLogHistoryListToListDto(List<LogHistory> logs) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		List<LogHistoryDto> list = new ArrayList<>();
		logs.forEach(elem -> {
			LogHistoryDto dto = new LogHistoryDto();
			dto.setId(elem.getId());
			dto.setCommands(elem.getCommands());
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
