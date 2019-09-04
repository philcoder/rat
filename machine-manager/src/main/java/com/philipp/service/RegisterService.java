package com.philipp.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.philipp.exception.NotFoundMachineException;
import com.philipp.model.Machine;

@Service
public class RegisterService {

	@Autowired
	private MachineService machineService;

	// save or update
	public void register(Machine machine) {
		Machine checkMachine = machineService.findByNameAndIpAndPort(machine.getName(), machine.getIp(),
				machine.getPort());
		if (checkMachine != null) {
			machine.setId(checkMachine.getId());
		}
		machineService.save(machine);
	}

	public void heartbeat(String name, String ip, int port) throws NotFoundMachineException {
		Machine checkMachine = machineService.findByNameAndIpAndPort(name, ip, port);
		if (checkMachine != null) {
			checkMachine.setLastSeen(LocalDateTime.now());
			machineService.save(checkMachine);
		} else {
			throw new NotFoundMachineException("Invalid to update last seen client.");
		}
	}
}
