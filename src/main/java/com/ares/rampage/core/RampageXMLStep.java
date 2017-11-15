package com.ares.rampage.core;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ares.common.web.Step;
import com.ares.rampage.core.RampageXMLStepKeyWords;

public class RampageXMLStep extends Step {

	public RampageXMLStep(Element el) {
		this.step = el; 
	}
	
	private Element step;

	/*
	 * Check for embedded Regex elements inside this element
	 */
	private NodeList checkForRegex(Element e) {
		NodeList nl = e.getElementsByTagName("regex");
		return nl; 
	}

	private String getRegexValue(String pattern) {
		// Use regex pattern to find a matching string in the last HTML response received
		
		try {
			Pattern p = Pattern.compile(pattern);
			String resp = super.webClient.getResponse(); 
			Matcher m1 = p.matcher(resp);
			
			if (m1.find()) {
				// First find Session ID String
				MatchResult mr = m1.toMatchResult();
				return  resp.substring(mr.start(), mr.end());
			}
			
		} catch (NullPointerException npe) {
			return "Null Pointer Exception - check you have performed at least one GET"; 
		}
		
		return "Session ID not found in response from server"; 
	}

	
	/*
	 * get the Text value of any tag - by name
	 */
	protected String getTextValue(Element ele, String tagName) {
		
		// Check for and embedded regex string.
		// If found, it means that this value must be retrieved from the previous response
		
		NodeList regexNodeList = checkForRegex(ele); 
		
		if ((ele.hasChildNodes()) && (regexNodeList.getLength() > 0)) {
			String textVal = regexNodeList.item(0).getTextContent();	        
			return getRegexValue(textVal);
		} else {
			String pd = ele.getTextContent(); 
			if (pd.equalsIgnoreCase("omitted")) {
				return ("");
			} else {
				return pd; 
			}
		}
	}

	/**
	 * This method will first acquire the COMMAND, URL and 
	 * 
	 * This method will then process all remaining nodes in the POST step to create the post data. 
	 * Append the key/value pair to this.postData e.g. task=login;
	 *
	 */
	protected void translateStep() {
		
		Node n = step.getFirstChild();
		StringBuffer sb = new StringBuffer();
		n = n.getNextSibling();
		
		boolean loop = true; 
		
		do {
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				Element el = (Element) n; 
				
				String eName = el.getNodeName();
				String eValue = getTextValue(el, el.getNodeName());

				// Identify Standard RampageXMLStep keywords
				if (eName.equalsIgnoreCase(RampageXMLStepKeyWords.DO.toString()) || eName.equalsIgnoreCase(RampageXMLStepKeyWords.URL.toString()) 
					|| eName.equalsIgnoreCase(RampageXMLStepKeyWords.SESSIONURL.toString())
				    || eName.equalsIgnoreCase("COMMAND") || eName.equalsIgnoreCase(RampageXMLStepKeyWords.SLEEPTIME.toString())
				    || eName.equalsIgnoreCase(RampageXMLStepKeyWords.SEQUENCE.toString())
				    || eName.equalsIgnoreCase(RampageXMLStepKeyWords.STEPID.toString())) {
				    setStandard(eName, eValue);
				} else {					
					sb.append(eName + "=" + eValue);
					sb.append("&");
				}
			}
			
			try {			
			    n = n.getNextSibling();
			    if (n == null ) {
			    	loop = false; 
			    }
			    	
			} catch (NullPointerException npe) {
				loop = false; 
			}
		} while (loop);
		
		super.setPostData(sb.toString());
	   
	}

	
	/**
	 * This method will process the "standard" elements of each RampageXMLStep. 
	 * 1 * COMMAND (DO) - the type of request - can only be GET or POST - Can also be "COMMAND"
	 * 2 * URL - URL 
	 * 3 * SESSIONURL - data to append to the URL
	 * 4 * SLEEPTIME
	 * 5 * SEQUENCE 
	 */
	
	private void setStandard(String eName, String eValue) {
	
		if (eName.equalsIgnoreCase("DO") || eName.equalsIgnoreCase("COMMAND")) { 
			if (eValue.equalsIgnoreCase("POST")) {
				super.setPost(true);
			} else {
				super.setPost(false);
			}
		}
		
		if (eName.equalsIgnoreCase("URL")) {
			super.setURL(eValue.toString());
		}
		
		if (eName.equalsIgnoreCase("SESSIONURL")) {
		    super.setURLsession(eValue.toString());	
		}
		
		if (eName.equalsIgnoreCase("SLEEPTIME")) {
			super.setDelay(Integer.parseInt(eValue.toString()));
		}
		
		if (eName.equalsIgnoreCase(RampageXMLStepKeyWords.SEQUENCE.toString())) {
			super.setSequence(Integer.parseInt(eValue.toString()));
		}
		
		if (eName.equalsIgnoreCase(RampageXMLStepKeyWords.STEPID.toString())) {
			super.setStepID(eValue.toString());
		}
	}
	
	/*
	 * 
	 */
	
	
}
