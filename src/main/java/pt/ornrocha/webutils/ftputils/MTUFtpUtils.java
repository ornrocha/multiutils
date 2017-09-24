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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.webutils.connectionutils.gui.DownloadProgressPanel;

public class MTUFtpUtils {
	
	
	//public static String CURRENTEFILEPROGRESS="currentfileprogress";
	//public static String CURRENTEFILENAME="currentfilename";
	//public static String CURRENTEFILESIZE="currentfilesize";
	//public static String HIDEPANELPROGESS="hideprogresspanel";
	
	
	

	public static void listfilesDirectory(FTPClient ftpClient, ArrayList<String> getter, String parentDir,String currentDir, boolean getabsolutepath) throws IOException {
	    
		String listdir = parentDir;
	    
	    if (!currentDir.equals("")) {
	    	listdir += "/" + currentDir;
	    }
	    
	    
	    
	    FTPFile[] subdirFiles = ftpClient.listFiles(listdir);
	    if (subdirFiles != null && subdirFiles.length > 0) {
	    	
	        for (FTPFile file : subdirFiles) {
	            String filename = file.getName();

	            
	            if (filename.equals(".") || filename.equals(".."))
	                   continue;

	            if (file.isDirectory())
	            	listfilesDirectory(ftpClient, getter,listdir, filename, getabsolutepath);
	           else{
	        	   if(getabsolutepath)
	                  getter.add(parentDir+currentDir+"/"+filename);
	        	   else
	        		getter.add(currentDir+"/"+filename);
	           }
	          
	        }
	    }
	}
	
 
	public static ArrayList<String> listFolders(FTPClient ftpClient) throws IOException{
		ArrayList<String> ftpfolders=new ArrayList<>();
		
		FTPFile[] files=ftpClient.listDirectories();
		if(files.length>0){
			for (int i = 0; i < files.length; i++) {
				String filename=files[i].getName();
				 if (filename.equals(".") || filename.equals(".."))
	                   continue;
				 else
				    ftpfolders.add(filename);
			}
		}
		return ftpfolders;
	}
	
	
	public static ArrayList<String> listFolders(FTPClient ftpClient, String remotedirpath) throws IOException{
		ArrayList<String> ftpfolders=new ArrayList<>();
		
		FTPFile[] files =ftpClient.listDirectories(remotedirpath);
		if(files.length>0){
			for (int i = 0; i < files.length; i++) {
				FTPFile file=files[i];
				if(file.isDirectory())
					ftpfolders.add(file.getName());
			}
		}
		return ftpfolders;
	}


	
	
  public static void downloadFilesOfDirectory(FTPClient ftpClient, String parentDir,String currentDir, String saveto, boolean createsubdirs, PropertyChangeSupport changelst) throws IOException {
	    
		String listdir = parentDir;
		
		if(!parentDir.endsWith("/") && !currentDir.equals(""))
			listdir+="/"+currentDir+"/";
		else if(parentDir.endsWith("/") && !currentDir.equals(""))
			listdir+=currentDir+"/";
	    
		//System.out.println(listdir);

	    FTPFile[] subdirFiles = ftpClient.listFiles(listdir);
	    if (subdirFiles != null && subdirFiles.length > 0) {
	    	
	        for (FTPFile file : subdirFiles) {
	            String filename = file.getName();
	            
	            
	            if (filename.equals(".") || filename.equals(".."))
	                   continue;

	            if (file.isDirectory()){
	            	if(createsubdirs){
	            		String path=FilenameUtils.concat(saveto, filename);
	            		File f = new File(path);
	            		if(!f.exists()){
	            			f.mkdirs();
	            		}
	            	}
	            	downloadFilesOfDirectory(ftpClient, listdir, filename, saveto, createsubdirs, changelst);
	            	
	            }
	           else{
	        	   String savefilepath=null;
	        	   if(createsubdirs){
	        		   String savefiledir=FilenameUtils.concat(saveto, currentDir); 
	        		   savefilepath=FilenameUtils.concat(savefiledir, filename); 
	        	   }
	        	   else
	        		   savefilepath=FilenameUtils.concat(saveto, filename);

	        	   FileOutputStream output= new FileOutputStream(savefilepath);
	        	   
	        	   if(changelst!=null){
	        		   
	        		   changelst.firePropertyChange(DownloadProgressPanel.CURRENTEFILENAME, "old", filename);
	        		   changelst.firePropertyChange(DownloadProgressPanel.CURRENTEFILESIZE, "old", file.getSize());
	        		   FtpFileStreamListener filesizelist = new FtpFileStreamListener(file.getSize(), changelst);
	        		   ftpClient.setCopyStreamListener(filesizelist);
	        	   }
            
                   boolean ok = ftpClient.retrieveFile(listdir+filename, output);
                   output.close();
                   if(!ok){
                	   File f=new File(savefilepath);
                	   if(f.exists())
                		   f.delete();
                   }
      
	           }
	          
	        }
	    }
	}
  
  public static ArrayList<String> listFilesFtpDirectoryAnonymous(String server, String directory, boolean usepassivemode) throws SocketException, IOException{
		ArrayList<String> filelist = new ArrayList<>();
		
		FTPClient client = new FTPClient();
		client.connect(server);
		if(usepassivemode)
		   client.enterLocalPassiveMode();
		
		client.login("anonymous", "");
		FTPFile[] files = client.listFiles(directory);
		for (FTPFile ftpFile : files) {
			filelist.add(ftpFile.getName());
		}
		return filelist;	
	}
	
	
	public  static ArrayList<String> listDirAndSubDirPathFilesFtpAnonymous(String server, String directory,boolean usepassivemode, boolean showonlyparentdir) throws SocketException, IOException{
		ArrayList<String> filelist = new ArrayList<>();
		
		FTPClient client = new FTPClient();
		client.connect(server);
		if(usepassivemode)
		   client.enterLocalPassiveMode();
		
		client.login("anonymous", "");
		listfilesDirectory(client, filelist, directory, "",showonlyparentdir);
		
		return filelist;	
	 }
	
	
	
	public static boolean getDirAndSubDirFilesFtp(String server, String directory, String savein, boolean createsubdirs){

		
		FTPClient client = new FTPClient();
		
			try {
				client.connect(server);
		        client.login("anonymous", "");
		       // client.setControlEncoding("UTF-8");
		        //client.setFileType(FTP.BINARY_FILE_TYPE);
		        //client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
		        
		        int reply;
		        reply = client.getReplyCode();
		        if (!FTPReply.isPositiveCompletion(reply)) {
		        	client.disconnect();
		            throw new Exception("Exception in connecting to FTP Server");
		        }

				downloadFilesOfDirectory(client, directory, "",savein,createsubdirs,null);
				client.logout();
		        client.disconnect();
				
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
	      
			return true;
	 }
	


	
	// code from http://www.codejava.net/java-se/networking/ftp/how-to-upload-a-directory-to-a-ftp-server
	public static void uploadDirectory(FTPClient ftpClient,String remoteDirPath, String localParentDir, String remoteParentDir)throws IOException {
	 
	    File localDir = new File(localParentDir);
	    File[] subFiles = localDir.listFiles();
	    if (subFiles != null && subFiles.length > 0) {
	        for (File item : subFiles) {
	            String remoteFilePath = remoteDirPath + "/" + remoteParentDir+ "/" + item.getName();
	            if (remoteParentDir.equals("")) {
	                remoteFilePath = remoteDirPath + "/" + item.getName();
	            }
	 
	            if (item.isFile()) {
	                // upload the file
	                String localFilePath = item.getAbsolutePath();
	               
	                boolean uploaded = uploadSingleFile(ftpClient,localFilePath, remoteFilePath);
	                if (uploaded) {
	                    LogMessageCenter.getLogger().addInfoMessage("UPLOADED a file to: "+ remoteFilePath);
	                } else {
	                	 LogMessageCenter.getLogger().addInfoMessage("COULD NOT upload the file: "+ localFilePath);
	                }
	            } else {
	                // create directory on the server
	                boolean created = ftpClient.makeDirectory(remoteFilePath);
	                if (created) {
	                	LogMessageCenter.getLogger().addInfoMessage("CREATED the directory: "+ remoteFilePath);
	                } else {
	                	LogMessageCenter.getLogger().addInfoMessage("COULD NOT create the directory: "+ remoteFilePath);
	                }
	 
	                // upload the sub directory
	                String parent = remoteParentDir + "/" + item.getName();
	                if (remoteParentDir.equals("")) {
	                    parent = item.getName();
	                }
	 
	                localParentDir = item.getAbsolutePath();
	                uploadDirectory(ftpClient, remoteDirPath, localParentDir,parent);
	            }
	        }
	    }
	}
	
	
	
	
	
	// code from http://www.codejava.net/java-se/networking/ftp/how-to-upload-a-directory-to-a-ftp-server
	public static boolean uploadSingleFile(FTPClient ftpClient, String localfilepath, String remotefilepath) throws IOException{
		File localfile = new File(localfilepath);
		InputStream inputStream = new FileInputStream(localfile);
	    try {
	        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
	        return ftpClient.storeFile(remotefilepath, inputStream);
	    } finally {
	        inputStream.close();
	    }
	    
	}
	
	public static boolean sendFileToFtpServer(FTPClient ftpClient,String filepath) throws IOException{
		FileInputStream fis =null;
		try{
			fis = new FileInputStream(filepath);
			String filename=FilenameUtils.getName(filepath);
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.storeFile(filename, fis);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
				ftpClient.disconnect();
			} catch (IOException e) {
				return false;
			}
		}
		
		return true;
	}
	
	public static String getSubPath(String path){
		
		int pos=path.indexOf("/");
		return path.substring(pos+1, path.length());
		
	}

}
