/************************************************************************** 
 * Orlando Rocha (ornrocha@gmail.com)
 *
 * This is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This code is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU Public License for more details. 
 * 
 * You should have received a copy of the GNU Public License 
 * along with this code. If not, see http://www.gnu.org/licenses/ 
 *  
 */
package pt.ornrocha.webutils.connectionutils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;



public class WEBConnection {
	
	//private URLConnection connection=null;
	private String inputURL=null;
	private final String USER_AGENT = "Mozilla/5.0";
	private String contentType=null;
	private boolean isvalidConnection=false;
	private String accepteddoctype=null;
	private Proxy proxy=null;
	private long timeout=20000;
	
	
	
	public WEBConnection(String inputurl){
	        this.inputURL=checkURL(inputurl);     
	}
	
	
	public WEBConnection(String inputurl,String hostname,int port){
        this.inputURL=checkURL(inputurl); 
        InetSocketAddress adrs = new InetSocketAddress(hostname, port);
        this.proxy=new Proxy(Proxy.Type.HTTP, adrs);
     }
	
	public WEBConnection(String inputurl,Proxy proxy){
        this.inputURL=checkURL(inputurl); 
        this.proxy=proxy;
     }
	
	public WEBConnection(String inputurl, String validfiletype){
        this.inputURL=checkURL(inputurl);
        this.accepteddoctype=validfiletype;
    }
	
	
	public WEBConnection(String inputurl,String hostname,int port,String validfiletype){
        this(inputurl,hostname,port);
        this.accepteddoctype=validfiletype;
     }
	
	public WEBConnection(String inputurl,Proxy proxy, String validfiletype){
		this(inputurl,proxy);
        this.accepteddoctype=validfiletype;
    }
	
	
	public boolean isValidConnection(){
		return isvalidConnection;
	}
	
	public String getConnectionContentType(){
		return contentType;
	}
	
	public void setConnectiontimeout(long t){
		//MTULogUtils.addDebugMsgToClass(getClass(), "Connection Timeout: {}", t);
		this.timeout=t;
	}
	
	
	private InputStream tryURLConnection() throws WebConnectionException, IOException{
		
		InputStream strm=null;
		HttpURLConnection check=null;
		
		URL u = new URL(inputURL);
		
		if(proxy!=null)
		   check=(HttpURLConnection)u.openConnection(proxy);
		else
		   check =(HttpURLConnection)u.openConnection(); 
		
		
		
		check.setRequestProperty("User-Agent", USER_AGENT);
		check.setRequestMethod("GET"); 
		check.setConnectTimeout((int) timeout);
		check.setReadTimeout((int) timeout);
		
		check.connect(); 
		InterruptConnectionThread interr =new InterruptConnectionThread(check,timeout);
		new Thread(interr).start();
		
		int code = check.getResponseCode();
		
		boolean validconnect = connectionOKCode(code);
		if(validconnect){
			this.contentType = check.getContentType();
			
			if(accepteddoctype!=null){
				if(contentType.contains(accepteddoctype))
					this.isvalidConnection=true;
				else
					throw new WebConnectionException(-1);
			}
			else
              this.isvalidConnection=true;
		}
		
		if(isvalidConnection){
			interr.cancelDisconnect(isvalidConnection);
			strm=check.getInputStream();
		}
			
		else
			throw new WebConnectionException(code);
		
		if(!interr.getcanceldisconnectstate())
			throw new WebConnectionException(-2);
		
		return strm;
	}
	
	
	public InputStream doConnection(String allowedfiletype) throws IOException, WebConnectionException {
        this.accepteddoctype=allowedfiletype;       
		return tryURLConnection();
	}
	
	public InputStream doConnection() throws IOException, WebConnectionException {   
		return tryURLConnection();
	}
	
	public InputStream connectToURL(String url, String allowedfiletype) throws IOException, WebConnectionException {
        this.inputURL=url;     
		return doConnection(allowedfiletype);
	}
	
	public InputStream connectToURL(String url) throws IOException, WebConnectionException {
		this.inputURL=url;     
		return tryURLConnection();
	}
	
	
	public String getURL() throws MalformedURLException{
		return inputURL;
	}
	
  
	
	public static String checkURL(String url){
		return url.replaceAll("\\s+","%20");
	}
	
	
	private boolean connectionOKCode(int code){
		if(code==HttpURLConnection.HTTP_OK){
			return true;
		}
		else
			return false;
	}
	
	
	
}

