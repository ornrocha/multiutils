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
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import pt.ornrocha.ioutils.writers.MTUWriterUtils;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.propertyutils.PropertiesUtilities;

public class MTUFtp {
	
public static final String FTPURL="ftp_url";
public static final String FTPUSERNAME="ftp_username";
public static final String FTPPASSWORD="ftp_password";
public static final String FTPPORT="ftp_port";

	
private final FTPClient client;
private String host;
private String username=null;
private String password=null;
private int port=21;    
private boolean usepassivemode=true;

    


	private MTUFtp(String host, int port){
		client=new FTPClient();
		this.host=host;
		this.port=port;
	}
	
	private MTUFtp(String host){
		this(host,0);
	}
	
	private MTUFtp(String host, String username, String password, int port){
		this(host,port);
		this.username=username;
		this.password=password;

		
	}
	
	private MTUFtp(String host, String username, String password){
		this(host,username,password,0);
	}

	
	
	
	
	private boolean startConnection(){
		
		if(host!=null){
		
			try {
				if(port>0 && port!=21)
					client.connect(host, port);
				else
					client.connect(host);
				
				if(username!=null && password!=null)
      			  client.login(username, password);
      		    else{
      			  username="anonymous";
      			  client.login(username, "");
      		    }
			} catch (Exception e) {
				LogMessageCenter.getLogger().addErrorMessage("Cannot login with username: "+username);
				return false;
			}
			LogMessageCenter.getLogger().addInfoMessage("Successful login with username: "+username);

			int reply;
			reply = client.getReplyCode();
			try {
				if (!FTPReply.isPositiveCompletion(reply)) {
					client.disconnect();
				}
			} catch (Exception e) {
				LogMessageCenter.getLogger().addErrorMessage("Connection failed: "+e.getMessage());
				return false;
			}
			LogMessageCenter.getLogger().addInfoMessage("Connection established to "+host);
			return true;
        
		}
		LogMessageCenter.getLogger().addErrorMessage("A hostname must be defined to execute the connection to ftp server");
        return false;
	}
	
	private boolean passiveModeConnection(){
		client.enterLocalPassiveMode();
		boolean connection=startConnection();
		if(connection)
			LogMessageCenter.getLogger().addInfoMessage("Successful connection in passive mode");
		else
			LogMessageCenter.getLogger().addInfoMessage("connection to FTP in passive mode failed...");
		
		return connection;
	}
	
	
	private boolean activeModeConnection(){
		client.enterLocalActiveMode();
		boolean connection=startConnection();
		if(connection)
			LogMessageCenter.getLogger().addInfoMessage("Successful connection in active mode");
		else
			LogMessageCenter.getLogger().addInfoMessage("connection to FTP in active mode failed...");
		
		return connection;
	}
	
	public boolean doConnection(){
		if(usepassivemode)
			return passiveModeConnection();
		else
			return activeModeConnection();
	}
	
	
	public boolean tryConnection(){
		boolean connected=false;
		
		connected=passiveModeConnection();
		if(!connected)
			connected=activeModeConnection();
		if(!connected){
			try {
				client.enterRemoteActiveMode(InetAddress.getByName(host), port);
			} catch (Exception e) {
				LogMessageCenter.getLogger().addInfoMessage("Error using remote active mode: "+e.getMessage());
				return false;
			}
			LogMessageCenter.getLogger().addInfoMessage("Using remote active mode");
			connected=passiveModeConnection();
			if(!connected)
				connected=activeModeConnection();
		}
		
		return connected;
	}
	
	
	public static  MTUFtp newinstance(Properties loginprops) throws IOException{
    	
    	String hostname=null;
    	String username=null;
    	String password=null;
    	int port=21;
    	
    	if(!loginprops.containsKey(FTPURL) || loginprops.getProperty(FTPURL).isEmpty())
    		throw new IOException("Please set host url in properties file");
    	else{
    		
    		hostname=loginprops.getProperty(FTPURL);
    		
    		if(loginprops.containsKey(FTPUSERNAME) && !loginprops.getProperty(FTPUSERNAME).isEmpty())
    			username=loginprops.getProperty(FTPUSERNAME);
    		if(loginprops.containsKey(FTPPASSWORD) && !loginprops.getProperty(FTPPASSWORD).isEmpty())
    			password=loginprops.getProperty(FTPPASSWORD);
    		if(loginprops.containsKey(FTPPORT) && !loginprops.getProperty(FTPPORT).isEmpty()){
    			try {
					port=Integer.parseInt(loginprops.getProperty(FTPPORT));
				} catch (Exception e) {
					port=21;
				}
    		}
    		
    			if(username!=null && password!=null && port==21)
    				return configureConnectionWithLogin(hostname, username, password);
    			else if(username!=null && password!=null && port!=21)
    				return configureConnectionWithLogin(hostname, username, password, port);
    			else if((username==null || password==null) && port!=21)
    				return configureConnection(hostname, port);
    			else
    				return configureConnection(hostname);
    	}
    }
	
	public static  MTUFtp newinstance(String loginpropsfilepath) throws IOException{
		return newinstance(PropertiesUtilities.loadFileProperties(loginpropsfilepath));
	}

	public static MTUFtp configureConnection(String host, int port){
		return new MTUFtp(host,port);
	}
	
	public static MTUFtp configureConnection(String host){
		return new MTUFtp(host);
	}
	
	
	public static MTUFtp configureConnectionWithLogin(String host, String username, String password){
		return new MTUFtp(host,username, password);
	}
	
	public static MTUFtp configureConnectionWithLogin(String host, String username, String password, int port){
		return new MTUFtp(host,username, password,port);
	}
	
	public MTUFtp doConnectionAnd(){
		doConnection();
		return this;
	}
	
	public MTUFtp tryConnectionAnd(){
		tryConnection();
		return this;
	}
	
	
	public MTUFtp withlocalPassiveMode(){
		this.usepassivemode=true;
		return this;
	}
	
	public MTUFtp withlocalActiveMode(){
		this.usepassivemode=false;
		return this;
	}
	
	public MTUFtp disableRemoteVerification(){
		client.setRemoteVerificationEnabled(false);
		return this;
	}
	
	public MTUFtp enableRemoteVerification(){
		client.setRemoteVerificationEnabled(true);
		return this;
	}
	
	public MTUFtp withRemoteActiveMode(){
		try {
			client.enterRemoteActiveMode(InetAddress.getByName(host), port);
		} catch (Exception e) {
			LogMessageCenter.getLogger().addErrorMessage("Error using remote active mode: "+e.getMessage());
		}
		LogMessageCenter.getLogger().addInfoMessage("Using remote active mode");
		return this;
	}
	
	
	public ArrayList<String> listAllFilesInDir(String dir, boolean getabsolutepath){
		
		ArrayList<String> filelist = new ArrayList<>();
		if(client.isConnected()){
			try {
				MTUFtpUtils.listfilesDirectory(client, filelist, dir, "", getabsolutepath);
				client.logout();
				client.disconnect();
			
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return filelist;
	}
	
	
	public Boolean downloadAllFilesOfDir(String sourcedir, String saveto, boolean createsubdirs, PropertyChangeSupport changelst) throws IOException{
            if(client.isConnected()){
            	client.setFileType(FTP.BINARY_FILE_TYPE);
            	MTUFtpUtils.downloadFilesOfDirectory(client, sourcedir, "",saveto,createsubdirs,changelst);
            	client.logout();
            	client.disconnect();	
            	return true;
            	}
            return false;
	}
	
	public void disconnect() throws IOException{
		if(client.isConnected())
			client.disconnect();
	}
	
	public boolean sendFileToFtpServer(String filepath) throws IOException{
		FileInputStream fis =null;
		
		if(client.isConnected()){
		try{
			fis = new FileInputStream(filepath);
			LogMessageCenter.getLogger().addInfoMessage("File to send: "+filepath);
			String filename=FilenameUtils.getName(filepath);
			client.setFileType(FTP.BINARY_FILE_TYPE);
			client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
			client.storeFile(filename, fis);
		} catch (IOException e) {
			LogMessageCenter.getLogger().addCriticalErrorMessage("Error sending file: "+e.getMessage());
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
				client.disconnect();
			} catch (IOException e) {
				LogMessageCenter.getLogger().addCriticalErrorMessage("Error: "+e.getMessage());
				return false;
			}
		}
		LogMessageCenter.getLogger().addInfoMessage("File was sent successfully");
		return true;
	    }
		return false;
	}
	
	/*public boolean uploadFolderFilesToFTPServer(String localdir){
		
		try {
			MTUFtpUtils.uploadDirectories(client, localdir, null);
		} catch (Exception e) {
			return false;
		}
		return true;
	}*/
	
	public boolean uploadFolderFilesToFTPServer(String localdir) throws IOException{
		if(client.isConnected()){
			String foldername=FilenameUtils.getBaseName(localdir);
			ArrayList<String> folderslist=MTUFtpUtils.listFolders(client);
			if(!folderslist.contains(foldername))
				client.makeDirectory(foldername);
			try {
				MTUFtpUtils.uploadDirectory(client,foldername,localdir,"");
			} catch (Exception e) {
				return false;
			}
			return true;
			}
		return false;
		
		
	}
	
	public static String makeFtpLoginPropertiesFile(){
	    StringBuilder str=new StringBuilder();
	    str.append(FTPURL+"=\n");
	    str.append(FTPPORT+"=21\n");
	    str.append(FTPUSERNAME+"=\n");
	    str.append(FTPPASSWORD+"=\n");
	    
	    return str.toString();
	}
	
	public static void writeConnectionTemplateToPropertiesFile(String filepath) throws IOException{
		MTUWriterUtils.writeStringWithFileChannel(makeFtpLoginPropertiesFile(), filepath, 0);
	}
	

	public static void main(String[] args) throws IOException {
		//ArrayList<String> l = ORIOFtp.connectToServer("ftp.ncbi.nlm.nih.gov").listAllFilesInDir("/geo/series/GSE41nnn/GSE41939/",false);
       //System.out.println(l);
		
	/*	PropertyChangeSupport changelst = new PropertyChangeSupport(new MTUFtpUtils());
		
		PropertyChangeListener propChangeListn = new PropertyChangeListener() {

			@Override

			public void propertyChange(PropertyChangeEvent event) {

			    String property = event.getPropertyName();

			    if (property.equals(DownloadProgressPanel.CURRENTEFILEPROGRESS)) {
                    System.out.println("Progress: "+(int)event.getNewValue());
			    }
			    else if(property.equals(DownloadProgressPanel.CURRENTEFILENAME)){
			    	System.out.println("################## current file name: "+event.getNewValue()+"  ####################");
			    }
			  }
	       };
		changelst.addPropertyChangeListener(propChangeListn);
		
		
		try {
			MTUFtp.connectToServer("ftp.ncbi.nlm.nih.gov").downloadAllFilesOfDir("/geo/series/GSE41nnn/GSE41939/", "/home/orocha/TESTE_DOWNLOAD", true, changelst);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
		
		
	/*	FTPClient ftpc=new FTPClient();
		
		ftpc.connect("orochaserver.ddns.info", 130);
		ftpc.login("ftpdata", "orocha1999");
		//client.enterLocalActiveMode();
		ftpc.setFileType(FTP.BINARY_FILE_TYPE);
		//ftpc.enterLocalPassiveMode();
		//ftpc.enterLocalActiveMode();
		//ftpc.enterLocalActiveMode();
		ftpc.setRemoteVerificationEnabled(false);
		//ftpc.enterRemoteActiveMode(InetAddress.getByName("orochaserver.ddns.info"), 130);
		FileInputStream fis =null;
		boolean ok=false;
		try{
			fis = new FileInputStream("/home/orocha/Documentos/47.Mi_Fit_2.20_pt_PT_jaymeferreyra_fermateus.apk");
			String filename=FilenameUtils.getName("/home/orocha/Documentos/47.Mi_Fit_2.20_pt_PT_jaymeferreyra_fermateus.apk");
			ok=ftpc.storeFile(filename, fis);
		} catch (IOException e) {
			e.printStackTrace();
		} */

		
		//boolean ok=MTUFtp.connectToServerWithLogin("orochaserver.ddns.info", "ftpdata", "orocha1999",130).disableRemoteVerification().sendFileToFtpServer("/home/orocha/Documentos/47.Mi_Fit_2.20_pt_PT_jaymeferreyra_fermateus.apk");
		//boolean ok=MTUFtp.newinstance("/home/orocha/ftplogin").disableRemoteVerification().sendFileToFtpServer("/home/orocha/Documentos/47.Mi_Fit_2.20_pt_PT_jaymeferreyra_fermateus.apk");
		//boolean ok=MTUFtp.newinstance("/home/orocha/ftplogin").disableRemoteVerification().sendFileToFtpServer("/home/orocha/Imagens/smplayer_screenshots/novo ficheiro.txt");
		//boolean ok=MTUFtp.newinstance("/home/orocha/ftplogin").disableRemoteVerification().uploadFolderFilesToFTPServer("/home/orocha/Imagens/smplayer_screenshots");
		boolean ok=MTUFtp.configureConnectionWithLogin("orochaserver.ddns.info", "ftpdata", "orocha1999",130).disableRemoteVerification().tryConnectionAnd().sendFileToFtpServer("/home/orocha/Documentos/47.Mi_Fit_2.20_pt_PT_jaymeferreyra_fermateus.apk");
		System.out.println("Condition: "+ok);
	}

}
