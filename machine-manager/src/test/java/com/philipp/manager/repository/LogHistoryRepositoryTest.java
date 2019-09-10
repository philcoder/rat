package com.philipp.manager.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.philipp.manager.model.LogHistory;
import com.philipp.manager.model.Machine;
import com.philipp.manager.util.DefaultModel;
import com.philipp.manager.util.RandomSpringJUnit4ClassRunner;

@RunWith(RandomSpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@Transactional
@DataJpaTest
@TestPropertySource("/database-test.properties")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class LogHistoryRepositoryTest extends AbstractRepository {

	@Autowired
	private LogHistoryRepository repository;

	@Test
	public void findAllByMachine() throws Exception {
		Machine machine = DefaultModel.getMachine();
		machine.getLogHistories().add(DefaultModel.getLogHistory(machine));
		machine.getLogHistories().add(DefaultModel.getLogHistory(machine));
		machine.getLogHistories().add(DefaultModel.getLogHistory(machine));
		machine = persist(machine);

		Machine findMachine = new Machine();
		findMachine.setId(machine.getId());
		List<LogHistory> logHistories = repository.findAllByMachine(findMachine);
		assertFalse(logHistories.isEmpty());
		assertTrue(logHistories.size() == 3);
		assertTrue("dir C://".equals(logHistories.get(0).getCommands()));
	}

	@Test
	public void findAllByMachineEmptyListPersist() throws Exception {
		Machine machine = persist(DefaultModel.getMachine());

		Machine findMachine = new Machine();
		findMachine.setId(machine.getId());
		List<LogHistory> logHistories = repository.findAllByMachine(findMachine);
		assertTrue(logHistories.isEmpty());
	}

	@Test
	public void findAllByMachineEmptyListWrongId() throws Exception {
		Machine machine = DefaultModel.getMachine();
		machine.getLogHistories().add(DefaultModel.getLogHistory(machine));
		machine.getLogHistories().add(DefaultModel.getLogHistory(machine));
		machine.getLogHistories().add(DefaultModel.getLogHistory(machine));
		machine = persist(machine);

		Machine findMachine = new Machine();
		findMachine.setId(1234);
		List<LogHistory> logHistories = repository.findAllByMachine(findMachine);
		assertTrue(logHistories.isEmpty());
	}
}
