package com.philipp.manager.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.philipp.manager.model.LogHistory;
import com.philipp.manager.model.Machine;
import com.philipp.manager.repository.LogHistoryRepository;

@Service
public class LogHistoryService extends AbstractService<LogHistory, LogHistoryRepository> {
	public List<LogHistory> findAllByMachine(Machine machine) {
		return repository.findAllByMachine(machine);
	}
}
