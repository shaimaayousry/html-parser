package com.scout24.challenge.htmlparser.controller;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.scout24.challenge.htmlparser.model.LinkInfo;
import com.scout24.challenge.htmlparser.model.LinkStatusEnum;
import com.scout24.challenge.htmlparser.model.LinkTypeEnum;

public class LinkParserTest {

	private LinkParser linkParser;

	@Before
	public void setUp() throws Exception {
		linkParser = new LinkParser();
	}

	@Test
	public void testParseExternalLinkInfo_ExternalURL() {
		LinkInfo linkInfo = new LinkInfo(LinkTypeEnum.WEB_URL, "https://facebook.com",
				"https://facebook.com", "Facebook");
		linkParser.parseExternalLinkInfo(linkInfo, "google.com");
		assertTrue(linkInfo.isExternal());
	}

	@Test
	public void testParseExternalLinkInfo_InternalURL() {
		LinkInfo linkInfo = new LinkInfo(LinkTypeEnum.WEB_URL, "/mail", "https://google.com/mail",
				"Mail");
		linkParser.parseExternalLinkInfo(linkInfo, "google.com");
		assertFalse(linkInfo.isExternal());
	}

	@Test
	public void testValidateLinkURLFormat_ValidURL() {
		LinkInfo linkInfo = new LinkInfo(LinkTypeEnum.WEB_URL, "/mail", "https://google.com/mail",
				"Mail");
		linkParser.validateLinkURLFormat(linkInfo);
		assertTrue(linkInfo.getStatus() == LinkStatusEnum.VALID);
	}

	@Test
	public void testValidateLinkURLFormat_InvalidURL() {
		LinkInfo linkInfo = new LinkInfo(LinkTypeEnum.WEB_URL, "mail", "/mail", "Mail");
		linkParser.validateLinkURLFormat(linkInfo);
		assertTrue(linkInfo.getStatus() == LinkStatusEnum.INVALID_URL_FORMAT);
	}

	@Test
	public void testValidateReachable_ValidURL() {
		LinkInfo linkInfo = new LinkInfo(LinkTypeEnum.WEB_URL, "/mail", "https://google.com/mail",
				"Mail");
		linkParser.validateReachableLink(linkInfo);
		assertTrue(linkInfo.getStatus() == LinkStatusEnum.VALID);
	}

	@Test
	public void testValidateReachable_InvalidURL() {
		LinkInfo linkInfo = new LinkInfo(LinkTypeEnum.WEB_URL, "/", "https://google.com.anything",
				"Anything");
		linkParser.validateReachableLink(linkInfo);
		assertTrue(linkInfo.getStatus() == LinkStatusEnum.UNREACHABLE_URL);
	}

	@Test
	public void testParseLinkURLType() {
		String url = "/mail";
		LinkTypeEnum linkInfoType = linkParser.parseLinkInfoType(url);
		assertEquals(LinkTypeEnum.WEB_URL, linkInfoType);
	}

	@Test
	public void testParseLinkURLType_AbsoluteURL() {
		String url = "https://google.com";
		LinkTypeEnum linkInfoType = linkParser.parseLinkInfoType(url);
		assertEquals(LinkTypeEnum.WEB_URL, linkInfoType);
	}

	@Test
	public void testParseLinkURLType_Email() {
		String url = "mailto:test@test.com";
		LinkTypeEnum linkInfoType = linkParser.parseLinkInfoType(url);
		assertEquals(LinkTypeEnum.EMAIL, linkInfoType);
	}

	@Test
	public void testParseLinkURLType_EmptyURL() {
		String url = "";
		LinkTypeEnum linkInfoType = linkParser.parseLinkInfoType(url);
		assertEquals(LinkTypeEnum.OTHER, linkInfoType);
	}
}
