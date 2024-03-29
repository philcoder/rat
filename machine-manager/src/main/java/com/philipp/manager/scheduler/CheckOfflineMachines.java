package com.philipp.manager.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.philipp.manager.service.MachineRepositoryService;

@Component
public class CheckOfflineMachines implements Job {

	private final Logger logger = LoggerFactory.getLogger(CheckOfflineMachines.class);

	@Autowired
	private MachineRepositoryService machineService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		machineService.findOfflineMachines().forEach(machine -> {
			machine.setOnline(false);
			machineService.save(machine);
			logger.info("Offline Machine " + machine.getHostname() + " <" + machine.getIp() + ":" + machine.getPort()
					+ "> database was updated.");
		});
	}

}
