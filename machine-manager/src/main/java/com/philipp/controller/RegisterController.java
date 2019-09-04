package com.philipp.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.philipp.exception.NotFoundMachineException;
import com.philipp.model.Machine;
import com.philipp.service.RegisterService;
import com.philipp.util.ResponseMessage;

@RestController
@RequestMapping("/v1/client/win")
public class RegisterController {
	private final Logger logger = LoggerFactory.getLogger(RegisterController.class);

	@Autowired
	private RegisterService registerService;

	@PostMapping("/register")
	public ResponseEntity<ResponseMessage> register(@Valid @RequestBody Machine machine) {
		try {
			registerService.register(machine);
			String message = "Machine " + machine.getName() + " <" + machine.getIp() + ":" + machine.getPort()
					+ "> registered with successful.";
			logger.info(message);
			return new ResponseEntity<ResponseMessage>(new ResponseMessage(message), HttpStatus.OK);
		} catch (Exception e) {
			String message = "Invalid data for register machine.";
			logger.warn(message, e);
			return new ResponseEntity<ResponseMessage>(new ResponseMessage(message), HttpStatus.NOT_ACCEPTABLE);
		}
	}

	/**
	 * See more info about this feature on:
	 * https://dzone.com/articles/the-mystery-of-eureka-health-monitoring
	 * 
	 * @return
	 */
	@PutMapping("/heartbeat/{name}/{ip}/{port}")
	public ResponseEntity<ResponseMessage> heartbeat(@PathVariable String name, @PathVariable String ip,
			@PathVariable int port) {
		try {
			registerService.heartbeat(name, ip, port);
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("ok"), HttpStatus.OK);
		} catch (NotFoundMachineException e) {
			logger.warn(e.getMessage());
			return new ResponseEntity<ResponseMessage>(new ResponseMessage(e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
		}
	}
}
