package com.philipp.manager.service;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		Machine machineConverted = convertToEntity(machineDto);
		try {
			Machine machine = machineService.findByHostnameAndIpAndPort(machineDto.getNetworkInfoDto().getHostname(),
					machineDto.getNetworkInfoDto().getIp(), machineDto.getNetworkInfoDto().getPort());

			machineConverted.setId(machine.getId());
			machineConverted.setLogHistories(machine.getLogHistories());
		} catch (NotFoundMachineException e) {
			// the 'machineConverted' is new data on database
		}

		Machine saved = machineService.save(machineConverted); // for persist first time
		return saved.getId();
	}

	@Transactional
	public void heartbeat(int id, NetworkInfoDto loginForm) throws NotFoundMachineException {
		Machine machine = machineService.findById(id);
		machine = machineService.findByHostnameAndIpAndPort(loginForm.getHostname(), loginForm.getIp(),
				loginForm.getPort());

		machine.setLastSeen(LocalDateTime.now());
		machine.setOnline(true);
		// machineService.save(machine); //Redundant save() Call for update operations
	}

	private Machine convertToEntity(MachineDto machineDto) {
		Machine machine = modelMapper.map(machineDto, Machine.class);

		machine.setOnline(true);
		machine.setHostname(machineDto.getNetworkInfoDto().getHostname());
		machine.setIp(machineDto.getNetworkInfoDto().getIp());
		machine.setPort(machineDto.getNetworkInfoDto().getPort());

		return machine;
	}
}
