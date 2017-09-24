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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedHashSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import pt.ornrocha.webutils.connectionutils.gui.DownloadFutureProcesskiller;
import pt.ornrocha.webutils.connectionutils.gui.DownloadsProgressGUI;
import pt.ornrocha.webutils.connectionutils.gui.IDownloadsProgressInterface;

public class GenericHTTPDownloadProcessor <T extends HTTPFileDownloader> implements Runnable{

	
	protected ThreadPoolExecutor executor = null;
	protected LinkedHashSet<T> downloads;
	protected boolean running=false;
	protected String destfolder;
	protected IDownloadsProgressInterface progressviewer=null;
	protected int numberprocesses=0;
	protected PropertyChangeSupport changelst =null;
	
	public static String CLOSEHTTPPROCESSOR="closehttpprocessor";
	
	
	public GenericHTTPDownloadProcessor(int nproc, IDownloadsProgressInterface viewer){
		this.executor= (ThreadPoolExecutor) Executors.newFixedThreadPool(nproc);
		if(viewer!=null){
			this.progressviewer=viewer;
			progressviewer.setFutureProcesskiller(new DownloadFutureProcesskiller(executor));
			progressviewer.addPropertyChangeListener(getProcessesCancelListenerOperation());
		}
	}

	

	public GenericHTTPDownloadProcessor(LinkedHashSet<T> downloads,int nproc, IDownloadsProgressInterface viewer){
		this.downloads=downloads;
		this.executor= (ThreadPoolExecutor) Executors.newFixedThreadPool(nproc);
		if(viewer!=null){
			this.progressviewer=viewer;
			progressviewer.setFutureProcesskiller(new DownloadFutureProcesskiller(executor));
			progressviewer.addPropertyChangeListener(getProcessesCancelListenerOperation());
		}
	}
	
	
	public void addPropertyChangeListener(PropertyChangeListener propchangelist){
		this.changelst=new PropertyChangeSupport(this);
		this.changelst.addPropertyChangeListener(propchangelist);
	}
	
	
	public void addHTTPDownloadSet(LinkedHashSet<T> downloads){
		
		if(running)	
			addDownloadsToProcess(downloads);
		else{

			if(this.downloads==null)
			  this.downloads= new LinkedHashSet<>(downloads);
			else
			  this.downloads.addAll(downloads);	
		}
	}
	
	
	public void addHTTPDownload(T download){
		if(running){	
			executeDownload(download);
			numberprocesses++;
		}
		else{
			if(this.downloads==null)
			  this.downloads= new LinkedHashSet<>();
			 
            this.downloads.add(download);	
		}
	}
	
	
	private void addDownloadsToProcess(LinkedHashSet<T> downloads){
		
		for (HTTPFileDownloader infocont : downloads) {
			 executeDownload(infocont);
			 numberprocesses++;
		}
		
	}
	
	
	 private void executeDownload(HTTPFileDownloader down){
		 if(executor!=null && !executor.isTerminated()){
			 this.executor.execute(down);
		 }
	 }
	
	 
	 private  PropertyChangeListener getProcessesCancelListenerOperation(){
	
		 PropertyChangeListener propChangeListn = new PropertyChangeListener() {

			@Override

			public void propertyChange(PropertyChangeEvent event) {

			    String property = event.getPropertyName();

			    if (property.equals(DownloadsProgressGUI.CANCELPROCESSESNOW) ){
                 for (HTTPFileDownloader down : downloads) {
					 down.cancelNow();
				  }
			    }
			   /* else  if (property.equals(DownloadsProgressGUI.CANCELNEXTPROCESSES) ){
	                 for (HTTPFileDownloader down : downloads) {
						 down.cancelIfNotRunning();
					  }
				    }*/
			  }
	       };
	      
	       return propChangeListn;
	 }
	
	
	
	@Override
	public void run() {
        
    	this.running=true;
		for (HTTPFileDownloader element : downloads) {
			executor.submit(element);
			numberprocesses++;
		}
		
		while (!executor.isTerminated()) {
			try {
				Thread.sleep(200);
				int complete=(int) executor.getCompletedTaskCount();
				if(complete==numberprocesses && running)
					 executor.shutdownNow();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.running=false;
		progressviewer.dispose();
		changelst.firePropertyChange(CLOSEHTTPPROCESSOR, false, true);
		
	}
	

}
