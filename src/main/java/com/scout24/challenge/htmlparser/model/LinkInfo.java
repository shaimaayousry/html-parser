package com.scout24.challenge.htmlparser.model;

public class LinkInfo {
	private String url;
	private String absoluteURL;
	private String title;
	private LinkStatusEnum status;
	private LinkTypeEnum type;
	private boolean isExternal;
	private String validationFailureReason;

	public LinkInfo(LinkTypeEnum type, String URL, String absoluteURL, String title) {
		setType(type);
		setUrl(URL);
		setTitle(title);
		setAbsoluteURL((null == absoluteURL ? URL : absoluteURL));
		// initialize the status with valid
		setStatus(LinkStatusEnum.VALID);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAbsoluteURL() {
		return absoluteURL;
	}

	public void setAbsoluteURL(String absoluteURL) {
		this.absoluteURL = absoluteURL;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LinkStatusEnum getStatus() {
		return status;
	}

	public void setStatus(LinkStatusEnum status) {
		this.status = status;
	}

	public LinkTypeEnum getType() {
		return type;
	}

	public void setType(LinkTypeEnum type) {
		this.type = type;
	}

	public boolean isExternal() {
		return isExternal;
	}

	public void setExternal(boolean isExternal) {
		this.isExternal = isExternal;
	}

	public String getValidationFailureReason() {
		return validationFailureReason;
	}

	public void setValidationFailureReason(String validationFailureReason) {
		this.validationFailureReason = validationFailureReason;
	}

}
