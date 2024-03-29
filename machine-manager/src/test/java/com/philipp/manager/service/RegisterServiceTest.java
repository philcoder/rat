package com.philipp.manager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.philipp.manager.exception.NotFoundMachineException;
import com.philipp.manager.model.Machine;
import com.philipp.manager.model.dto.MachineDto;
import com.philipp.manager.util.DefaultModel;
import com.philipp.manager.util.RandomSpringJUnit4ClassRunner;

@RunWith(RandomSpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { RegisterService.class, ModelMapper.class })
public class RegisterServiceTest {

	@Autowired
	private RegisterService registerService;

	@MockBean
	private MachineRepositoryService machineServiceMock;

	@Test
	public void registerWithSuccess() throws Exception {
		when(machineServiceMock.findByHostnameAndIpAndPort(ArgumentMatchers.any(String.class),
				ArgumentMatchers.any(String.class), ArgumentMatchers.any(Integer.class)))
						.thenThrow(NotFoundMachineException.class);

		Machine saved = DefaultModel.convertToEntity(DefaultModel.getMachineDto());
		when(machineServiceMock.save(ArgumentMatchers.any(Machine.class))).thenReturn(saved);

		MachineDto machineDto = DefaultModel.getMachineDto();
		int id = registerService.register(machineDto);
		assertThat(id).isEqualTo(machineDto.getId());
	}

	@Test
	public void registerUpdateWithSuccess() throws Exception {
		Machine findMachine = DefaultModel.convertToEntity(DefaultModel.getMachineDto());
		findMachine.setId(123);// find id
		when(machineServiceMock.findByHostnameAndIpAndPort(ArgumentMatchers.any(String.class),
				ArgumentMatchers.any(String.class), ArgumentMatchers.any(Integer.class))).thenReturn(findMachine);

		when(machineServiceMock.save(ArgumentMatchers.any(Machine.class))).thenReturn(findMachine);

		MachineDto machineDto = DefaultModel.getMachineDto();
		int id = registerService.register(machineDto);
		assertThat(id).isEqualTo(findMachine.getId());
	}

	@Test
	public void heartbeatWithSuccess() throws Exception {
		Machine findMachine = DefaultModel.convertToEntity(DefaultModel.getMachineDto());

		when(machineServiceMock.findById(ArgumentMatchers.any(Integer.class))).thenReturn(findMachine);
		when(machineServiceMock.findByHostnameAndIpAndPort(ArgumentMatchers.any(String.class),
				ArgumentMatchers.any(String.class), ArgumentMatchers.any(Integer.class))).thenReturn(findMachine);

		MachineDto machineDto = DefaultModel.getMachineDto();
		registerService.heartbeat(findMachine.getId(), machineDto.getNetworkInfoDto());
		assertTrue(true);// non exception executed
	}

	@Test(expected = NotFoundMachineException.class)
	public void putHeartbeatNotFoundMachineById() throws Exception {
		when(machineServiceMock.findById(ArgumentMatchers.any(Integer.class)))
				.thenThrow(NotFoundMachineException.class);

		MachineDto machineDto = DefaultModel.getMachineDto();
		registerService.heartbeat(machineDto.getId(), machineDto.getNetworkInfoDto());
		fail();
	}

	@Test(expected = NotFoundMachineException.class)
	public void putHeartbeatNotFoundMachineNetworkAttributes() throws Exception {
		Machine findMachine = DefaultModel.convertToEntity(DefaultModel.getMachineDto());
		when(machineServiceMock.findById(ArgumentMatchers.any(Integer.class))).thenReturn(findMachine);
		when(machineServiceMock.findByHostnameAndIpAndPort(ArgumentMatchers.any(String.class),
				ArgumentMatchers.any(String.class), ArgumentMatchers.any(Integer.class)))
						.thenThrow(NotFoundMachineException.class);

		MachineDto machineDto = DefaultModel.getMachineDto();
		registerService.heartbeat(machineDto.getId(), machineDto.getNetworkInfoDto());
		fail();
	}
}
