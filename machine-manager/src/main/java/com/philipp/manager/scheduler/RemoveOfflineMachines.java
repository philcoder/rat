package com.philipp.manager.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.philipp.manager.service.MachineService;

@Component
public class RemoveOfflineMachines implements Job {

	private final Logger logger = LoggerFactory.getLogger(RemoveOfflineMachines.class);

	@Autowired
	private MachineService machineService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		machineService.findOfflineMachines().forEach(machine -> {
			machineService.deleteById(machine.getId());

			String message = "Offline Machine " + machine.getHostname() + " <" + machine.getIp() + ":"
					+ machine.getPort() + "> was removed from database.";
			logger.info(message);
		});
	}

}
