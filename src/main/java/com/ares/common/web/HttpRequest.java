package com.ares.common.web;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HttpRequest {

	private HttpRequestType type; 
	private URL url;
	private char[] postData;
	private long sequence; 
	
	/*
	 *	Receives an String containing the entire HTTP Request document
	 *	Extracts the data required to create an HttpRequest 
	 */
	
	public HttpRequest(String string) {
		
		String[] httpRequest = string.split(System.getProperty("line.separator"));
		
		// Determine the URL
		doStandardHeadingFields(httpRequest);
						
		if (this.type == HttpRequestType.POST) {
			// Finally, handle the payload for POST requests - should always be the last line of the Request
			int payLoad = httpRequest.length - 1;
			setPostData(httpRequest[payLoad].toCharArray());
		}
	}
	
	public HttpRequestType getType() {
		return type;
	}
	
	public void setType(HttpRequestType type) {
		this.type = type;
	}
	
	public void setType(char[] line1) {
		if (line1[0] == 'P' && line1[1] == 'O' && line1[2] == 'S' && line1[3] == 'T') {
			setType(HttpRequestType.POST);
		} else {
			setType(HttpRequestType.GET);
		}
	}
	
	public URL getUrl() {
		return url;
	}
	public void setUrl(URL url) {
		this.url = url;
	}
	
	/*
	 * This method receives a single line of an HttpRequest header 
	 * It will "strip" the value portion and return a String
	 */
	
	public String stripValue(char[] line1) {
		boolean x = false;
		StringBuffer sb = new StringBuffer();
		for (char y: line1) {
			if (x && y!= ' ') {
				sb.append(y);
			}
			if (y == ' ' && x == true) {
				x = false;
			} else {
				if (y == ' ' && x == false) {
					x = true;
				}
			}
		}
		
		String value = sb.toString();
		return value; 		
	}
	
	public char[] getPostData() {
		return postData;
	}
	
	public void setPostData(char[] postData) {
		this.postData = postData;
	} 
	
	public ArrayList<String> getPostDataArrayList() {
		
		ArrayList<String> postDataArray = new ArrayList<String>();  
		StringBuffer sb = new StringBuffer(); 
		for (char c: this.postData) {
			if (c != '&') {
				sb.append(c);
			} else {
				if (sb.length() > 0) {
					postDataArray.add(sb.toString());
					sb = new StringBuffer(); 
				}
			}
		}
		
		// Add the last pair 
		postDataArray.add(sb.toString());
		return postDataArray;
	}
	
	public String toString(){
		String s = this.type.toString();
		
		if (this.url == null) {
			s+= " : URL Not set";
		} else {
			s+= " : " + this.url.toString();
		}
		
		if (postData == null) {
			s+= " : No Payload.";
		} else {
			s+= " : Post Data "; 
			for (int i=0; i < (postData.length -1); i++) {
				s+=postData[i]; 
			}
		}
		
		return s; 
		
	}
	
	private void doStandardHeadingFields(String[] httpRequest) {
		
		String url1 = "";
		for (String line : httpRequest) {
			
			// Process the various HTTP Request Header "ORIGIN" etc
			if (line.contains(HttpRequestField.HOST.getValue())) {
				url1 = stripValue(line.toCharArray());
			}
								
		}
		// First Line should always contain Request Type
		setType(httpRequest[0].toCharArray());
		String url2 = stripValue(httpRequest[0].toCharArray());
		
		String sUrl = "http://" + url1 + url2;
		try {
			setUrl(new URL(sUrl));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public long getSequence() {
		return sequence;
	}

	public void setSequence(long sequence) {
		this.sequence = sequence;
	}
}
