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
package pt.ornrocha.webutils.ftputils;

import java.beans.PropertyChangeSupport;

import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;

import pt.ornrocha.webutils.connectionutils.gui.DownloadProgressPanel;

public class FtpFileStreamListener implements CopyStreamListener{

	
	protected long filesize;
	protected PropertyChangeSupport changelst;
	
	
	public FtpFileStreamListener(long filesize){
	   this.filesize=filesize;	
	}
	
	public FtpFileStreamListener(long filesize, PropertyChangeSupport changelst){
		   this.filesize=filesize;
		   this.changelst=changelst;
		}
	
	@Override
	public void bytesTransferred(CopyStreamEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize) {
		
		int percent = (int)(totalBytesTransferred*100/filesize);
		if(changelst!=null)
			changelst.firePropertyChange(DownloadProgressPanel.CURRENTEFILEPROGRESS, 0, percent);
		
	}

}
