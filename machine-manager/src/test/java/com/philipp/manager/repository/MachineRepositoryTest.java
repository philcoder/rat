package com.philipp.manager.repository;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.philipp.manager.util.RandomSpringJUnit4ClassRunner;

@RunWith(RandomSpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@Transactional
@DataJpaTest
@TestPropertySource("/database-test.properties")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class MachineRepositoryTest extends AbstractRepository {

	@Autowired
	private MachineRepository repository;

}
