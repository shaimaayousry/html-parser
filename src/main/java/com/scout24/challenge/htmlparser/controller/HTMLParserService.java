package com.scout24.challenge.htmlparser.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scout24.challenge.htmlparser.model.WebPageInfo;

@Service
public class HTMLParserService {

	@Autowired
	private WebPageParser webPageParser;

	/**
	 * webPageInfoCache caches the results of info of web-pages inquired to avoid the parsing of the page info again if
	 * the it was requested before. It can be enhanced by applying expiry mechanism, where entries are removed from
	 * cache if reached maximum life time or based on least usage.
	 */
	private Map<String, WebPageInfo> webPageInfoCache = new ConcurrentHashMap<>();

	public WebPageInfo parseWebPage(String url) {
		if (null != url) {
			url = url.trim();
		}

		WebPageInfo cachedResult = findInCache(url);
		if (null != cachedResult) {
			return cachedResult;
		}

		WebPageInfo webPageInfo = webPageParser.parseWebPage(url);
		addToCache(url, webPageInfo);
		return webPageInfo;
	}

	private WebPageInfo findInCache(String url) {
		return webPageInfoCache.get(url);
	}

	private void addToCache(String url, WebPageInfo webPageInfo) {
		webPageInfoCache.put(url, webPageInfo);
	}

}
