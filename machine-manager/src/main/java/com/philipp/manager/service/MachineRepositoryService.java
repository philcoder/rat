package com.philipp.manager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.philipp.manager.exception.NotFoundMachineException;
import com.philipp.manager.model.Machine;
import com.philipp.manager.repository.MachineRepository;

@Service
public class MachineRepositoryService extends AbstractRepositoryService<Machine, MachineRepository> {

	@Override
	public Machine findById(Integer id) throws NotFoundMachineException {
		Optional<Machine> optional = repository.findById(id);
		if (optional.isEmpty()) {
			throw new NotFoundMachineException("Not found machine for id: " + id);
		}
		return optional.get();
	}

	public Machine findByHostnameAndIpAndPort(String hostname, String ip, int port) throws NotFoundMachineException {
		Optional<Machine> optional = repository.findByHostnameAndIpAndPort(hostname, ip, port);
		if (optional.isEmpty()) {
			throw new NotFoundMachineException("Not found machine: " + "[" + hostname + "] " + ip + ":" + port);
		}
		return optional.get();
	}

	public List<Machine> findOfflineMachines() {
		return repository.findOfflineMachines();
	}

	public List<Machine> findOnlineMachines() {
		return repository.findOnlineMachines();
	}
}
