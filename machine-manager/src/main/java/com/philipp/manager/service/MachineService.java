package com.philipp.manager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.philipp.manager.model.Machine;
import com.philipp.manager.repository.MachineRepository;

@Service
public class MachineService extends AbstractService<Machine, MachineRepository> {

	public Optional<Machine> findByHostnameAndIpAndPort(String hostname, String ip, int port) {
		return repository.findByHostnameAndIpAndPort(hostname, ip, port);
	}

	public List<Machine> findOfflineMachines() {
		return repository.findOfflineMachines();
	}

	public List<Machine> findOnlineMachines() {
		return repository.findOnlineMachines();
	}
}
