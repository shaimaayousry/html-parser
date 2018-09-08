package com.scout24.challenge.htmlparser.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.scout24.challenge.htmlparser.model.WebPageInfo;

@RunWith(MockitoJUnitRunner.class)
public class HTMLParserServiceTest {

	@InjectMocks
	private HTMLParserService htmlParserService;

	@Mock
	private WebPageParser htmlParser;

	@Test
	public void testIsDirectBusRouteExist() {

		WebPageInfo webPageInfo = new WebPageInfo();
		when(htmlParser.parseWebPage("https://google.com")).thenReturn(webPageInfo);

		assertEquals(webPageInfo, htmlParserService.parseWebPage("https://google.com")); // from repository
		assertEquals(webPageInfo, htmlParserService.parseWebPage("https://google.com"));// from cache

		verify(htmlParser, times(1)).parseWebPage("https://google.com");
	}
}
