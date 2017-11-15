package com.ares.rampage.core;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ares.common.web.Step;

public class RampageXML {
	
	// RampageXML is a class that encapsulates an XML file used by Rampage Load Tester
	// Its primary function is to create a StepList using an input XML file
	// It can also perform validation or any other functions appropriate to XML files intended for use in Rampage
	
	private Document dom;
	private File file; 
	
	public RampageXML(File file) {
		this.file = file; 
	}

	private File getFile() {
		return file;
	}

	protected synchronized void parseXmlFile() {
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	
		try {
			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
	
			//parse using builder to get DOM representation of the XML file
			dom = db.parse(this.getFile());
						
		} catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch(SAXException se) {
			se.printStackTrace();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public synchronized List<Step> createStepList() {
		
		parseXmlFile();
				
		// Get the root element
		Element docEle = dom.getDocumentElement();
		
		// Create new List to hold each step 
		List<Step> stepList = new ArrayList<Step>(); 
		
		//Create a NodeList of <step> elements
		NodeList nl = docEle.getElementsByTagName("step");
		
		// Loop through the NodeList and add each node to a RampageXMLStep List
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				//get the step element
				Element el = (Element)nl.item(i);
				stepList.add(i, new RampageXMLStep(el));
			}
		}
		
		return stepList; 
	}
}
