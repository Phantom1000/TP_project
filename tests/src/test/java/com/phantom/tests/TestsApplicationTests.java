package com.phantom.tests;

import com.phantom.tests.controllers.ProfileController;
import com.phantom.tests.controllers.RegistrationController;
import com.phantom.tests.controllers.TestController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TestsApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private RegistrationController registrationController;

	@Autowired
	private TestController testController;

	@Autowired
	private ProfileController profileController;

	@Test
	void contextLoads() throws Exception {
		this.mockMvc.perform(get("/registration"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Регистрация")));
	}

	@Test
	public void accessDeniedTest() throws Exception {
		this.mockMvc.perform(get("/"))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/registration"));
	}

}
