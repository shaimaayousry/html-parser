package com.scout24.challenge.htmlparser.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.scout24.challenge.htmlparser.model.PageStatusEnum;
import com.scout24.challenge.htmlparser.model.WebPageInfo;

@RunWith(MockitoJUnitRunner.class)
public class WebPageParserTest {

	private static final String WEB_PAGE_TEST_FILE = "src/test/resources/web-page-test.html";
	private Document document;

	@InjectMocks
	private WebPageParser htmlParser;

	@Mock
	private LinkParser linkURLParser;

	@Before
	public void setUp() throws Exception {
		reset(linkURLParser);
		document = Jsoup.parse(new File(WEB_PAGE_TEST_FILE), "UTF-8", "https://google.com");
	}

	@Test
	public void testParseWebPageDocument_ValidURL() {
		WebPageInfo webPageInfo = new WebPageInfo();
		String url = "https://google.com";
		htmlParser.parseWebPageDocument(webPageInfo, url);
		assertEquals(PageStatusEnum.VALID, webPageInfo.getPageStatus());
	}

	@Test
	public void testParseWebPageDocument_InvalidURLFormat() {
		WebPageInfo webPageInfo = new WebPageInfo();
		String url = "https://";
		htmlParser.parseWebPageDocument(webPageInfo, url);
		assertEquals(PageStatusEnum.INVALID_URL_FORMAT, webPageInfo.getPageStatus());
	}

	@Test
	public void testParseWebPageDocument_InvalidURLFormat2() {
		WebPageInfo webPageInfo = new WebPageInfo();
		String url = "google.com";
		htmlParser.parseWebPageDocument(webPageInfo, url);
		assertEquals(PageStatusEnum.INVALID_URL_FORMAT, webPageInfo.getPageStatus());
	}

	@Test
	public void testParseWebPageDocument_UnreachableURL() {
		WebPageInfo webPageInfo = new WebPageInfo();
		String url = "https://google.com.anything";
		htmlParser.parseWebPageDocument(webPageInfo, url);
		assertEquals(PageStatusEnum.UNREACHABLE_URL, webPageInfo.getPageStatus());
	}

	@Test
	public void testParseTitle() {
		WebPageInfo webPageInfo = new WebPageInfo();
		htmlParser.parseTitle(webPageInfo, document);
		assertEquals("Web Page Test", webPageInfo.getTitle());
	}

	@Test
	public void testParseHTMLVersion() {
		WebPageInfo webPageInfo = new WebPageInfo();
		htmlParser.parseHtmlVersion(webPageInfo, document);
		assertEquals(HTMLParserUtil.HTML_5_VERSION, webPageInfo.getHtmlVersion());
	}

	@Test
	public void testParseLoginAuthentication() {
		WebPageInfo webPageInfo = new WebPageInfo();
		htmlParser.parseAuthenticationInfo(webPageInfo, document);
		assertTrue(webPageInfo.isRequiresAuthentication());
	}

	@Test
	public void testParseHeadings() {
		WebPageInfo webPageInfo = new WebPageInfo();
		htmlParser.parseHeadings(webPageInfo, document);

		assertEquals(3, webPageInfo.getHeadings().size());
		assertEquals(2, webPageInfo.getHeadings().get("h1").size());
		assertEquals(1, webPageInfo.getHeadings().get("h3").size());
		assertEquals(1, webPageInfo.getHeadings().get("h6").size());
		assertEquals(Arrays.asList("Heading#1.1", "Heading#1.2"),
				webPageInfo.getHeadings().get("h1"));
		assertEquals(Arrays.asList("Heading#3"), webPageInfo.getHeadings().get("h3"));
		assertEquals(Arrays.asList("Heading#6"), webPageInfo.getHeadings().get("h6"));
	}

	@Test
	public void testParseLinks() {
		WebPageInfo webPageInfo = new WebPageInfo();
		htmlParser.parseLinks(webPageInfo, document);

		verify(linkURLParser, times(4)).parseLinkInfo(any(WebPageInfo.class), any(Element.class));
	}
}
