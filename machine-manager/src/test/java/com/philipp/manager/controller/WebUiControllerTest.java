package com.philipp.manager.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import com.philipp.manager.util.RandomSpringJUnit4ClassRunner;

@RunWith(RandomSpringJUnit4ClassRunner.class)
@WebMvcTest(WebUiController.class)
public class WebUiControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void homeReturnSelectPage() throws Exception {
		this.mockMvc.perform(get("/web/home")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("<title>Machine Manager v1.0 - Home</title>")));
	}

	@Test
	public void logHistoryReturnSelectPage() throws Exception {
		this.mockMvc.perform(get("/web/loghistory")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("<title>Machine Manager v1.0 - Log History</title>")));
	}
}
