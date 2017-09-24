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
package pt.ornrocha.webutils.connectionutils.downloaders;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

import org.apache.commons.io.FilenameUtils;

import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.webutils.connectionutils.InterruptConnectionThread;
import pt.ornrocha.webutils.connectionutils.WebConnectionException;
import pt.ornrocha.webutils.connectionutils.gui.DownloadProgressPanel;

public class HTTPFileDownloader implements Runnable{

	
	
	private URL url;
	private HttpURLConnection conn;
	private final String USER_AGENT = "Mozilla/5.0";
	private Proxy proxy=null;
	private String filename;
	private String destdir;
	private long filesize;
	protected PropertyChangeSupport changelst =null;
	protected DownloadProgressPanel progresspanel=null;
	protected boolean running=false;
	protected boolean keepalive=true;
	protected boolean success=false;
	private long timeout=20000;
	private Exception outexception;
	
	
	public static String HTTPDOWNLOADEREXCEPTION="httpdownloadexception";
	
	public HTTPFileDownloader(String url, String destdir) throws MalformedURLException{
		this.url=new URL(url);
		this.filename=getFileName(url);
		this.destdir=destdir;
	}
	
	
	public HTTPFileDownloader(String url, String destdir,String hostname,int port) throws MalformedURLException{
		this.url=new URL(url);
		this.filename=getFileName(url);
		this.destdir=destdir;
		InetSocketAddress adrs = new InetSocketAddress(hostname, port);
        this.proxy=new Proxy(Proxy.Type.HTTP, adrs);
	}
	
	private String getFileName(String url){
		return url.substring(url.lastIndexOf("/") + 1,url.length());
	}
	
	
	public void setProgessPanel(DownloadProgressPanel panel){
		 this.progresspanel=panel;
		 progresspanel.setVisible(false);
		 this.changelst=progresspanel.getListenerSupport();
	 }
	
	
	public void setPropertyChangeSupportListener(PropertyChangeSupport sl){
		if(progresspanel==null)
		    this.changelst=sl;
	}
	
	public void setPropertyPropertyChangeListener(PropertyChangeListener l){
		if(this.changelst==null)
			this.changelst=new PropertyChangeSupport(this);
		
		this.changelst.addPropertyChangeListener(l);
	
	}
	
	public void cancelNow(){
		this.keepalive=false;

	}
	

	
	
	private InputStream tryURLConnection() throws WebConnectionException, IOException{

		InputStream strm=null;


		if(proxy!=null)
			conn=(HttpURLConnection)url.openConnection(proxy);
		else
			conn =(HttpURLConnection)url.openConnection(); 



		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestMethod("GET"); 
		conn.setConnectTimeout((int) timeout);
		conn.setReadTimeout((int) timeout);

		conn.connect(); 
		InterruptConnectionThread interr =new InterruptConnectionThread(conn,timeout);
		new Thread(interr).start();

		int code = conn.getResponseCode();

		boolean validconnect = (code==HttpURLConnection.HTTP_OK);
		if(validconnect){
			interr.cancelDisconnect(validconnect);
			filesize=conn.getContentLength();
			strm=conn.getInputStream();
		}

		else
			throw new WebConnectionException(code);

		if(!interr.getcanceldisconnectstate())
			throw new WebConnectionException(-2);

		return strm;
	}
	
	
	
	
	
	
	
	@Override
	public void run(){

		String saveto = getFilePath();
		this.running=true;

		try {
			if(progresspanel!=null)
				progresspanel.setVisible(true);
			if(changelst!=null)
				changelst.firePropertyChange("startprogress", false, true);

			FileOutputStream outputStream = new FileOutputStream(saveto);
			InputStream inputStream =tryURLConnection();

			if(inputStream!=null) {
				if(changelst!=null){
					changelst.firePropertyChange(DownloadProgressPanel.CURRENTEFILENAME, null, filename);
					changelst.firePropertyChange(DownloadProgressPanel.CURRENTEFILESIZE, null, filesize);
				}

				byte[] buffer = new byte[4096];
				int bytesRead = -1;
				long totalBytesRead = 0;
				int percentCompleted = 0;
				long fileSize = filesize;



				while ((bytesRead = inputStream.read(buffer)) != -1 && keepalive) {
					outputStream.write(buffer, 0, bytesRead);
					totalBytesRead += bytesRead;
					percentCompleted = (int) (totalBytesRead * 100 / fileSize);

					if(changelst!=null)
						changelst.firePropertyChange(DownloadProgressPanel.CURRENTEFILEPROGRESS, "old", percentCompleted);

				}

				outputStream.close();
				conn.disconnect();

			}


		} catch (Exception e) {
			
			File fileerror=new File(saveto);
			System.out.println(fileerror.getAbsolutePath());
			if(fileerror.exists())
				fileerror.delete();
			
			if(changelst!=null)
				changelst.firePropertyChange(HTTPDOWNLOADEREXCEPTION,null, e);
			LogMessageCenter.getLogger().toClass(getClass()).addCriticalErrorMessage(e);
			outexception=e;
			success=false;
		}
		if(changelst!=null)
			changelst.firePropertyChange("finishedprogress", false, true);
		if(progresspanel!=null)
			progresspanel.setVisible(false);

		if(new File(saveto).exists())
			success=true;
	}
	
	
	
	public String getFilePath(){
		String path=FilenameUtils.concat(destdir, filename);
		File f = new File(destdir);
		if(!f.exists()){
			f.mkdirs();
		}
		return path;
	}
	

	
	public boolean isSuccessfully(){
		return success;
	}
	
	public Exception connectionException() {
		return outexception;
	}
	
	

	
	/*public static void main(String[] args) {
		String url="http://www.ebi.ac.uk/arrayexpress/files/E-GEOD-17314/E-GEOD-17314.raw.1.zip";
		String filename="E-GEOD-17314.raw.1.zip";
		String dir="/home/orocha/TESTE_DOWNLOAD";
		
         PropertyChangeSupport changelst = new PropertyChangeSupport(new MTUFtpUtils());
		
		PropertyChangeListener propChangeListn = new PropertyChangeListener() {

			@Override

			public void propertyChange(PropertyChangeEvent event) {

			    String property = event.getPropertyName();

			    if (property.equals(DownloadProgressPanel.CURRENTEFILEPROGRESS)) {
                    System.out.println("Progress: "+(int)event.getNewValue());
			    }
			    else if (property.equals(DownloadProgressPanel.CURRENTEFILENAME)) {
                    System.out.println("Filename: "+event.getNewValue());
			    }
			    else if (property.equals(DownloadProgressPanel.CURRENTEFILESIZE)) {
                    System.out.println("File size: "+event.getNewValue());
			    }
			    else if(property.equals(HTTPFileDownloader.HTTPDOWNLOADEREXCEPTION)){
			    	System.out.println("################## current file name: "+event.getNewValue()+"  ####################");
			    }
			  }
	       };
		changelst.addPropertyChangeListener(propChangeListn);
		
		
		try {
			HTTPFileDownloader d = new HTTPFileDownloader(url,dir);
			d.setPropertyChangeSupportListener(changelst);
			d.run();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

}
