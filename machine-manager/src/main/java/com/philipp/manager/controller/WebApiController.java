package com.philipp.manager.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.philipp.manager.exception.NotFoundLogHistoryException;
import com.philipp.manager.exception.NotFoundMachineException;
import com.philipp.manager.model.dto.InputDto;
import com.philipp.manager.model.dto.LogHistoryDto;
import com.philipp.manager.model.dto.MachineDto;
import com.philipp.manager.model.dto.MachineLogHistoryDto;
import com.philipp.manager.model.dto.ResponseDto;
import com.philipp.manager.service.WebApiService;

@RestController
@RequestMapping("/web/api")
public class WebApiController {

	@Autowired
	private WebApiService webApiService;

	@GetMapping("/show/online/machines")
	public List<MachineDto> showOnlineMachines() {
		return webApiService.showOnlineMachines();
	}

	@PostMapping("/execute/online/machines/commands")
	public ResponseEntity<?> executeCommands(@Valid @RequestBody InputDto inputDto) {
		return new ResponseEntity<List<MachineLogHistoryDto>>(webApiService.executeCommands(inputDto), HttpStatus.OK);
	}

	@GetMapping("/history/show/all/machines")
	public List<MachineDto> historyShowAllMachines() {
		return webApiService.historyShowAllMachines();
	}

	@GetMapping("/history/show/machine/logs/{machine_id}")
	public ResponseEntity<?> historyShowMachineLogs(@PathVariable("machine_id") int id) {
		try {
			return new ResponseEntity<MachineLogHistoryDto>(webApiService.historyShowMachineLogs(id), HttpStatus.OK);
		} catch (NotFoundMachineException e) {
			return new ResponseEntity<ResponseDto>(new ResponseDto(e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
		}
	}

	@GetMapping("/history/show/machine/log/{log_id}/output")
	public ResponseEntity<?> historyShowMachineLogOutput(@PathVariable("log_id") int id) {
		try {
			return new ResponseEntity<LogHistoryDto>(webApiService.historyShowMachineLogOutput(id), HttpStatus.OK);
		} catch (NotFoundLogHistoryException e) {
			return new ResponseEntity<ResponseDto>(new ResponseDto(e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
		}
	}
}
