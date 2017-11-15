package com.ares.rampage.front;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.ares.rampage.core.RampageResponse;
import com.ares.rampage.core.RampageXML;
import com.ares.rampage.core.RampageThread;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;



public class TestSession1 {

	// Test Harness for Rampage Load Testing
	static final Logger logger = Logger.getLogger(TestSession1.class);
	public static void main(String[] args) {

		System.out.println(new Date(System.currentTimeMillis()) + " " + args.length);
		BasicConfigurator.configure();
		if  (args.length <= 0) {
			System.out.println("Path for test configuration XML files required");
		}

		try {
			Properties props = new Properties();
			props.load(new FileInputStream(args[3]));
			PropertyConfigurator.configure(props);	
			logger.info(args[3]);
		} catch (FileNotFoundException fnfe) {
			logger.warn("No Log4j files implicitly loaded - Will use default");
		} catch (IOException ioe) {
			logger.warn("IO Exception loading log4j file - Will use default");
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			logger.warn("No Log4J file implicitly provided - Will use default");
		}
		int reps = 0;
		int threads = 0; 

		try {
			threads = Integer.parseInt(args[1]);
			reps = Integer.parseInt(args[2]);
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			logger.warn("Defaulting to one thread per Test File and one repetition of the test.");
		}
		
		int numberOfFiles = 0; 
		boolean complete = false; 
        //Search directory for XML files
		do {
        	System.setProperty("http.agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.63 Safari/535.7");
        	//TO DO compensate for lack of "/" in base directory
        	String filename = new String(args[0] + "TEST" + Integer.toString(numberOfFiles) + ".xml");
        	String proxyName = new String(args[0] + "proxy.properties");
        	File XMLfile = new File(filename);
        	logger.info("Using " + filename);
        	// For every file found, create a step list and inject into TestThread
        	File ProxyFile = new File(proxyName);
    		
        	if (XMLfile.exists()) {
    			++numberOfFiles;
        		RampageXML rx = new RampageXML(XMLfile);
        		for (int x=0; x <=threads; ++x) {
	            	Runnable r; 
	            	if (ProxyFile.exists()) {
	    				r = new RampageThread(reps, rx.createStepList(), proxyName);
	    			} else {
	    			    r = new RampageThread(reps, rx.createStepList());	
	    			}
	    			Thread thread = new Thread(r);
	    			thread.start();
        		}
            } else {
            	complete = true; 
            }
        } while (!(complete));
    }
}
