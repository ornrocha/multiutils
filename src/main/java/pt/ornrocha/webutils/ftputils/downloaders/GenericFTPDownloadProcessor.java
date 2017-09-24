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
package pt.ornrocha.webutils.ftputils.downloaders;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedHashSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import pt.ornrocha.webutils.connectionutils.gui.DownloadFutureProcesskiller;
import pt.ornrocha.webutils.connectionutils.gui.DownloadsProgressGUI;
import pt.ornrocha.webutils.connectionutils.gui.IDownloadsProgressInterface;
import pt.ornrocha.webutils.ftputils.FtpInfoContainer;

public class GenericFTPDownloadProcessor <T extends FtpInfoContainer> implements Runnable {
	
	
	protected ThreadPoolExecutor executor = null;
	protected LinkedHashSet<T> downloads;
	protected boolean running=false;
	protected String destfolder;
	protected IDownloadsProgressInterface progressviewer=null;
	protected int numberprocesses=0;
	protected PropertyChangeSupport changelst =null;
	
	public static String CLOSEFTPPROCESSOR="closeftpprocessor";
	
	
	public GenericFTPDownloadProcessor(int nproc, IDownloadsProgressInterface viewer){
		this.executor= (ThreadPoolExecutor) Executors.newFixedThreadPool(nproc);
		if(viewer!=null){
			this.progressviewer=viewer;
			progressviewer.setFutureProcesskiller(new DownloadFutureProcesskiller(executor));
			progressviewer.addPropertyChangeListener(getProcessesCancelListenerOperation());
		}
	}
	
	
	public GenericFTPDownloadProcessor(String destfolder,int nproc, IDownloadsProgressInterface viewer){
		this.executor= (ThreadPoolExecutor) Executors.newFixedThreadPool(nproc);
		this.destfolder=destfolder;
		if(viewer!=null){
			this.progressviewer=viewer;
			progressviewer.setFutureProcesskiller(new DownloadFutureProcesskiller(executor));
			progressviewer.addPropertyChangeListener(getProcessesCancelListenerOperation());
		}
	}
	
	public GenericFTPDownloadProcessor(LinkedHashSet<T>data, String destfolder, int nproc, IDownloadsProgressInterface viewer){
		this.downloads=data;
		this.destfolder=destfolder;
		this.executor=(ThreadPoolExecutor) Executors.newFixedThreadPool(nproc);

		if(viewer!=null){
			this.progressviewer=viewer;
			progressviewer.setFutureProcesskiller(new DownloadFutureProcesskiller(executor));
			progressviewer.addPropertyChangeListener(getProcessesCancelListenerOperation());
		}
	}
	
	
	public void setNumberOfThreadPools(int n){
		  this.executor= (ThreadPoolExecutor) Executors.newFixedThreadPool(n);
		  if(progressviewer!=null)
			  progressviewer.setFutureProcesskiller(new DownloadFutureProcesskiller(executor));
	}
	
	
	public void addPropertyChangeListener(PropertyChangeListener propchangelist){
		this.changelst=new PropertyChangeSupport(this);
		this.changelst.addPropertyChangeListener(propchangelist);
	}
	
	
	public void addFtpDownloadSet(LinkedHashSet<T> downloads, String folder){
		
		if(running)	
			addDownloadsToProcess(downloads,folder);
		else{
			this.destfolder=folder;
			
			if(this.downloads==null)
			  this.downloads= new LinkedHashSet<>(downloads);
			else
			  this.downloads.addAll(downloads);	
		}
	}
	
	
	public void addFtpDownload(T download, String folder){

		if(running){
			download.configureDownloadToDirByAccessionName(folder, true);
			executeDownload(download);
			numberprocesses++;
		}
		else{
			this.destfolder=folder;
			if(this.downloads==null)
			  this.downloads= new LinkedHashSet<>();
			 
            this.downloads.add(download);	
		}
	}
	
	
	private void addDownloadsToProcess(LinkedHashSet<T> downloads, String folder){
		
		for (FtpInfoContainer infocont : downloads) {
			 infocont.configureDownloadToDirByAccessionName(folder, true);
			 executeDownload(infocont);
			 numberprocesses++;
		}
		
	}
	
	
	 private void executeDownload(FtpInfoContainer down){
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
                 for (FtpInfoContainer down : downloads) {
					 down.cancelConnectionNow();
				  }
			    }
			    else  if (property.equals(DownloadsProgressGUI.CANCELNEXTPROCESSES) ){
	               for (FtpInfoContainer down : downloads) {
						 down.cancelNotRunningConnection();
					  }
	                 running=false;
				   }
			  }
	       };
	      
	       return propChangeListn;
	 }

    @Override
	public void run() {
        
    	this.running=true;
		for (FtpInfoContainer element : downloads) {
			element.configureDownloadToDirByAccessionName(destfolder, true);
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
		changelst.firePropertyChange(CLOSEFTPPROCESSOR, false, true);
		
	}
	
	


	

}
