package com.ares.common.web;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Properties;
import java.net.URL; 

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

import org.apache.log4j.Logger;

public class WebClient {

	private String URL;						// URL to use 
	private String URLappend; 				// Any information following the URL e.g. jsessionid
	
    private InetSocketAddress isa;			// 
    private Proxy proxy; 
    protected Properties defaultProps;
    private URLConnection connection;
    private HttpsURLConnection httpsConnection;
    private InputStream in; 
    private String response;				// Last response received
    private int responseCode;
    
    static final Logger logger = Logger.getLogger(WebClient.class);
    private String threadName = Thread.currentThread().getName(); 
    
      public void createProperties(String configFile) {
        
        // create and load default properties
        defaultProps = new Properties();
        FileInputStream in; 

        try {
            in = new FileInputStream(configFile);
            defaultProps.load(in);
            in.close();

        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace(); 
        } catch (IOException ioe) {
            ioe.printStackTrace(); 
        }
      }
    
    public void setProxy() {

    	if (!(this.defaultProps == null)) { 
    		try {

            	String proxyserver = defaultProps.getProperty("proxyserver"); 
            	String proxyport   = defaultProps.getProperty("proxyport");
            	Authenticator.setDefault(new ProxyAuthenticator(defaultProps.getProperty("proxydomain") + "\\" + defaultProps.getProperty("proxyuser"),
                                      defaultProps.getProperty("proxypassword")));
            	System.setProperty("http.proxyHost", proxyserver);
            	System.setProperty("http.proxyPort", proxyport);
            	System.setProperty("http.agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.63 Safari/535.7"); 
            	int proxyPort = Integer.parseInt(proxyport);

            	InetAddress iaProxy = InetAddress.getByName(proxyserver);
            	isa = new InetSocketAddress(iaProxy, proxyPort);
            	proxy = new Proxy(Proxy.Type.HTTP, isa);

        	} catch (NumberFormatException nfe) {
               	logger.error("Number Format for Proxy Port Incorrect");
            } catch (UnknownHostException uhe) {
        		logger.error("Unknown Host Exception");
        	}
    	}
    }

        
	public void setResponse(String response) {
		this.response = response;
	}
	
	/*
	 * 1 * Check if Response string is not null
	 * 2 * Grab position 
	 * 
	 */
	
	public int getResponseCode() {
		return responseCode;
	}

	public String getResponse() {
		return response;
	}

	/*
	 * Perform a GET operation
	 * This WebClient object needs to be initialised with 
	 * an InputStream. 
	 * The response from the GET operation is stored in this.response
	 */
	
    protected void get() throws IOException {
        
    	StringBuffer sb = new StringBuffer();
        try {
            byte[] buf = new byte[1024];
            int nread = 0;
            
            while ((nread = in.read(buf)) > 0) {
            	for (int i=0; i < nread; i++) {
            		sb.append((char) buf[i]);
            	}
            }
        } catch (IOException ioe) {
            in.close();
        }
        this.response = sb.toString();
        setResponseCode();
    }
    
    public void makeConnection(URL url) {
        try {
        	connection = url.openConnection();
        	connection.connect(); 
            in = connection.getInputStream();
        
        } catch (IOException e) {
            logger.error("Failed to fetch URL " + url.toString() + ": ");
            e.printStackTrace();
        }
    }
    
    public void makeHttpsConnection(URL url) {
        try {
        	httpsConnection = (HttpsURLConnection) url.openConnection();
        	httpsConnection.connect(); 
            in = httpsConnection.getInputStream();
        
        } catch (IOException e) {
            logger.error("Failed to fetch URL " + url.toString() + ": ");
            e.printStackTrace();
        }
    }
    
    public void makeOutputConnection() {
    	try {
    		makeOutputConnection(new URL(this.URL));
    	} catch (MalformedURLException mue){
    		logger.error("Malformed URL " + this.URL);
       	}
    	
    }
    
    public void makeOutputConnection(URL url) {
        try {
            connection = url.openConnection();
        	connection.setDoOutput(true);
                
        } catch (IOException e) {
            logger.error("Failed to fetch URL " + url.toString() + ": "
                                + e.getMessage());
        }
    }
  
    
    public void setSOAP(String length) {
		connection.setRequestProperty("Content-Length", length); 
		connection.setRequestProperty("Content-Type", "text/xml"); 
		connection.setRequestProperty("Connection", "Close"); 
		connection.setRequestProperty("SoapAction", ""); 
    }
    
    /*
     * Sends post request / captures response
     */
    public void sendPostRequest(String data) {
        
    	StringBuffer answer = new StringBuffer();
    	logger.trace(threadName + " " + connection.getURL());
    	logger.trace(threadName + " " + data);
        try {
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            //write parameters
            writer.write(data);
            writer.flush();
                             
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                answer.append(line);
            }
            writer.close();
            reader.close();
            
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } 
        
        this.response = answer.toString();
        setResponseCode(); 
        logger.trace(threadName + " " + answer.toString());
    }
    
    private void setResponseCode() {
    	// Cast to a HttpURLConnection
    	if ( connection instanceof HttpURLConnection)
    	{
    	   HttpURLConnection httpConnection = (HttpURLConnection) connection;

    	   try {
    		   setResponseCode(httpConnection.getResponseCode());
    	   } catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("IOException when getting Response Code");
		}

    	   // do something with code .....
    	}
    	else
    	{
    	   logger.error("error - not a http request!");
    	}

    }

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
}
