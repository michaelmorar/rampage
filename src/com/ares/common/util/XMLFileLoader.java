package com.ares.common.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLFileLoader {
	
	private Document dom;
	private File file; 
	private String xmlTagName;
	
	public XMLFileLoader(File file) {
		this.file = file; 
	}
	
	public XMLFileLoader(Document dom) {
		this.dom = dom; 
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
	
	/*
	 * Currently this method will only return a NodeList
	 */
	
	public synchronized NodeList getElementList() {
		
		parseXmlFile();
				
		// Get the root element
		Element docEle = dom.getDocumentElement();
				
		//Create a NodeList of <step> elements
		NodeList nl = docEle.getElementsByTagName(getXmlTagName());
		return nl; 		
	}

	public String getXmlTagName() {
		return xmlTagName;
	}

	public void setXmlTagName(String xmlTagName) {
		this.xmlTagName = xmlTagName;
	}
}

	
