package com.philipp.manager.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.philipp.manager.exception.NotFoundMachineException;
import com.philipp.manager.model.dto.MachineDto;
import com.philipp.manager.model.dto.NetworkInfoDto;
import com.philipp.manager.service.RegisterService;
import com.philipp.manager.util.DefaultModel;
import com.philipp.manager.util.RandomSpringJUnit4ClassRunner;

// https://reflectoring.io/spring-boot-web-controller-test/
@RunWith(RandomSpringJUnit4ClassRunner.class)
@WebMvcTest(RegisterController.class)
public class RegisterControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	@MockBean
	private RegisterService registerServiceMock;

	@Test
	public void postRegisterWithSuccess() throws Exception {
		// mock register service
		when(registerServiceMock.register(ArgumentMatchers.any(MachineDto.class))).thenReturn(100);

		MachineDto requestBody = DefaultModel.getMachineDto();
		MockHttpServletRequestBuilder httpServletRequest = post("/v1/client/win/register")
				.content(mapper.writeValueAsString(requestBody)).contentType(MediaType.APPLICATION_JSON);

		String responseMessage = "Machine " + requestBody.getNetworkInfoDto().getHostname() + " <"
				+ requestBody.getNetworkInfoDto().getIp() + ":" + requestBody.getNetworkInfoDto().getPort()
				+ "> registered with successful.";
		mockMvc.perform(httpServletRequest).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(100)).andExpect(jsonPath("$.message").value(responseMessage));
	}

	@Test
	public void postRegisterWithInternalException() throws Exception {
		when(registerServiceMock.register(ArgumentMatchers.any(MachineDto.class))).thenThrow(RuntimeException.class);

		MachineDto requestBody = DefaultModel.getMachineDto();
		MockHttpServletRequestBuilder httpServletRequest = post("/v1/client/win/register")
				.content(mapper.writeValueAsString(requestBody)).contentType(MediaType.APPLICATION_JSON);

		String responseMessage = "Invalid data for register machine.";
		mockMvc.perform(httpServletRequest).andDo(print()).andExpect(status().is(HttpStatus.NOT_ACCEPTABLE.value()))
				.andExpect(jsonPath("$.message").value(responseMessage));
	}

	@Test
	public void postRegisterWithRequiredValidFieldPattern() throws Exception {
		when(registerServiceMock.register(ArgumentMatchers.any(MachineDto.class))).thenReturn(100);

		MachineDto requestBody = DefaultModel.getMachineDto();
		requestBody.getNetworkInfoDto().setIp("localhost");

		MockHttpServletRequestBuilder httpServletRequest = post("/v1/client/win/register")
				.content(mapper.writeValueAsString(requestBody)).contentType(MediaType.APPLICATION_JSON);

		String responseMessage = "IP must be 0.0.0.0 at 255.255.255.255 range";
		mockMvc.perform(httpServletRequest).andDo(print()).andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
				.andExpect(jsonPath("$.fieldErrors[0].field").value("networkInfoDto.ip"))
				.andExpect(jsonPath("$.fieldErrors[0].message").value(responseMessage));

	}

	@Test
	public void postRegisterWithRequiredFieldEmpty() throws Exception {
		when(registerServiceMock.register(ArgumentMatchers.any(MachineDto.class))).thenReturn(100);

		MachineDto requestBody = DefaultModel.getMachineDto();
		requestBody.setDotNetVersion(null);

		MockHttpServletRequestBuilder httpServletRequest = post("/v1/client/win/register")
				.content(mapper.writeValueAsString(requestBody)).contentType(MediaType.APPLICATION_JSON);

		String responseMessage = "must not be empty";
		mockMvc.perform(httpServletRequest).andDo(print()).andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
				.andExpect(jsonPath("$.fieldErrors[0].field").value("dotNetVersion"))
				.andExpect(jsonPath("$.fieldErrors[0].message").value(responseMessage));
	}

	@Test
	public void putHeartbeatWithSuccess() throws Exception {
		doNothing().when(registerServiceMock).heartbeat(ArgumentMatchers.any(Integer.class),
				ArgumentMatchers.any(NetworkInfoDto.class));

		int id = 100;
		NetworkInfoDto requestBody = DefaultModel.getMachineDto().getNetworkInfoDto();
		registerServiceMock.heartbeat(id, requestBody);// simulated call mock

		MockHttpServletRequestBuilder httpServletRequest = put("/v1/client/win/heartbeat/" + id)
				.content(mapper.writeValueAsString(requestBody)).contentType(MediaType.APPLICATION_JSON);

		String responseMessage = "ok";
		mockMvc.perform(httpServletRequest).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value(responseMessage));
	}

	@Test
	public void putHeartbeatNotFoundMachine() throws Exception {
		doNothing().doThrow(new NotFoundMachineException()).when(registerServiceMock)
				.heartbeat(ArgumentMatchers.any(Integer.class), ArgumentMatchers.any(NetworkInfoDto.class));

		int id = 10;
		NetworkInfoDto requestBody = DefaultModel.getMachineDto().getNetworkInfoDto();
		registerServiceMock.heartbeat(id, requestBody);

		MockHttpServletRequestBuilder httpServletRequest = put("/v1/client/win/heartbeat/" + id)
				.content(mapper.writeValueAsString(requestBody)).contentType(MediaType.APPLICATION_JSON);

		String responseMessage = "Not found machine";
		mockMvc.perform(httpServletRequest).andDo(print()).andExpect(status().is(HttpStatus.NOT_ACCEPTABLE.value()))
				.andExpect(jsonPath("$.message").value(responseMessage));
	}

	@Test
	public void putHeartbeatWithRequiredFieldEmpty() throws Exception {
		doNothing().when(registerServiceMock).heartbeat(ArgumentMatchers.any(Integer.class),
				ArgumentMatchers.any(NetworkInfoDto.class));

		int id = 100;
		NetworkInfoDto requestBody = DefaultModel.getMachineDto().getNetworkInfoDto();
		registerServiceMock.heartbeat(id, requestBody);

		requestBody.setHostname(null);// set null on body resquest

		MockHttpServletRequestBuilder httpServletRequest = put("/v1/client/win/heartbeat/" + id)
				.content(mapper.writeValueAsString(requestBody)).contentType(MediaType.APPLICATION_JSON);

		String responseMessage = "must not be empty";
		mockMvc.perform(httpServletRequest).andDo(print()).andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
				.andExpect(jsonPath("$.fieldErrors[0].field").value("hostname"))
				.andExpect(jsonPath("$.fieldErrors[0].message").value(responseMessage));
	}
}
