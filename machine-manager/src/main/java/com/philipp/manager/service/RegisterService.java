package com.philipp.manager.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.philipp.manager.exception.NotFoundMachineException;
import com.philipp.manager.model.Machine;
import com.philipp.manager.model.dto.MachineDto;
import com.philipp.manager.model.dto.NetworkInfoDto;

@Service
public class RegisterService {

	@Autowired
	private MachineService machineService;

	@Autowired
	private ModelMapper modelMapper;

	// save or update
	public int register(MachineDto machineDto) {
		Optional<Machine> optionalMachine = machineService.findByHostnameAndIpAndPort(
				machineDto.getNetworkInfoDto().getHostname(), machineDto.getNetworkInfoDto().getIp(),
				machineDto.getNetworkInfoDto().getPort());

		Machine machine = convertToEntity(machineDto);
		if (!optionalMachine.isEmpty()) {
			machine.setId(optionalMachine.get().getId());
		}
		Machine saved = machineService.save(machine);
		return saved.getId();
	}

	public void heartbeat(int id, NetworkInfoDto loginForm) throws NotFoundMachineException {
		Optional<Machine> optionalMachine = machineService.findById(id);
		if (optionalMachine.isEmpty()) {
			throw new NotFoundMachineException("Invalid id, the host need to register again.");
		}

		optionalMachine = machineService.findByHostnameAndIpAndPort(loginForm.getHostname(), loginForm.getIp(),
				loginForm.getPort());
		if (optionalMachine.isEmpty()) {
			throw new NotFoundMachineException("Some attributes changes on host and need to register again.");
		}

		Machine machine = optionalMachine.get();
		machine.setLastSeen(LocalDateTime.now());
		machineService.save(machine);
	}

	private Machine convertToEntity(MachineDto machineDto) {
		Machine machine = modelMapper.map(machineDto, Machine.class);

		machine.setHostname(machineDto.getNetworkInfoDto().getHostname());
		machine.setIp(machineDto.getNetworkInfoDto().getIp());
		machine.setPort(machineDto.getNetworkInfoDto().getPort());
		return machine;
	}
}
