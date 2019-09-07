package com.philipp.manager.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.philipp.manager.model.LogHistory;
import com.philipp.manager.model.Machine;

public interface LogHistoryRepository extends CrudRepository<LogHistory, Integer> {
	List<LogHistory> findAllByMachine(Machine machine);
}
