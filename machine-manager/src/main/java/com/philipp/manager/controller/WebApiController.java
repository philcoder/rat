package com.philipp.manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.philipp.manager.model.dto.MachineDto;
import com.philipp.manager.model.dto.MachineLogHistoryDto;
import com.philipp.manager.model.dto.OutputDto;
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

	@GetMapping("/show/all/machines")
	public List<MachineDto> showAllMachines() {
		return webApiService.showAllMachines();
	}

	@GetMapping("/show/machine/logs/{machine_id}")
	public MachineLogHistoryDto showMachineLogs(@PathVariable("machine_id") int id) {
		return webApiService.showMachineLogs(id);
	}

	@GetMapping("/show/machine/log/output/{log_id}")
	public OutputDto showMachineLogOutput(@PathVariable("log_id") int id) {
		return webApiService.showMachineLogOutput(id);
	}
}
