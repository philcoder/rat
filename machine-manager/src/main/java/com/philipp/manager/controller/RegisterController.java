package com.philipp.manager.controller;

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

import com.philipp.manager.exception.NotFoundMachineException;
import com.philipp.manager.model.dto.MachineDto;
import com.philipp.manager.model.dto.MachineResponseDto;
import com.philipp.manager.model.dto.NetworkInfoDto;
import com.philipp.manager.model.dto.ResponseDto;
import com.philipp.manager.service.RegisterService;

@RestController
@RequestMapping("/v1/client/win")
public class RegisterController {
	private final Logger logger = LoggerFactory.getLogger(RegisterController.class);

	@Autowired
	private RegisterService registerService;

	@PostMapping("/register")
	public ResponseEntity<ResponseDto> register(@Valid @RequestBody MachineDto machineDto) {
		try {
			int id = registerService.register(machineDto);
			String message = "Machine " + machineDto.getNetworkInfoDto().getHostname() + " <"
					+ machineDto.getNetworkInfoDto().getIp() + ":" + machineDto.getNetworkInfoDto().getPort()
					+ "> registered with successful.";
			logger.info(message);
			return new ResponseEntity<ResponseDto>(new MachineResponseDto(id, message), HttpStatus.OK);
		} catch (Exception e) {
			String message = "Invalid data for register machine.";
			logger.warn(message, e);
			return new ResponseEntity<ResponseDto>(new ResponseDto(message), HttpStatus.NOT_ACCEPTABLE);
		}
	}

	/**
	 * See more info about this feature on:
	 * https://dzone.com/articles/the-mystery-of-eureka-health-monitoring
	 * 
	 * @return
	 */
	@PutMapping("/heartbeat/{id}")
	public ResponseEntity<ResponseDto> heartbeat(@PathVariable int id, @Valid @RequestBody NetworkInfoDto hostInfoDto) {
		try {
			registerService.heartbeat(id, hostInfoDto);
			return new ResponseEntity<ResponseDto>(new ResponseDto("ok"), HttpStatus.OK);
		} catch (NotFoundMachineException e) {
			logger.warn(e.getMessage());
			return new ResponseEntity<ResponseDto>(new ResponseDto(e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
		}
	}
}
