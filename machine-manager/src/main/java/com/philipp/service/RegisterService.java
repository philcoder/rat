package com.philipp.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.philipp.exception.NotFoundMachineException;
import com.philipp.model.Machine;
import com.philipp.repository.MachineRepository;

@Service
public class RegisterService {

	@Autowired
	private MachineRepository machineRepository;

	// save or update
	public void register(Machine machine) {
		Machine checkMachine = machineRepository.findByNameAndIpAndPort(machine.getName(), machine.getIp(),
				machine.getPort());
		if (checkMachine != null) {
			machine.setId(checkMachine.getId());
		}

		saveOrUpdate(machine);
	}

	public void heartbeat(String name, String ip, int port) throws NotFoundMachineException {
		Machine checkMachine = machineRepository.findByNameAndIpAndPort(name, ip, port);
		if (checkMachine != null) {
			checkMachine.setLastSeen(LocalDateTime.now());
			saveOrUpdate(checkMachine);
		} else {
			throw new NotFoundMachineException("Invalid to update last seen client.");
		}
	}

	private void saveOrUpdate(Machine machine) {
		machineRepository.save(machine);
	}
}
