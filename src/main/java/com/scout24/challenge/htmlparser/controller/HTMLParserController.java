package com.scout24.challenge.htmlparser.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scout24.challenge.htmlparser.model.WebPageInfo;

@RestController
public class HTMLParserController {

	@Autowired
	private HTMLParserService htmlParserService;

	@RequestMapping(value = "/api/parse", method = RequestMethod.GET)
	public WebPageInfo parseWebPage(@RequestParam String url) {
		WebPageInfo webPageInfo = htmlParserService.parseWebPage(url);
		return webPageInfo;
	}
}
