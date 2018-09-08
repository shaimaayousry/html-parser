package com.scout24.challenge.htmlparser.controller;

import static org.junit.Assert.*;

import org.junit.Test;

public class HTMLParserUtilTest {

	@Test
	public void testGetHTMLVersion_HTML5() {
		String publicid = "";
		String htmlVersion = HTMLParserUtil.getHtmlVersion(publicid);
		assertEquals(HTMLParserUtil.HTML_5_VERSION, htmlVersion);
	}

	@Test
	public void testGetHTMLVersion_HTML4() {
		String publicid = "-//W3C//DTD HTML 4.01//EN";
		String htmlVersion = HTMLParserUtil.getHtmlVersion(publicid);
		assertEquals("HTML 4.01", htmlVersion);
	}

	@Test
	public void testGetValidURL_InvalidProtocol() {
		String url = "google";
		assertNull(HTMLParserUtil.getValidURL(url));
	}

	@Test
	public void testGetValidURL_InvalidHost() {
		String url = "https://";
		assertNull(HTMLParserUtil.getValidURL(url));
	}

	@Test
	public void testGetValidURL_ValidURL() {
		String url = "https://google.com";
		assertNotNull(HTMLParserUtil.getValidURL(url));
	}
}
