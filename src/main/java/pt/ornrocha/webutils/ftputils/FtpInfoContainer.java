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

import pt.ornrocha.webutils.ftputils.downloaders.IFTPDownloader;



public abstract class FtpInfoContainer implements IFTPDownloader, Runnable{

	
	protected MTUFtp ftpclient;
    protected String savetodir;
    protected String ftpurl;
    protected String webftpurl;
    protected String accession;
	protected String entrytype;
    protected boolean createdirs=true;
	
    
    public FtpInfoContainer(String accession){
    	this.accession=accession;
    }
    
    
    public FtpInfoContainer(String accession, String webftpurl){
    	this.accession=accession;
    	this.webftpurl=webftpurl;
    }
    

    public abstract String print();
    
    public abstract void setPropertyChangeSupport(PropertyChangeSupport evtsupport);
    
    public abstract void cancelConnectionNow();
    
    public abstract void cancelNotRunningConnection();

}
