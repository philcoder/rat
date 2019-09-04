package com.philipp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.philipp.model.Machine;

public interface MachineRepository extends CrudRepository<Machine, Integer> {
	Machine findByNameAndIpAndPort(String name, String ip, int port);

	/**
	 * Find offline Machines with more 30s without heartbeat update.
	 * 
	 * @return
	 */
	@Query(value = "SELECT * FROM machine where last_seen < date_sub(now(), interval 30 second)", nativeQuery = true)
	List<Machine> findOfflineMachines();

	/**
	 * Find online Machines with 30s or less, last seen update.
	 * 
	 * @return
	 */
	@Query(value = "SELECT * FROM machine where last_seen >= date_sub(now(), interval 30 second)", nativeQuery = true)
	List<Machine> findOnlineMachines();
}
