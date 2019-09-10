package com.philipp.manager;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.philipp.manager.controller.RegisterController;
import com.philipp.manager.controller.WebUiController;
import com.philipp.manager.service.WebApiService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

	@Autowired
	private RegisterController registerController;

	@Autowired
	private WebApiService webApiService;

	@Autowired
	private WebUiController webUiController;

	@Test
	public void contextLoads() throws Exception {
		assertThat(registerController).isNotNull();
		assertThat(webApiService).isNotNull();
		assertThat(webUiController).isNotNull();
	}

}
