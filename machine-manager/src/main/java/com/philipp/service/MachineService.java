package com.philipp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.philipp.model.Machine;
import com.philipp.repository.MachineRepository;

@Service
public class MachineService {

	@Autowired
	private MachineRepository machineRepository;

	public void save(Machine machine) {
		machineRepository.save(machine);
	}

	public Machine findByNameAndIpAndPort(String name, String ip, int port) {
		return machineRepository.findByNameAndIpAndPort(name, ip, port);
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
