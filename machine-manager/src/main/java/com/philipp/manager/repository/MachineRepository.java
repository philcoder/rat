package com.philipp.manager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.philipp.manager.model.Machine;

public interface MachineRepository extends CrudRepository<Machine, Integer> {
	Optional<Machine> findByHostnameAndIpAndPort(String hostname, String ip, int port);

	/**
	 * Find offline Machines with more 30s without heartbeat update.
	 * 
	 * @return
	 */
	@Query(value = "SELECT * FROM machine where last_seen < date_sub(now(), interval 30 second) and online = 1", nativeQuery = true)
	List<Machine> findOfflineMachines();

	/**
	 * Find online Machines, they have with 30s or less seconds last seen update and
	 * didn't goes to offline from.
	 * 
	 * @return
	 */
	@Query(value = "SELECT * FROM machine where online = 1", nativeQuery = true)
	List<Machine> findOnlineMachines();
}
