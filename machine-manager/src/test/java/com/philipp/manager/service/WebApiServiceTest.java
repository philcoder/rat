package com.philipp.manager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.philipp.manager.exception.ExecuteRemoteCommandException;
import com.philipp.manager.exception.NotFoundLogHistoryException;
import com.philipp.manager.exception.NotFoundMachineException;
import com.philipp.manager.model.LogHistory;
import com.philipp.manager.model.Machine;
import com.philipp.manager.model.dto.InputDto;
import com.philipp.manager.model.dto.LogHistoryDto;
import com.philipp.manager.model.dto.MachineDto;
import com.philipp.manager.model.dto.MachineLogHistoryDto;
import com.philipp.manager.util.DefaultModel;
import com.philipp.manager.util.RandomSpringJUnit4ClassRunner;

@RunWith(RandomSpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { WebApiService.class, ModelMapper.class })
public class WebApiServiceTest {

	@Autowired
	private WebApiService serviceMock;

	@MockBean
	private LogHistoryService logHistoryService;

	@MockBean
	private MachineService machineService;

	@MockBean
	private RestClientService restClientService;

	@Test
	public void showOnlineMachinesWithSuccess() throws Exception {
		List<Machine> machines = new ArrayList<>();
		machines.add(DefaultModel.getMachine());
		machines.add(DefaultModel.getMachine());
		when(machineService.findOnlineMachines()).thenReturn(machines);

		List<MachineDto> dtos = serviceMock.showOnlineMachines();
		assertFalse(dtos.isEmpty());
		assertTrue(dtos.size() == 2);
	}

	@Test
	public void showOnlineMachinesWithSuccessEmptyList() throws Exception {
		List<Machine> machines = new ArrayList<>();
		when(machineService.findOnlineMachines()).thenReturn(machines);

		List<MachineDto> dtos = serviceMock.showOnlineMachines();
		assertTrue(dtos.isEmpty());
	}

	@Test
	public void historyShowAllMachines() throws Exception {
		List<Machine> machines = new ArrayList<>();
		machines.add(DefaultModel.getMachine());
		machines.add(DefaultModel.getMachine());
		machines.add(DefaultModel.getMachine());
		machines.add(DefaultModel.getMachine());

		when(machineService.findAll()).thenReturn(machines);

		List<MachineDto> dtos = serviceMock.historyShowAllMachines();
		assertFalse(dtos.isEmpty());
		assertTrue(dtos.size() == 4);
	}

	@Test
	public void historyShowAllMachinesEmptyList() throws Exception {
		List<Machine> machines = new ArrayList<>();
		when(machineService.findAll()).thenReturn(machines);

		List<MachineDto> dtos = serviceMock.historyShowAllMachines();
		assertTrue(dtos.isEmpty());
	}

	@Test
	public void historyShowMachineLogOutputWithSuccess() throws Exception {
		int id = 50;
		LogHistory logHistory = DefaultModel.getLogHistory();
		when(logHistoryService.findById(id)).thenReturn(logHistory);

		LogHistoryDto dto = serviceMock.historyShowMachineLogOutput(id);
		assertThat(dto.getCommands()).isEqualTo(logHistory.getCommands());
		assertThat(dto.getOutputs()).isEqualTo(logHistory.getOutputs());
	}

	@Test(expected = NotFoundLogHistoryException.class)
	public void historyShowMachineLogOutputNotFoundId() throws Exception {
		int id = 60;
		when(logHistoryService.findById(id)).thenThrow(NotFoundLogHistoryException.class);

		serviceMock.historyShowMachineLogOutput(id);
		fail();
	}

	@Test
	public void historyShowMachineLogsWithSuccess() throws Exception {
		int id = 250;
		List<LogHistory> logs = new ArrayList<>();
		Machine machine = DefaultModel.getMachine();
		LogHistory log = DefaultModel.getLogHistory();
		log.setMachine(machine);
		logs.add(log);
		log = DefaultModel.getLogHistory();
		log.setMachine(machine);
		logs.add(DefaultModel.getLogHistory());
		when(logHistoryService.findAllByMachine(ArgumentMatchers.any(Machine.class))).thenReturn(logs);

		MachineLogHistoryDto dto = serviceMock.historyShowMachineLogs(id);
		assertFalse(dto.getLogs().isEmpty());
		assertTrue(dto.getLogs().size() == 2);
	}

	@Test
	public void historyShowMachineLogsNotFoundLogsForId() throws Exception {
		int id = 300;
		Machine machine = DefaultModel.getMachine();
		when(logHistoryService.findAllByMachine(ArgumentMatchers.any(Machine.class))).thenReturn(new ArrayList<>());
		when(machineService.findById(ArgumentMatchers.any(Integer.class))).thenReturn(machine);

		MachineLogHistoryDto dto = serviceMock.historyShowMachineLogs(id);
		assertTrue(dto.getLogs().isEmpty());
	}

	@Test(expected = NotFoundMachineException.class)
	public void historyShowMachineLogsNotFoundIdMachine() throws Exception {
		int id = 350;
		when(logHistoryService.findAllByMachine(ArgumentMatchers.any(Machine.class))).thenReturn(new ArrayList<>());
		when(machineService.findById(ArgumentMatchers.any(Integer.class))).thenThrow(NotFoundMachineException.class);

		serviceMock.historyShowMachineLogs(id);
		fail();
	}

	@Test
	public void executeCommandsWithSuccess() throws Exception {
		Machine machine = DefaultModel.getMachine();
		when(machineService.findById(ArgumentMatchers.any(Integer.class))).thenReturn(machine);
		LogHistory logHistoryFromRemoteCommand = DefaultModel.getLogHistory();
		when(restClientService.executeRemoteCommand(ArgumentMatchers.any(String.class),
				ArgumentMatchers.any(Machine.class))).thenReturn(logHistoryFromRemoteCommand);
		when(logHistoryService.save(ArgumentMatchers.any(LogHistory.class))).thenReturn(logHistoryFromRemoteCommand);

		InputDto dto = DefaultModel.getInputDto();
		dto.setCommands(logHistoryFromRemoteCommand.getCommands());

		List<MachineLogHistoryDto> machineLogHistoryDtos = serviceMock.executeCommands(dto);
		assertFalse(machineLogHistoryDtos.isEmpty());
		assertTrue(machineLogHistoryDtos.size() == 2);
		assertFalse(machineLogHistoryDtos.get(0).getLogs().isEmpty());
		assertTrue(machineLogHistoryDtos.get(1).getLogs().size() == 1);
	}

	public void executeCommandsWithMachineInvalidId() throws Exception {
		Machine machine = DefaultModel.getMachine();
		machine.setOnline(false);
		when(machineService.findById(ArgumentMatchers.any(Integer.class))).thenThrow(NotFoundMachineException.class);

		InputDto dto = DefaultModel.getInputDto();
		List<MachineLogHistoryDto> machineLogHistoryDtos = serviceMock.executeCommands(dto);
		assertFalse(machineLogHistoryDtos.isEmpty());
		assertTrue(machineLogHistoryDtos.size() == 2);
		assertFalse(machineLogHistoryDtos.get(0).getLogs().isEmpty());
		assertThat(machineLogHistoryDtos.get(0).getMessage()).contains("Not found machine for id");
		assertTrue(machineLogHistoryDtos.get(1).getLogs().size() == 1);
		assertTrue(machineLogHistoryDtos.get(1).getLogs().get(0).getOutputs().isEmpty());
	}

	@Test
	public void executeCommandsWithMachineOffline() throws Exception {
		Machine machine = DefaultModel.getMachine();
		machine.setOnline(false);
		when(machineService.findById(ArgumentMatchers.any(Integer.class))).thenReturn(machine);

		InputDto dto = DefaultModel.getInputDto();

		List<MachineLogHistoryDto> machineLogHistoryDtos = serviceMock.executeCommands(dto);
		assertFalse(machineLogHistoryDtos.isEmpty());
		assertTrue(machineLogHistoryDtos.size() == 2);
		assertFalse(machineLogHistoryDtos.get(0).getLogs().isEmpty());
		assertThat(machineLogHistoryDtos.get(0).getMessage()).isEqualTo("Machine " + machine + " is offline");
		assertTrue(machineLogHistoryDtos.get(1).getLogs().size() == 1);
		assertTrue(machineLogHistoryDtos.get(1).getLogs().get(0).getOutputs().isEmpty());
	}

	@Test
	public void executeCommandsWithExcuteRemoteException() throws Exception {
		Machine machine = DefaultModel.getMachine();
		when(machineService.findById(ArgumentMatchers.any(Integer.class))).thenReturn(machine);
		when(restClientService.executeRemoteCommand(ArgumentMatchers.any(String.class),
				ArgumentMatchers.any(Machine.class))).thenThrow(new ExecuteRemoteCommandException());

		List<MachineLogHistoryDto> machineLogHistoryDtos = serviceMock.executeCommands(DefaultModel.getInputDto());
		assertFalse(machineLogHistoryDtos.isEmpty());
		assertTrue(machineLogHistoryDtos.size() == 2);
		assertFalse(machineLogHistoryDtos.get(0).getLogs().isEmpty());
		assertThat(machineLogHistoryDtos.get(0).getMessage()).isEqualTo("Machine failed to connect");
		assertTrue(machineLogHistoryDtos.get(1).getLogs().size() == 1);
		assertTrue(machineLogHistoryDtos.get(1).getLogs().get(0).getOutputs().isEmpty());
	}
}
