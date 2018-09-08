package com.scout24.challenge.htmlparser.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.scout24.challenge.htmlparser.model.LinkInfo;
import com.scout24.challenge.htmlparser.model.LinkStatusEnum;
import com.scout24.challenge.htmlparser.model.LinkTypeEnum;
import com.scout24.challenge.htmlparser.model.WebPageInfo;

@Component
public class LinkParser {

	@Value("${enableValidateReachableURLs}")
	private boolean enableValidateReachableURLs;

	/**
	 * parses Link URL Info, where the link can be WEB URL link or email link or other.
	 * 
	 * @param webPageInfo
	 * @param htmlLink
	 *            HTML link info
	 * @return new instance of <tt>LinkInfo</tt>
	 */
	public LinkInfo parseLinkInfo(WebPageInfo webPageInfo, Element htmlLink) {
		LinkInfo linkInfo = null;
		String url = null;

		/**
		 * 1- parse URL from HTML Link:
		 */
		try {
			url = htmlLink.attr(HTMLParserUtil.ATTR_REF);
			url = url.trim();
		} catch (Exception ex) {
			// for any exception while parsing the link url i.e. if link is null
			return null;
		}

		/**
		 * 2- parse Link type:
		 */
		LinkTypeEnum linkType = parseLinkInfoType(url);

		/**
		 * 3- create LinkInfo and parse it based on the type:
		 */
		switch (linkType) {
		case WEB_URL:
			linkInfo = new LinkInfo(linkType, url, htmlLink.attr(HTMLParserUtil.ATTR_ABS_REF),
					htmlLink.text());
			validateLinkURLFormat(linkInfo);
			parseExternalLinkInfo(linkInfo, webPageInfo.getUrlHost());
			break;
		case EMAIL:
			linkInfo = new LinkInfo(linkType, url, url.split(HTMLParserUtil.LINK_TYPE_MAIL)[1],
					htmlLink.text());
			break;
		case OTHER:
			linkInfo = new LinkInfo(linkType, url, url, htmlLink.text());
			linkInfo.setStatus(LinkStatusEnum.INVALID_URL_FORMAT);
		}

		/**
		 * 4- Validate Web page Reachability:
		 * 
		 */
		if (enableValidateReachableURLs) {
			validateReachableLink(linkInfo);
		}
		return linkInfo;
	}

	/**
	 * Validates that the given link URL is a well formatted url and reachable.
	 * 
	 * @param linkInfo
	 * @return linkInfo updated with its status<tt>LinkStatusEnum</tt>
	 */
	public LinkInfo validateReachableLink(LinkInfo linkInfo) {
		/**
		 * skip validating reachable links for Invalid formatted links and links of types other than Web URL
		 */
		if (null == linkInfo || linkInfo.getStatus() != LinkStatusEnum.VALID
				|| linkInfo.getType() != LinkTypeEnum.WEB_URL) {
			return linkInfo;
		}

		try {
			Jsoup.connect(linkInfo.getAbsoluteURL()).userAgent("Mozilla").timeout(5000).execute();
			linkInfo.setStatus(LinkStatusEnum.VALID);
		} catch (HttpStatusException e) {
			linkInfo.setStatus(LinkStatusEnum.UNREACHABLE_URL);
			linkInfo.setValidationFailureReason(
					e.getMessage() + "[ status code: " + e.getStatusCode() + "]");
		} catch (IOException e) {
			linkInfo.setStatus(LinkStatusEnum.UNREACHABLE_URL);
			linkInfo.setValidationFailureReason(e.getMessage());
		}
		return linkInfo;
	}

	/**
	 * Parse the given URL type, type can be normal url or email link or Other (empty url used for Reload or javascript
	 * url refers to javascript function)
	 * 
	 * @param url
	 * @return <tt>LinkTypeEnum</tt> of the given url.
	 */
	protected LinkTypeEnum parseLinkInfoType(String url) {
		if (url.startsWith(HTMLParserUtil.LINK_TYPE_MAIL)) {
			return LinkTypeEnum.EMAIL;
		} else if (url.equals("") || url.startsWith(HTMLParserUtil.LINK_TYPE_JS_FUNC)) {
			return LinkTypeEnum.OTHER;
		} else {
			return LinkTypeEnum.WEB_URL;
		}
	}

	/**
	 * validates the format of the url of the given LinkInfo and update linkInfo status if URL is INVALID_FORMAT
	 * 
	 * @param linkInfo
	 */
	protected void validateLinkURLFormat(LinkInfo linkInfo) {
		if (linkInfo == null || null == HTMLParserUtil.getValidURL(linkInfo.getAbsoluteURL())) {
			linkInfo.setStatus(LinkStatusEnum.INVALID_URL_FORMAT);
		}
	}

	/**
	 * checks if the given linkInfo url is external URL.</br>
	 * URL is considered External if it is Absolute and doesn't contain the domain of the webpage (i.e. is not
	 * subdomain)</br>
	 * updates the given LinkInfo with isExternal value.
	 * 
	 * @param linkURL
	 * @param webPageHost
	 *            webpage host or domain
	 */
	protected void parseExternalLinkInfo(LinkInfo linkInfo, String webPageHost) {
		if (linkInfo == null || linkInfo.getStatus() != LinkStatusEnum.VALID) {
			return;
		}

		// update link type if it is External based on original URI not absolute value considering that if it is not
		// absolute then it is Internal
		try {
			URI uri = new URI(linkInfo.getUrl());
			linkInfo.setExternal(uri.isAbsolute() && !uri.getHost().contains(webPageHost));
		} catch (URISyntaxException e) {
			// do nothing as the URI has been validated in the previous step
		}
	}
}
