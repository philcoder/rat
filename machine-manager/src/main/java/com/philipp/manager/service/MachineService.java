package com.philipp.manager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.philipp.manager.model.Machine;
import com.philipp.manager.repository.MachineRepository;

@Service
public class MachineService {

	@Autowired
	private MachineRepository machineRepository;

	public Machine save(Machine machine) {
		return machineRepository.save(machine);
	}

	public Optional<Machine> findById(int id) {
		return machineRepository.findById(id);
	}

	public Optional<Machine> findByHostnameAndIpAndPort(String hostname, String ip, int port) {
		return machineRepository.findByHostnameAndIpAndPort(hostname, ip, port);
	}

	public List<Machine> findOfflineMachines() {
		return machineRepository.findOfflineMachines();
	}

	public List<Machine> findOnlineMachines() {
		return machineRepository.findOnlineMachines();
	}

	public void deleteById(Integer id) {
		machineRepository.deleteById(id);
	}

}
