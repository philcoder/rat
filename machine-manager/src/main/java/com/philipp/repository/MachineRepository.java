package com.philipp.repository;

import org.springframework.data.repository.CrudRepository;

import com.philipp.model.Machine;

public interface MachineRepository extends CrudRepository<Machine, Integer> {
	Machine findByNameAndIpAndPort(String name, String ip, int port);

//	List<Machine> findByOnline(boolean online);
}
