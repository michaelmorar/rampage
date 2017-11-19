package com.ares.common.web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;

public class Step {

	private String stepID;
	private String postData;
	private boolean isPost; // Only GET and POST supported. False=GET; True=POST
	private String URL;     // Needs to be injected by specific class
	private String URLsession; // Needs to be injected by specific class
	protected WebClient webClient;    // Needs to be injected for each round of testing 
	private int delay = 0; 
	private int sequence; 
	private int httpResponseCode;
	   
	static final Logger logger = Logger.getLogger(Step.class);
	
	protected Step() {
		this.setPost(false);
	}
	
	/*
	 * Execute the GET or POST Operation
	 * This method returns the Response time in a 'long' variable 
	 * 	  
	 */
	
	public long executeWithResponse() throws InterruptedException {
		setUpConnection(); 
		Thread.sleep(delay);
		long beginTime = System.currentTimeMillis();
		executeHTTP();
		long responseTime = (System.currentTimeMillis() - beginTime);
		logger.trace(Thread.currentThread().getName() + " Step ID " + this.stepID + " response time was " + responseTime);
		logger.trace(this.webClient.getResponse());
		logger.trace(this.webClient.getResponseCode());
		return responseTime;
	}
	
	/*
	 * Execute the GET or POST Operation
	 * This method calls executeHTTP and returns no output (void) 
	 * Uses getFullURL utility to join URL and URLSession
	 */
		
	public void execute() throws InterruptedException {
		Thread.sleep(delay);
		setUpConnection();
		executeHTTP();
	}
	
	/*
	 * Execute the HTTP GET or POST Command. 
	 */
	
	private void executeHTTP() {
		try {
			if (isPost){
			    this.webClient.sendPostRequest(this.postData); 
			    setHttpResponseCode(this.webClient.getResponseCode());
			} else {
				this.webClient.get();
     		}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}	 
	}
	
	
	protected void setPost(boolean isPost) {
		this.isPost = isPost; 
	}

	public boolean isPost() {
		return isPost;
	}
	
	private String getIsPost() {
		if (isPost) {
			return "POST";
		} else {
			return "GET";
		}
	}

	public String getPostData() {
		return postData;
	}

	public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
	}

	public WebClient getWebClient() {
		return webClient;
	}
		
	public String getURL() {
		return URL;
	}

	protected void setURL(String uRL) {
		URL = uRL;
	}
	
	public String getURLsession() {
		return URLsession;
	}

	protected void setURLsession(String uRLsession) {
		URLsession = uRLsession;
	}
	
	public void setPostData(String postData) {
		this.postData = postData;
	}
	
	/*
	 * Sets up connection
	 * 1 * Sets up Proxy (if any)
	 * 2 * Joins URL and URLSession strings 
	 * 3 * Sets the connection type (makeConnection or makeOutputConnection)
	 */
	
	private void setUpConnection() {
		this.webClient.setProxy();
		String s =  this.URL;
		
		if (!(URLsession == null)) {
			s += ";" + this.URLsession;
		}
		
		try {
			if (isPost){
				this.webClient.makeOutputConnection(new URL(s));
			} else {
				this.webClient.makeConnection(new URL(s));
			}
		} catch (MalformedURLException mfue) {
			logger.error("URL has not been formed correctly : " + s);
		}
	}
	
	public int getDelay() {
		return delay;
	}

	protected void setDelay(int delay) {
		this.delay = delay;
	}

	public int getSequence() {
		return sequence;
	}

	protected void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getStepID() {
		return stepID;
	}

	protected void setStepID(String stepID) {
		this.stepID = stepID;
	}
	
	public String toString() {
		return (getStepID() + " " + getIsPost());
	}

	public int getHttpResponseCode() {
		return httpResponseCode;
	}

	private void setHttpResponseCode(int httpResponseCode) {
		this.httpResponseCode = httpResponseCode;
	}

}

	
