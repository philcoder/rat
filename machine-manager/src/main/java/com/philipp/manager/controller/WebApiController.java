package com.philipp.manager.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.philipp.manager.model.dto.InputDto;
import com.philipp.manager.model.dto.LogHistoryDto;
import com.philipp.manager.model.dto.MachineDto;
import com.philipp.manager.model.dto.MachineLogHistoryDto;
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
	public List<MachineLogHistoryDto> executeCommands(@Valid @RequestBody InputDto inputDto) {
		return webApiService.executeCommands(inputDto);
	}

	@GetMapping("/history/show/all/machines")
	public List<MachineDto> historyShowAllMachines() {
		return webApiService.historyShowAllMachines();
	}

	@GetMapping("/history/show/machine/logs/{machine_id}")
	public MachineLogHistoryDto historyShowMachineLogs(@PathVariable("machine_id") int id) {
		return webApiService.historyShowMachineLogs(id);
	}

	@GetMapping("/history/show/machine/log/{log_id}/output")
	public LogHistoryDto historyShowMachineLogOutput(@PathVariable("log_id") int id) {
		return webApiService.historyShowMachineLogOutput(id);
	}
}
