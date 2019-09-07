package com.philipp.manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.philipp.manager.model.dto.MachineDto;
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
}
