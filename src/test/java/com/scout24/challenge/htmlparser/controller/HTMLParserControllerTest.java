package com.scout24.challenge.htmlparser.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.scout24.challenge.htmlparser.app.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class HTMLParserControllerTest {
	@Autowired
	private WebApplicationContext ctx;

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
	}

	@Test
	public void testParseWebPage_WhenValidPageURL() throws Exception {
		mockMvc.perform(get("/api/parse").param("url", "https://google.com")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.pageStatus", Matchers.is("VALID")));

	}

	@Test
	public void testParseWebPage_WhenInvalidPageURL() throws Exception {
		mockMvc.perform(
				get("/api/parse").param("url", "google.com").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.pageStatus", Matchers.is("INVALID_URL_FORMAT")));
	}
}
