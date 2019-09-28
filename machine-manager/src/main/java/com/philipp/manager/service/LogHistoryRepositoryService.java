package com.philipp.manager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.philipp.manager.exception.NotFoundLogHistoryException;
import com.philipp.manager.model.LogHistory;
import com.philipp.manager.model.Machine;
import com.philipp.manager.repository.LogHistoryRepository;

@Service
public class LogHistoryRepositoryService extends AbstractRepositoryService<LogHistory, LogHistoryRepository> {
	public List<LogHistory> findAllByMachine(Machine machine) {
		return repository.findAllByMachine(machine);
	}

	@Override
	public LogHistory findById(Integer id) throws NotFoundLogHistoryException {
		Optional<LogHistory> optional = repository.findById(id);
		if (optional.isEmpty()) {
			throw new NotFoundLogHistoryException("Not found log history for id: " + id);
		}
		return optional.get();
	}
}
