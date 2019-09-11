package com.philipp.manager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import com.philipp.manager.exception.ExecuteRemoteCommandException;
import com.philipp.manager.model.LogHistory;
import com.philipp.manager.model.Machine;
import com.philipp.manager.model.dto.ExecuteCommandDto;
import com.philipp.manager.util.DefaultModel;
import com.philipp.manager.util.RandomSpringJUnit4ClassRunner;

@RunWith(RandomSpringJUnit4ClassRunner.class)
@RestClientTest(RestClientService.class)
public class RestClientServiceTest {

	@Autowired
	private RestClientService service;

	@Autowired
	private MockRestServiceServer server;

	@Test
	public void executeRemoteCommandWithSuccess() throws Exception {
		ExecuteCommandDto dto = DefaultModel.getExecuteCommandDto();
		Machine machine = DefaultModel.getMachine();
		String url = "http://" + machine.getIp() + ":" + machine.getPort() + "/watcher/v1/terminal";
		this.server.expect(requestTo(url)).andRespond(MockRestResponseCreators
				.withSuccess(DefaultModel.getExecuteCommandDtoToJsonString(), MediaType.APPLICATION_JSON));

		LogHistory logHistory = service.executeRemoteCommand(dto.getCommands(), machine);
		assertNotNull(logHistory.getMachine());
		assertThat(machine.getHostname()).isEqualTo(logHistory.getMachine().getHostname());

		assertFalse(logHistory.getOutputs().isEmpty());
		assertTrue(logHistory.getOutputs().size() == 2);
		assertThat(dto.getCommands()).isEqualTo(logHistory.getCommands());
	}

	@Test
	public void executeRemoteCommandWithEmptyList() throws Exception {
		ExecuteCommandDto dto = DefaultModel.getExecuteCommandDto();
		dto.setOutputs(new ArrayList<>());
		Machine machine = DefaultModel.getMachine();
		String url = "http://" + machine.getIp() + ":" + machine.getPort() + "/watcher/v1/terminal";
		this.server.expect(requestTo(url)).andRespond(MockRestResponseCreators
				.withSuccess(DefaultModel.getJsonStringFromObject(dto), MediaType.APPLICATION_JSON));

		try {
			service.executeRemoteCommand(dto.getCommands(), machine);
		} catch (ExecuteRemoteCommandException e) {
			assertThat(e.getMessage()).isEqualTo("Machine " + machine + " generate a empty output");
		}
	}

	@Test
	public void executeRemoteCommandWithBadRequest() throws Exception {
		ExecuteCommandDto dto = DefaultModel.getExecuteCommandDto();
		Machine machine = DefaultModel.getMachine();
		String url = "http://" + machine.getIp() + ":" + machine.getPort() + "/watcher/v1/terminal";
		this.server.expect(requestTo(url)).andRespond(MockRestResponseCreators.withBadRequest());
		try {
			service.executeRemoteCommand(dto.getCommands(), machine);
		} catch (ExecuteRemoteCommandException e) {
			assertThat(e.getMessage()).isEqualTo("Machine " + machine + " failed to connect");
		}
	}

}
