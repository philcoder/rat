package com.philipp.manager.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.philipp.manager.exception.NotFoundLogHistoryException;
import com.philipp.manager.exception.NotFoundMachineException;
import com.philipp.manager.model.dto.InputDto;
import com.philipp.manager.model.dto.LogHistoryDto;
import com.philipp.manager.model.dto.MachineDto;
import com.philipp.manager.model.dto.MachineLogHistoryDto;
import com.philipp.manager.service.WebApiService;
import com.philipp.manager.util.DefaultModel;
import com.philipp.manager.util.RandomSpringJUnit4ClassRunner;

@RunWith(RandomSpringJUnit4ClassRunner.class)
@WebMvcTest(WebApiController.class)
public class WebApiControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	@MockBean
	private WebApiService webApiServiceMock;

	@Test
	public void showOnlineMachines() throws Exception {
		List<MachineDto> returnedList = new ArrayList<>();
		returnedList.add(DefaultModel.getMachineDto());
		returnedList.add(DefaultModel.getMachineDto());
		returnedList.add(DefaultModel.getMachineDto());
		when(webApiServiceMock.showOnlineMachines()).thenReturn(returnedList);

		String json = mapper.writeValueAsString(returnedList);
		MockHttpServletRequestBuilder httpServletRequest = get("/web/api/show/online/machines").content(json)
				.contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(httpServletRequest).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(12345)).andExpect(jsonPath("$[0].ip").value("192.168.0.200"));

	}

	@Test
	public void executeCommandsWithSuccess() throws Exception {
		InputDto dto = DefaultModel.getInputDto();

		List<MachineLogHistoryDto> returnedList = new ArrayList<>();
		returnedList.add(DefaultModel.getMachineLogHistoryDto());
		returnedList.add(DefaultModel.getMachineLogHistoryDto());
		when(webApiServiceMock.executeCommands(ArgumentMatchers.any(InputDto.class))).thenReturn(returnedList);

		MockHttpServletRequestBuilder httpServletRequest = post("/web/api/execute/online/machines/commands")
				.content(mapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON);

		// comeback 2 itens
		mockMvc.perform(httpServletRequest).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$[1].netInfo.port").value(12345))
				.andExpect(jsonPath("$[1].netInfo.ip").value("192.168.0.200"));
	}

	@Test
	public void executeCommandsWithRequiredFieldEmpty() throws Exception {
		InputDto dto = DefaultModel.getInputDto();
		dto.setCommands(null);

		when(webApiServiceMock.executeCommands(ArgumentMatchers.any(InputDto.class))).thenReturn(new ArrayList<>());

		MockHttpServletRequestBuilder httpServletRequest = post("/web/api/execute/online/machines/commands")
				.content(mapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON);

		String responseMessage = "must not be empty";
		mockMvc.perform(httpServletRequest).andDo(print()).andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
				.andExpect(jsonPath("$.fieldErrors[0].field").value("commands"))
				.andExpect(jsonPath("$.fieldErrors[0].message").value(responseMessage));
	}

	@Test
	public void historyShowAllMachines() throws Exception {
		List<MachineDto> returnedList = new ArrayList<>();
		returnedList.add(DefaultModel.getMachineDto());
		returnedList.add(DefaultModel.getMachineDto());
		returnedList.add(DefaultModel.getMachineDto());
		returnedList.add(DefaultModel.getMachineDto());
		returnedList.add(DefaultModel.getMachineDto());
		when(webApiServiceMock.historyShowAllMachines()).thenReturn(returnedList);

		MockHttpServletRequestBuilder httpServletRequest = get("/web/api/history/show/all/machines");

		mockMvc.perform(httpServletRequest).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$[4].id").value(12345)).andExpect(jsonPath("$[4].ip").value("192.168.0.200"));
	}

	@Test
	public void historyShowMachineLogsWithSuccess() throws Exception {
		int machineId = 10;
		MachineLogHistoryDto machineLogHistoryDto = DefaultModel.getMachineLogHistoryDto();
		when(webApiServiceMock.historyShowMachineLogs(machineId)).thenReturn(machineLogHistoryDto);

		MockHttpServletRequestBuilder httpServletRequest = get("/web/api/history/show/machine/logs/" + machineId);

		mockMvc.perform(httpServletRequest).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.netInfo.port").value(12345))
				.andExpect(jsonPath("$.netInfo.ip").value("192.168.0.200"));
	}

	@Test
	public void historyShowMachineLogsNotFoundMachine() throws Exception {
		int machineId = 10;
		when(webApiServiceMock.historyShowMachineLogs(machineId)).thenThrow(new NotFoundMachineException());

		MockHttpServletRequestBuilder httpServletRequest = get("/web/api/history/show/machine/logs/" + machineId);

		String responseMessage = "Not found machine";
		mockMvc.perform(httpServletRequest).andDo(print()).andExpect(status().is(HttpStatus.NOT_ACCEPTABLE.value()))
				.andExpect(jsonPath("$.message").value(responseMessage));
	}

	@Test
	public void historyShowMachineLogOutputWithSuccess() throws Exception {
		int logId = 10;
		LogHistoryDto logHistoryDto = DefaultModel.getLogHistoryDto();
		when(webApiServiceMock.historyShowMachineLogOutput(logId)).thenReturn(logHistoryDto);

		MockHttpServletRequestBuilder httpServletRequest = get(
				"/web/api//history/show/machine/log/" + logId + "/output");

		mockMvc.perform(httpServletRequest).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.commands").value("dir C://")).andExpect(jsonPath("$.outputs[0]").exists());
	}

	@Test
	public void historyShowMachineLogOutputWithNotFoundLogHistory() throws Exception {
		int logId = 11;
		when(webApiServiceMock.historyShowMachineLogOutput(logId)).thenThrow(new NotFoundLogHistoryException());

		MockHttpServletRequestBuilder httpServletRequest = get(
				"/web/api//history/show/machine/log/" + logId + "/output");

		String responseMessage = "Not found log history";
		mockMvc.perform(httpServletRequest).andDo(print()).andExpect(status().is(HttpStatus.NOT_ACCEPTABLE.value()))
				.andExpect(jsonPath("$.message").value(responseMessage));
	}
}
