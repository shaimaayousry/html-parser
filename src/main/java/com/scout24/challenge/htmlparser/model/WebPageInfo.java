package com.scout24.challenge.htmlparser.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class WebPageInfo {

	private String url;
	private String urlHost;
	private String title;
	private String htmlVersion;
	private PageStatusEnum pageStatus;
	private boolean requiresAuthentication;
	private Map<String, List<String>> headings = new TreeMap<>();
	private List<LinkInfo> internalLinks = new ArrayList<>();
	private List<LinkInfo> externalLinks = new ArrayList<>();
	private List<LinkInfo> emailLinks = new ArrayList<>();
	private List<LinkInfo> otherLinks = new ArrayList<>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHtmlVersion() {
		return htmlVersion;
	}

	public void setHtmlVersion(String htmlVersion) {
		this.htmlVersion = htmlVersion;
	}

	public Map<String, List<String>> getHeadings() {
		return headings;
	}

	public void setHeadings(Map<String, List<String>> headings) {
		this.headings = headings;
	}

	public List<LinkInfo> getInternalLinks() {
		return internalLinks;
	}

	public List<LinkInfo> getExternalLinks() {
		return externalLinks;
	}

	public boolean isRequiresAuthentication() {
		return requiresAuthentication;
	}

	public void setRequiresAuthentication(boolean requiresAuthentication) {
		this.requiresAuthentication = requiresAuthentication;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public PageStatusEnum getPageStatus() {
		return pageStatus;
	}

	public void setPageStatus(PageStatusEnum pageStatus) {
		this.pageStatus = pageStatus;
	}

	public List<LinkInfo> getEmailLinks() {
		return emailLinks;
	}

	public String getUrlHost() {
		return urlHost;
	}

	public void setUrlHost(String urlHost) {
		this.urlHost = urlHost;
	}

	public List<LinkInfo> getOtherLinks() {
		return otherLinks;
	}

	public void addURLLink(LinkInfo linkInfo) {
		if (null == linkInfo) {
			return;
		}
		switch (linkInfo.getType()) {
		case WEB_URL:
			if (linkInfo.isExternal()) {
				externalLinks.add(linkInfo);
			} else {
				internalLinks.add(linkInfo);
			}
			break;
		case EMAIL:
			emailLinks.add(linkInfo);
			break;
		case OTHER:
			otherLinks.add(linkInfo);
			break;
		}
	}

}
