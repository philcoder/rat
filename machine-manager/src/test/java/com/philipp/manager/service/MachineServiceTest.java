package com.philipp.manager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.philipp.manager.model.Machine;
import com.philipp.manager.repository.MachineRepository;
import com.philipp.manager.util.DefaultModel;
import com.philipp.manager.util.RandomSpringJUnit4ClassRunner;

@RunWith(RandomSpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { MachineService.class })
public class MachineServiceTest {

	@Autowired
	private MachineService service;

	@MockBean
	private MachineRepository repositoryMock;

	@Test
	public void findByHostnameAndIpAndPortWithSuccess() throws Exception {
		Machine machine = DefaultModel.getMachine();
		String hostname = machine.getHostname();
		String ip = machine.getIp();
		int port = machine.getPort();

		when(repositoryMock.findByHostnameAndIpAndPort(hostname, ip, port))
				.thenReturn(Optional.of(DefaultModel.getMachine()));

		Optional<Machine> findMachine = service.findByHostnameAndIpAndPort(hostname, ip, port);
		if (findMachine.isPresent()) {
			assertThat(findMachine.get().getHostname()).isEqualTo(hostname);
			assertThat(findMachine.get().getIp()).isEqualTo(ip);
			assertThat(findMachine.get().getPort()).isEqualTo(port);
		}
	}

	@Test
	public void findByHostnameAndIpAndPortNotFound() throws Exception {
		Machine machine = DefaultModel.getMachine();
		String hostname = machine.getHostname();
		String ip = machine.getIp();
		int port = machine.getPort();

		when(repositoryMock.findByHostnameAndIpAndPort(hostname, ip, port)).thenReturn(Optional.empty());

		Optional<Machine> findMachine = service.findByHostnameAndIpAndPort(hostname, ip, port);
		assertTrue(findMachine.isEmpty());
	}

	@Test
	public void findOfflineMachinesFilledList() throws Exception {
		when(repositoryMock.findOfflineMachines())
				.thenReturn(new ArrayList<>(Arrays.asList(DefaultModel.getMachine(), DefaultModel.getMachine())));

		List<Machine> offlineMachines = service.findOfflineMachines();
		assertFalse(offlineMachines.isEmpty());
	}

	@Test
	public void findOfflineMachinesEmptyList() throws Exception {
		when(repositoryMock.findOfflineMachines()).thenReturn(new ArrayList<>());

		List<Machine> offlineMachines = service.findOfflineMachines();
		assertTrue(offlineMachines.isEmpty());
	}

	@Test
	public void findOnlineMachinesFilledList() throws Exception {
		when(repositoryMock.findOnlineMachines()).thenReturn(new ArrayList<>(
				Arrays.asList(DefaultModel.getMachine(), DefaultModel.getMachine(), DefaultModel.getMachine())));

		List<Machine> offlineMachines = service.findOnlineMachines();
		assertFalse(offlineMachines.isEmpty());
	}

	@Test
	public void findOnlineMachinesEmptyList() throws Exception {
		when(repositoryMock.findOnlineMachines()).thenReturn(new ArrayList<>());

		List<Machine> offlineMachines = service.findOnlineMachines();
		assertTrue(offlineMachines.isEmpty());
	}
}
