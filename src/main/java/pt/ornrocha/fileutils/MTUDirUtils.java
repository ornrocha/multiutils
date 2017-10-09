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
package pt.ornrocha.fileutils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import pt.ornrocha.stringutils.MTUStringUtils;
import pt.ornrocha.timeutils.MTUTimeUtils;

public class MTUDirUtils {
	

	public static String checkandsetDirectory(String directory){
		
		boolean oc = directory.matches("^.*(/)$");
		String dirpath ="";
		if (oc)
			dirpath = directory.substring(0, directory.length()-1);
		else
			dirpath = directory;
		
		File f = new File(dirpath);
		
		if(f.exists() && f.isDirectory())
			return dirpath+"/";
		else{
			f.mkdir();
		    return dirpath+"/";
		}
	}
	
	public static boolean checkDirectory(String dir){
		File f=new File(dir);
		if(f.exists() && f.isDirectory())
			return true;
		else
			return f.mkdirs();
	}
	
	public static String makeDirectory(String pathname){
		File f=new File(pathname);
		if(!f.exists()){
			f.mkdirs();		
		}
		return pathname;
	}
	
	public static String makeDirectoryWithDate(String parentpath, String foldername){
		return makeDirectory(FilenameUtils.concat(parentpath, foldername+"_"+MTUTimeUtils.getCurrentDateAndTime("yyyy-MM-dd_HH-mm-ss")));
	}
	
	public static String makeDirectoryWithUniqueID(String parentpath, String foldername){
		return makeDirectory(FilenameUtils.concat(parentpath, foldername+"_"+MTUStringUtils.shortUUID()));
	}
	
	public static String makeDirectoryWithUniqueIDAndDate(String parentpath, String foldername){
		String filename=foldername+"_"+MTUStringUtils.shortUUID()+"_"+MTUTimeUtils.getCurrentDateAndTime("yyyy-MM-dd_HH-mm-ss");
		return makeDirectory(FilenameUtils.concat(parentpath, filename));
	}
	
	
   public static String checkandsetDirectories(String directory){
		
		boolean oc = directory.matches("^.*(/)$");
		String dirpath ="";
		if (oc)
			dirpath = directory.substring(0, directory.length()-1);
		else
			dirpath = directory;
		
		File f = new File(dirpath);
		
		if(f.exists() && f.isDirectory())
			return dirpath+"/";
		else{
			f.mkdirs();
		    return dirpath+"/";
		}
	}
   
   public static void deleteDirectory(String dirpath) throws IOException{
	   FileUtils.deleteDirectory(new File(dirpath));
   }
   
   public static ArrayList<String> getFilesWithExtensionInsideDirectory(String directory, String fileextension){
	   ArrayList<String> listfiles=new ArrayList<>();
	   File folder =new File(directory);
	   File[] files=folder.listFiles();
	   
	   for (File file : files) {
		  if(fileextension==null && file.isFile())
			  listfiles.add(file.getAbsolutePath());
		  else if(file.isFile()){
			  String ext=FilenameUtils.getExtension(file.getAbsolutePath());
			  if(ext.equals(fileextension))
				  listfiles.add(file.getAbsolutePath());
		  }	  
	   }
	   return listfiles;
   }
   
   public static ArrayList<String> getFilePathsInsideDirectory(String directory){
	   ArrayList<String> listoffiles=new ArrayList<>();
	   File folder =new File(directory);
	   File[] files=folder.listFiles();
	   for (File file : files) {
		   if(file.isFile()){
			   listoffiles.add(file.getAbsolutePath());
		  }	  
	   }
	   return listoffiles;
   }
   
   public static boolean isFolderWritable(String folderpath){
	   File f=new File(folderpath);
	   return f.canWrite();
   }
   
   /**
    * One of the boolean parameters must be false, otherwise consider both as true
    * @param directory
    * @param getfiles
    * @param getfolders
    * @return
    */
   
   public static ArrayList<String> getFilePathsInsideDirectory(String directory, boolean getfiles, boolean getfolders){
	  
	   ArrayList<String> listoffiles=new ArrayList<>();
	   File folder =new File(directory);
	   File[] files=folder.listFiles();
	   
	   if(!getfiles && !getfolders){
		   getfiles=true;
		   getfolders=true;
	   }
	   
	   if(files!=null)
	   for (File file : files) {
		   if(getfiles && file.isFile() && !getfolders){
			   listoffiles.add(file.getAbsolutePath());
		    }
		   else if(getfolders && file.isDirectory() && !getfiles){
			   listoffiles.add(file.getAbsolutePath());
		   }
		   else if(getfiles && getfolders){
			   listoffiles.add(file.getAbsolutePath());
		   }
	   }
	   
	   return listoffiles;
   }
   
   
   public static ArrayList<String> getLastDirectoryInTree(String directory){
	   
	   File folder =new File(directory);
	   File[] files=folder.listFiles();
	   
	   int ndir=0;
	   ArrayList<String> collector=new ArrayList<>();
	   for (File file : files) {
		  if(file.isDirectory()){
			  ArrayList<String> indirs=getLastDirectoryInTree(file.getAbsolutePath());
			  if(indirs.size()==0)
				  collector.add(file.getAbsolutePath());
			  else
				  collector.addAll(indirs);
				  
		  }
	   }
	   return collector;
   }
   
   

   public static ArrayList<String> getLastDirectoryInTree(String directory, ArrayList<String> ignorenames){
	   
	   File folder =new File(directory);
	   File[] files=folder.listFiles();
	   
	   ArrayList<String> collector=new ArrayList<>();
	   for (File file : files) {
		  String dirname=FilenameUtils.getBaseName(file.getAbsolutePath());
		  if(file.isDirectory() && !ignorenames.contains(dirname)){
			  ArrayList<String> indirs=getLastDirectoryInTree(file.getAbsolutePath(), ignorenames);
			  if(indirs.size()==0)
				  collector.add(file.getAbsolutePath());
			  else
				  collector.addAll(indirs);
				  
		  }
	   }
	   return collector;
   }
   
 
   public static ArrayList<String> getLastDirectoryInTree(String directory, String... ignorenames){
	   ArrayList<String> ignore=new ArrayList<>(Arrays.asList(ignorenames));
	   return getLastDirectoryInTree(directory, ignore);
   }
   
   
   public static ArrayList<String> getFilesWithCertainNameInsideFoldersInFolder(String maindir, ArrayList<String> filesnames){
	   ArrayList<String> listoffiles=new ArrayList<>();
	   ArrayList<String> indirs=getLastDirectoryInTree(maindir);
	   
	   for (int i = 0; i < indirs.size(); i++) {
		
		   File folder =new File(indirs.get(i));
		   File[] files=folder.listFiles();
		   
		   for (File file : files) {
			   if(file.isFile()){
				   String filename=FilenameUtils.getBaseName(file.getAbsolutePath());
				   if(filesnames.contains(filename))
					   listoffiles.add(file.getAbsolutePath());
			  }	  
		   }
	   
	   }
	   
	   return listoffiles;
   }
   
   public static String checkEnumeratedDirAndIncrementIfExists(String dir, String dirname){
	   
	   ArrayList<String> dirnames= getFilePathsInsideDirectory(dir, false, true);
	   
	   Pattern pat=Pattern.compile(dirname.toLowerCase()+"(\\d+)");
	   
	   int currnumber=0;
	   
	   for (int i = 0; i < dirnames.size(); i++) {
		
		   String insidedirname=FilenameUtils.getBaseName(dirnames.get(i));
		   
		   Matcher m=pat.matcher(insidedirname.toLowerCase());
		   if(m.find()){
			   int n=Integer.parseInt(m.group(1));
			   if(n>currnumber)
				   currnumber=n;
		   } 
	   }
	   
	   return FilenameUtils.concat(dir, dirname+(currnumber+1));
	   
   }
   
   
   public static void main(String[] args){
	   //System.out.println(getFilePathsInsideDirectory("/home/orocha/discodados/Biclustering/datasets", false, true));
	  // System.out.println(getLastDirectoryInTree("/home/orocha/discodados/ApenasTrabalho/Resultados_biclustering/Evaluation/Mydata/mixed"));
	   System.out.println(checkEnumeratedDirAndIncrementIfExists("/home/orocha/discodados/ApenasTrabalho/Resultados_biclustering/Evaluation/teste", "Results"));
   }

}
