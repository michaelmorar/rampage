package com.ares.common.web;

public enum HttpRequestField {

	REFERER("Referer:"), ORIGIN("Origin:"), HOST("Host:");
	    
	private String value;

    private HttpRequestField(String value) {
            this.value = value;
    }

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
