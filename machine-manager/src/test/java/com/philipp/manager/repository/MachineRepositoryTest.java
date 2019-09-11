package com.philipp.manager.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.philipp.manager.model.Machine;
import com.philipp.manager.util.DefaultModel;
import com.philipp.manager.util.RandomSpringJUnit4ClassRunner;

@RunWith(RandomSpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
//@Transactional
@DataJpaTest
@TestPropertySource("/database-test.properties")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class MachineRepositoryTest extends AbstractRepository {

	@Autowired
	private MachineRepository repository;

	@Test
	public void findByHostnameAndIpAndPortWithSuccess() throws Exception {
		Machine machine = DefaultModel.getMachine();
		machine = persist(machine);

		Optional<Machine> findMachine = repository.findByHostnameAndIpAndPort(machine.getHostname(), machine.getIp(),
				machine.getPort());
		assertTrue(findMachine.isPresent());
		Machine find = findMachine.get();
		assertThat(find.getDotNetVersion()).isEqualTo(machine.getDotNetVersion());
		assertThat(find.getIp()).isEqualTo(machine.getIp());
	}

	@Test
	public void findByHostnameAndIpAndPortNotFound() throws Exception {
		Machine machine = DefaultModel.getMachine();
		machine = persist(machine);

		Optional<Machine> findMachine = repository.findByHostnameAndIpAndPort(machine.getHostname(), machine.getIp(),
				10000);
		assertTrue(findMachine.isEmpty());
	}

	@Test
	public void findOnlineMachinesFilledList() throws Exception {
		// already online true
		Machine machine = DefaultModel.getMachine();
		persist(machine);
		machine = DefaultModel.getMachine();
		machine.setOnline(false);
		persist(machine);
		machine = DefaultModel.getMachine();
		persist(machine);
		machine = DefaultModel.getMachine();
		persist(machine);

		List<Machine> onlineMachines = repository.findOnlineMachines();
		assertFalse(onlineMachines.isEmpty());
		assertTrue(onlineMachines.size() == 3);
	}

	@Test
	public void findOnlineMachinesEmptyListBecauseNonPersistence() throws Exception {
		List<Machine> onlineMachines = repository.findOnlineMachines();
		assertTrue(onlineMachines.isEmpty());
	}

	@Test
	public void findOnlineMachinesEmptyList() throws Exception {
		// already online true
		Machine machine = DefaultModel.getMachine();
		machine.setOnline(false);
		persist(machine);
		machine = DefaultModel.getMachine();
		machine.setOnline(false);
		persist(machine);
		machine = DefaultModel.getMachine();
		machine.setOnline(false);
		persist(machine);

		List<Machine> onlineMachines = repository.findOnlineMachines();
		assertTrue(onlineMachines.isEmpty());
	}

	@Test
	public void findOfflineMachinesFilledList() throws Exception {
		Machine machine = DefaultModel.getMachine();
		machine.setLastSeen(LocalDateTime.of(2019, 1, 1, 10, 10));
		persist(machine);
		machine = DefaultModel.getMachine();
		machine.setOnline(false);
		machine.setLastSeen(LocalDateTime.of(2019, 1, 1, 10, 10));
		persist(machine);
		machine = DefaultModel.getMachine();
		persist(machine);

		List<Machine> offlineMachines = repository.findOfflineMachines();

		Iterator<Machine> all = repository.findAll().iterator();
		while (all.hasNext()) {
			System.out.println(all.next().getLastSeen());
		}

		assertFalse(offlineMachines.isEmpty());
		// because one machine is online and more old time from now()
		assertTrue(offlineMachines.size() == 1);
	}

	@Test
	public void findOfflineMachinesEmptyListBecauseNonPersistence() throws Exception {
		List<Machine> offlineMachines = repository.findOfflineMachines();
		assertTrue(offlineMachines.isEmpty());
	}

	@Test
	public void findOfflineMachinesEmptyList() throws Exception {
		// already online true
		Machine machine = DefaultModel.getMachine();
		persist(machine);
		machine = DefaultModel.getMachine();
		persist(machine);
		machine = DefaultModel.getMachine();
		persist(machine);

		List<Machine> offlineMachines = repository.findOfflineMachines();
		assertTrue(offlineMachines.isEmpty());
	}

}
