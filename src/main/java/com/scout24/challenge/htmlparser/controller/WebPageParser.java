package com.scout24.challenge.htmlparser.controller;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.scout24.challenge.htmlparser.model.PageStatusEnum;
import com.scout24.challenge.htmlparser.model.WebPageInfo;

@Component
public class WebPageParser {

	@Autowired
	private LinkParser linkURLParser;

	/**
	 * parses webpage for the given URL.
	 * 
	 * @param url
	 * @return <tt>WebPageInfo</tt> holds the webpage parsed info.
	 */
	public WebPageInfo parseWebPage(String url) {
		WebPageInfo webPageInfo = new WebPageInfo();
		Document document = null;

		document = parseWebPageDocument(webPageInfo, url);
		if (webPageInfo.getPageStatus() != PageStatusEnum.VALID) {
			return webPageInfo;
		}

		parseTitle(webPageInfo, document);
		parseHtmlVersion(webPageInfo, document);
		parseHeadings(webPageInfo, document);
		parseAuthenticationInfo(webPageInfo, document);
		parseLinks(webPageInfo, document);
		return webPageInfo;
	}

	/**
	 * validates the given url (against format and connectivity) and generated HTML document of the webpage of the given
	 * url.
	 * 
	 * @param webPageInfo
	 * @param url
	 *            webpage URL string value
	 * @return HTML document <tt>Document</tt>
	 */
	protected Document parseWebPageDocument(WebPageInfo webPageInfo, String url) {
		Response response = null;
		Document document = null;

		// 1- validate it is valid URL format:
		URI pageURL = HTMLParserUtil.getValidURL(url);
		if (null == pageURL) {
			webPageInfo.setPageStatus(PageStatusEnum.INVALID_URL_FORMAT);
			return null;
		}

		// 2- validate it is reachable URL:
		try {
			response = Jsoup.connect(url.trim()).userAgent("Mozilla").timeout(30000).execute();
			webPageInfo.setPageStatus(PageStatusEnum.VALID);
		} catch (IOException e) {
			webPageInfo.setPageStatus(PageStatusEnum.UNREACHABLE_URL);
			return null;
		}

		// 3- generate document:
		try {
			document = response.parse();
			// remove "www." to be able to match with sub-domains
			webPageInfo.setUrlHost(pageURL.getHost().replaceFirst("www.", ""));
		} catch (IOException e) {
			webPageInfo.setPageStatus(PageStatusEnum.UNPARSABLE_HTML_CONTENT);
			return null;
		}
		return document;
	}

	/**
	 * parse the webpage title from HTML document and set it into webPageInfo given.
	 * 
	 * @param webPageInfo
	 * @param document
	 */
	protected void parseTitle(WebPageInfo webPageInfo, Document document) {
		webPageInfo.setTitle(document.title());
	}

	/**
	 * parse the webPage HTML version from HTML document and set it into webPageInfo given.
	 * 
	 * @param webPageInfo
	 * @param document
	 */
	protected void parseHtmlVersion(WebPageInfo webPageInfo, Document document) {
		String htmlVersion = document.childNodes().stream()
				.filter(node -> node instanceof DocumentType)
				.map(node -> HTMLParserUtil
						.getHtmlVersion(node.attr(HTMLParserUtil.ATTR_HTML_VERSION)))
				.collect(Collectors.joining());
		webPageInfo.setHtmlVersion(htmlVersion);
	}

	/**
	 * parse the webPage headings from the given HTML document and group them and set to the given webPageInfo.
	 * 
	 * @param webPageInfo
	 * @param document
	 */
	protected void parseHeadings(WebPageInfo webPageInfo, Document document) {
		Map<String, List<String>> headings = document.select(HTMLParserUtil.TAG_HEADINGS).stream()
				.collect(Collectors.groupingBy(Element::tagName,
						Collectors.mapping(Element::text, Collectors.toList())));
		webPageInfo.setHeadings(headings);
	}

	/**
	 * parses Authentication Info from the given HTML document and update the given webPageInfo with it. The webpage is
	 * considered requires authentication if it includes Password Input Type, there is also another type of verification
	 * is to check if the webpage contains login form whose title is "Login" or "Signup" but this would also require
	 * checking over the title in different languages.
	 * 
	 * @param webPageInfo
	 * @param document
	 */
	protected void parseAuthenticationInfo(WebPageInfo webPageInfo, Document document) {
		// TODO check over the form name:
		boolean requiresAuthentication = document.getElementsByTag(HTMLParserUtil.TAG_INPUT)
				.stream().anyMatch(ele -> ele.attr(HTMLParserUtil.ATTR_TYPE)
						.equals(HTMLParserUtil.INPUT_TYPE_PASSWORD));

		webPageInfo.setRequiresAuthentication(requiresAuthentication);
	}

	/**
	 * parses Links Info contained in the webpage. </br>
	 * It depends on parallel stream to enhance the performance of parsing links as one webpage might include many
	 * links.
	 * 
	 * @param webPageInfo
	 * @param document
	 */
	protected void parseLinks(WebPageInfo webPageInfo, Document document) {
		document.select(HTMLParserUtil.TAG_REF).parallelStream()
				.map(l -> linkURLParser.parseLinkInfo(webPageInfo, l))
				.forEach(l -> webPageInfo.addURLLink(l));
	}
}
