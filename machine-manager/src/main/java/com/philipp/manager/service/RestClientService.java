package com.philipp.manager.service;

import java.time.Duration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.philipp.manager.exception.ExecuteRemoteCommandException;
import com.philipp.manager.model.LogHistory;
import com.philipp.manager.model.Machine;
import com.philipp.manager.model.dto.ExecuteCommandDto;

@Service
public class RestClientService {
	private final Logger logger = LoggerFactory.getLogger(RestClientService.class);

	private final RestTemplate restTemplate;

	@Autowired
	public RestClientService(RestTemplateBuilder builder) {
		this.restTemplate = builder.setConnectTimeout(Duration.ofSeconds(3)).setReadTimeout(Duration.ofSeconds(120))
				.build();
	}

	public LogHistory executeRemoteCommand(String commands, Machine machine) throws ExecuteRemoteCommandException {
		try {
			ResponseEntity<ExecuteCommandDto> response = connectToRestApi(commands, machine);
			logger.warn("Execution: " + response);
			switch (response.getStatusCode()) {
			case OK:
				List<String> outputs = response.getBody().getOutputs();
				if (!outputs.isEmpty()) {
					LogHistory logHistory = new LogHistory();
					logHistory.setMachine(machine);
					logHistory.setCommands(commands);
					logHistory.setOutputs(outputs);
					return logHistory;
				} else {
					throw new ExecuteRemoteCommandException("Machine " + machine + " generate a empty output");
				}

			default:
				throw new ExecuteRemoteCommandException("Machine " + machine + " failed to connect");
			}
		} catch (Exception e) {
			logger.warn("Execution " + e.getMessage());
			throw new ExecuteRemoteCommandException("Machine " + machine + " failed to connect", e);
		}
	}

	private ResponseEntity<ExecuteCommandDto> connectToRestApi(String commands, Machine machine) {
		String url = "http://" + machine.getIp() + ":" + machine.getPort() + "/watcher/v1/terminal";

		ExecuteCommandDto executeCommandDto = new ExecuteCommandDto();
		executeCommandDto.setCommands(commands);

		HttpEntity<ExecuteCommandDto> request = new HttpEntity<>(executeCommandDto);
		return restTemplate.exchange(url, HttpMethod.POST, request, ExecuteCommandDto.class);
	}

}
