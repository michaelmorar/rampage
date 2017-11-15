package com.ares.rampage.core;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ares.common.web.HttpRequest;
import com.ares.rampage.core.RampageXMLStepKeyWords;

public class RampageXMLBuilder {

	private HttpRequest[] httpRequest; // Reference to an HttpRequest object
			
	public RampageXMLBuilder(HttpRequest[] httpRequest) {
		this.httpRequest = httpRequest; 
	}
	
	/*
	 * Getter/setters! 
	 */
	
	public HttpRequest[] getHtr() {
		return httpRequest;
	}

	public void setHtr(HttpRequest[] htr) {
		this.httpRequest = htr;
	}
	
	
	/*
	 * 	This is the most "loaded" method 
	 *  It will generate a DOM Document based on the HttpRequest[] array
	 *  Returns a Document 
	 */
	
	 public Document getDOMDocument() {
		 
		 Document doc = null;
		 
		 try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();	
		  
			// root element will be testsession
			doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("testsession");
			doc.appendChild(rootElement);
		
			System.out.println(httpRequest.length);
			for (HttpRequest htr: httpRequest) {
		
				//Step Elements
				Element step = doc.createElement("step");
				rootElement.appendChild(step);	
				
				//DO / TYPE Elements
				Element DO = doc.createElement(RampageXMLStepKeyWords.DO.toString());
				DO.appendChild(doc.createTextNode(htr.getType().toString()));
				step.appendChild(DO);
							
				// Handle URL and Session ID (if any)
				Element URL = doc.createElement(RampageXMLStepKeyWords.URL.toString());
				String[] url = htr.getUrl().toString().split("\\;",2);
				URL.appendChild(doc.createTextNode(url[0]));
				step.appendChild(URL);
				
				if (url.length > 1) {
					Element SURL = doc.createElement(RampageXMLStepKeyWords.SESSIONURL.toString()); 
					SURL.appendChild(doc.createTextNode(url[1]));
					step.appendChild(SURL);
				}
				
				// Now comes the FUN part: payload!
				// So much fun, it warrants a method if its own
				if (htr.getType() == com.ares.common.web.HttpRequestType.POST) {
					doPayLoad(step, htr.getPostDataArrayList(), doc);
				}
			}
			
		  } catch (ParserConfigurationException pce) {
			 pce.printStackTrace();
		  } finally {
			  return doc;
		  }
		    
	 }
	 
		/*
		 * 	This method will tokenise the Payload of the HTTP Request 
		 *  And generate the XML for each field
		 */
		
		 private void doPayLoad(Element step, ArrayList<String> postData, Document doku) {
			 for (String s: postData){
				 String x[] = s.split("\\=", 2);
			     Element e = doku.createElement(x[0]);
			     
			     if (x[1] != null) {
			         e.appendChild(doku.createTextNode(x[1]));
			     }
			     step.appendChild(e);
			 }
		 }
}
