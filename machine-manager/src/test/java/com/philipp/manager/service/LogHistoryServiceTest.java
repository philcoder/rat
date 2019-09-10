package com.philipp.manager.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.philipp.manager.model.LogHistory;
import com.philipp.manager.model.Machine;
import com.philipp.manager.repository.LogHistoryRepository;
import com.philipp.manager.util.DefaultModel;
import com.philipp.manager.util.RandomSpringJUnit4ClassRunner;

@RunWith(RandomSpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { LogHistoryService.class })
public class LogHistoryServiceTest {

	@Autowired
	private LogHistoryService service;

	@MockBean
	private LogHistoryRepository repositoryMock;

	@Test
	public void findAllByMachine() throws Exception {
		Machine machine = DefaultModel.getMachine();
		when(repositoryMock.findAllByMachine(machine))
				.thenReturn(new ArrayList<>(Arrays.asList(DefaultModel.getLogHistory(), DefaultModel.getLogHistory())));

		List<LogHistory> logHistories = service.findAllByMachine(machine);
		assertFalse(logHistories.isEmpty());
	}

	@Test
	public void findAllByMachineEmptyList() throws Exception {
		Machine machine = DefaultModel.getMachine();
		when(repositoryMock.findAllByMachine(machine)).thenReturn(new ArrayList<>());

		List<LogHistory> logHistories = service.findAllByMachine(machine);
		assertTrue(logHistories.isEmpty());
	}
}
