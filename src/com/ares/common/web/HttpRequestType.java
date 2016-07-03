package com.ares.common.web;

public enum  HttpRequestType{

    GET("GET"), POST("POST"), UNDEFINED("UNDEFINED");
        private String value;

        private HttpRequestType(String value) {
                this.value = value;
        }
        
        protected String getHttpRequestType() {
        	return this.value; 
        }
};   


	

