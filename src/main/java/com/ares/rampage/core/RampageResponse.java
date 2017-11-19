package com.ares.rampage.core;


import org.apache.log4j.Logger;

import com.ares.common.web.HttpResponse;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RampageResponse extends HttpResponse {
    
	private String sessionName;
	private String threadName;
	private String step; 
    private int responseCode;
    private long responseTime;
    private String responseSelection; 
	static final Logger logger = Logger.getLogger(RampageResponse.class);

    private RampageResponse() {
    	
    }
    
    package-private void logResponse() {
    	logger.info(toString());
    }

	package-private String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public long getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(long responseTime) {
		this.responseTime = responseTime;
	}
    
    public String toString() {
    	return new String(getSessionName() + " " + getThreadName() + " " + getStep() + " "
    			+ " " + getResponseCode() + " " + getResponseTime() + " " + getResponseSelection());
    }
    
    public void setResponseSelection(String pattern, String responseText) {
    	Pattern p = Pattern.compile(pattern); 
		Matcher m1 = p.matcher(responseText);
		
		if (m1.find()) {
			// First find Session ID String
			MatchResult mr = m1.toMatchResult();
			responseSelection =  responseText.substring(mr.start(), mr.end());
		}
    }
    
    public String getResponseSelection() {
    	return this.responseSelection;
    }
}
