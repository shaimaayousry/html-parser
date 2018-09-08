package com.scout24.challenge.htmlparser.controller;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLParserUtil {

	public final static String TAG_HEADINGS = "h1, h2, h3, h4, h5, h6";
	public final static String ATTR_HTML_VERSION = "publicid";
	public final static String ATTR_ABS_REF = "abs:href";
	public final static String ATTR_REF = "href";
	public final static String TAG_REF = "a[href]";
	public final static String TAG_INPUT = "input";
	public final static String ATTR_TYPE = "type";
	public final static String LINK_TYPE_MAIL = "mailto:";
	public final static String INPUT_TYPE_PASSWORD = "password";
	public final static String LINK_TYPE_JS_FUNC = "javascript:";
	public final static String HTML_5_VERSION = "HTML 5";
	public final static String HTML_4_VERSION = "HTML 4";

	/**
	 * Parses DocumentType node which holds HTML DocType info to get HTML version used.</br>
	 * If no version found, then HTML version is HTML 5 as starting from this version, the version value is removed from
	 * DocType definition.
	 * 
	 * @param publicid
	 * @return String value represents HTML version
	 */
	public static String getHtmlVersion(String publicid) {
		if ("".equals(publicid)) {
			return HTMLParserUtil.HTML_5_VERSION;
		} else {
			Pattern pattern = Pattern.compile("HTML(\\s)*[0-9]+(\\.[0-9][0-9]?)?");
			Matcher matcher = pattern.matcher(publicid);
			if (matcher.find()) {
				return matcher.group(0);
			} else {
				return HTMLParserUtil.HTML_4_VERSION;
			}
		}
	}

	/**
	 * custom validation method to validate if the given URL format is valid considering protocol and domain/host.
	 * 
	 * @param urlString
	 * @return URI if the given url string is well formatted URI and URL, else it returns null.
	 */
	public static URI getValidURL(String urlString) {
		if (null == urlString) {
			return null;
		}
		URI uri = null;
		try {
			// instantiate URL instance validates the given String against protocol:
			new URL(urlString.trim());
			// initiate URI instance validates the given String against domain/host:
			uri = new URI(urlString.trim());
		} catch (MalformedURLException e) {
			return null;
		} catch (URISyntaxException e) {
			return null;
		}
		return uri;
	}
}
