package com.ares.rampage.core;


import java.util.List;
import org.apache.log4j.Logger;
import com.ares.common.web.Step;
import com.ares.common.web.WebClient;
import com.ares.common.web.HttpResponse;


public class RampageThread implements Runnable {

	private String proxyConfig;					// Configuration file - if any
	private int reps;   						// Number of times to repeat each StepList
	private List<Step> stepList; 				// A List of Steps 
	static final Logger logger = Logger.getLogger(RampageThread.class);
	
	public RampageThread(int reps, List<Step> stepListIn) {
		this.stepList = stepListIn;
		this.reps = reps; 
	}
	
	public RampageThread(int reps, List<Step> stepListIn, String proxyConfig) {
		this(reps,stepListIn); 
		this.setProxyConfig(proxyConfig);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 * Rampage Thread is an instance of RampageXML combined with a specified number of executions
	 * Create a new WebClient for each run 
	 * Set Proxy values if provided
	 * Go through the  
	 * 
	 */
	
	@Override
	public void run() {

		for (int i = 0; i < reps; i++) {
			 
			WebClient wc = new WebClient(); 
			if (!(proxyConfig == null)) {
			    wc.createProperties(proxyConfig);
			}
			
			for (Step stl : stepList) {
			    try {
					stl.setWebClient(wc);
			    	Thread.sleep(10);
			    	RampageXMLStep rxmls = (RampageXMLStep) stl; 
			    	rxmls.translateStep();
				    long responseTime = rxmls.executeWithResponse();
				    RampageResponse rr = new RampageResponse();
				    generateResponseObject(rr,rxmls,responseTime);
				    rr.logResponse();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NullPointerException npe) {
					logger.error("Null element passed to RampageXMLStep constructor");
				}
			}	
		}
	}
	
		
	public synchronized void setMySteps(List<Step> mySteps) {
		this.stepList = mySteps;
	}

	public void setProxyConfig(String proxyConfig) {
		this.proxyConfig = proxyConfig;
	}

	public String getProxyConfig() {
		return proxyConfig;
	}
	
	/*
	 * Create RampageResponse 
	 * To Do - a more elegant implementation of this function
	 * One hard-coded regex pattern available for the match reporting
	 * TO DO - replace this regex method... 
	 */
	private void generateResponseObject(RampageResponse rr, RampageXMLStep rxmls, long rt) {
		String pattern = "<title>[0-9a-zA-Z\\s-]{1,70}</title>";
		rr.setResponseTime(rt);
		rr.setSessionName("TestSession1");
		rr.setStep(rxmls.getStepID());
		rr.setThreadName(Thread.currentThread().getName());
		rr.setResponseCode(rxmls.getHttpResponseCode());
		rr.setResponseSelection(pattern, rxmls.getWebClient().getResponse());
		
	}
}
