/************************************************************************** 
 *  Orlando Rocha (ornrocha@gmail.com)
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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTime;



public class MTUFileUtils {
	

	public static String buildFilePathWithExtension(String directory, String filename, String extension){ 
		String filepath=null;
		String savedir=null;
		  
		  if(directory.toLowerCase().endsWith(FileSystems.getDefault().getSeparator()))
			  savedir=directory;
		  else
			  savedir=directory+FileSystems.getDefault().getSeparator();
		  
		  String partialfilepath=savedir+filename;
		  filepath=buildFilePathWithExtension(partialfilepath, extension);
		
		 return filepath;
	}
	
	
	public static String addExtensionToFilepathIfNotExists(String dir, String filename, String extension){
		String filepath=null;
		String ext=FilenameUtils.getExtension(filename);

		if(ext==null || ext.isEmpty())
			filepath=FilenameUtils.concat(dir, filename+"."+extension);
		else
			filepath=FilenameUtils.concat(dir, filename);
		return filepath;
	}
	
	public static String forceFilePathExtension(String dir, String filename, String extension){
		String filepath=null;
		String ext=FilenameUtils.getExtension(filename);
		if(ext==null || ext.isEmpty())
			filepath=FilenameUtils.concat(dir, filename+"."+extension);
		else{
			String basename=FilenameUtils.getBaseName(filename);
			filepath=FilenameUtils.concat(dir, basename+"."+extension);
		}
		return filepath;
		
	}
	
	
	public static String buildFilePathWithExtension(String filepath, String extension){ 
		  String filename =null;
			 
		  if(FilenameUtils.getExtension(filepath).toLowerCase().equals(extension))
			  filename=filepath;
		  else{
			  String parentpath=FilenameUtils.getFullPath(filepath);
			  String basename=FilenameUtils.getBaseName(filepath);
			  String ext=FilenameUtils.getExtension(filepath);
			  
			  if(ext!=null && !ext.isEmpty()){
				  if(extension.startsWith("."))
					  filename=parentpath+basename+extension;
				  else
					  filename=parentpath+basename+"."+extension;
			  }
			  else{
				  if(extension.startsWith("."))
					  filename=filepath+extension;
				  else
					  filename=filepath+"."+extension;
				  
			  }  
		  }
			  
		 return filename;
	}
	
	public static String addWordsToFilePath(String originalfilepath, String words){
		String ext=FilenameUtils.getExtension(originalfilepath);
		String basename=FilenameUtils.getBaseName(originalfilepath);
		String path=FilenameUtils.getFullPath(originalfilepath);
		
		if(words!=null){
			basename=basename+words;
		}
		if(ext!=null && !ext.isEmpty())
			return path+basename+"."+ext;
		else
			return path+basename;
	}
	
	
	public static String createFileWithCurrentDateTime(String filepath, String extension) throws IOException{
		    String ext=FilenameUtils.getExtension(filepath);
		    String basename=FilenameUtils.getBaseName(filepath);
		    String path=FilenameUtils.getFullPath(filepath);
		    DateTime dateTime = new DateTime();
		    
		    String newbasename=basename+dateTime.toString();
		    
		    String filename=null;

		    if(extension!=null && !extension.isEmpty())
		    	filename=path+newbasename+"."+extension;
		    else if(ext!=null && !ext.isEmpty())
		    	filename=path+newbasename+"."+ext;
		    else
		    	filename=path+newbasename+"."+"txt";
		    

		    File f = new File(filename);
		    f.createNewFile();
		    
		    return filename;
	}
	
	public static String createFileWithCurrentDateTime(String filepath) throws IOException{
		String ext=FilenameUtils.getExtension(filepath);
		if(ext!=null)
			return createFileWithCurrentDateTime(filepath, ext);
		else
			return createFileWithCurrentDateTime(filepath, null);
	}
	
	
	
	public static String getFileExtension(String f) {  
		   String ext = "";  
		   int i = f.lastIndexOf('.');  
		   if (i > 0 &&  i < f.length() - 1) {  
		      ext = f.substring(i + 1).toLowerCase();  
		   }  
		   return ext;  
		}  
	
	
	public static void deleteFile(String filepath){
		File f=new File(filepath);
		if(f.exists())
			f.delete();
	}
	
	public static void deleteFolder(String dirpath) throws IOException {
		File f=new File(dirpath);
		if(f.exists() &&f.isDirectory() )
			FileUtils.deleteDirectory(new File(dirpath));
	}
	
	public static void createDirectory(String path){
		File f = new File(path);
		if(!f.exists())
			f.mkdirs();		
	}
	
	public static String createDirInside(String parentpath, String namedir){
		String savein=FilenameUtils.concat(parentpath, namedir);
		File f =new File(savein);
		if(!f.exists())
			f.mkdirs();
		return savein;
	}
	
	
	public static String checkifFileExistsandReturnNewName(String directory, String filename, String fileextension) {
		   
	    int num = 1;
	   // String filepath = file.substring(0, temp.length()-4);
	    String filepath = directory+filename;
	    String pathtofile = "";
	    
	    boolean ex = filename.matches("^.*_(\\d+)$");
	    
	    
	    if(!ex){
	    	return checkifFileExistsandReturnNewName(directory,filename+"_"+num, fileextension);	
	    }
	     
	    else{
	        Pattern p = Pattern.compile("(^.*_)(\\d+)$");
	        Matcher m = p.matcher(filename);
	        if (m.find()){
	    	         String n = m.group(2);
	    	         num = Integer.parseInt(n);

	    	         filepath = directory+m.group(1)+n+fileextension;

	    	         if (!new File(filepath).exists()){
	    	        	 pathtofile= filepath;
	    	         }
	    	         else
	    	        	 return checkifFileExistsandReturnNewName(directory,m.group(1)+(num+1), fileextension);        	 
	        }

	     }
	    
      return pathtofile;
  }
	
	
	
/*	public static void SaveIndexedMapToFile(String filepath, IndexedHashMap<String, String> map) throws IOException{
		 ArrayList<String> res = new ArrayList<String>();
		 for (int i = 0; i < map.size(); i++) {
			String line = map.getKeyAt(i)+";"+map.getValueAt(i);
			res.add(line);
		}
		 SaveArrayLisToFile(filepath, res);
	 }*/
	 
	 public static void SaveorAppendArrayLisToFile(String path, ArrayList<String> lines, boolean append) throws IOException{
		 File f = new File(path);
		 if(!f.exists())
			 f.createNewFile();
		 FileUtils.writeLines(f, lines, append);
	 }
	 
	 public static void SaveArrayLisToFile(String path, ArrayList<String> lines) throws IOException{
		 SaveorAppendArrayLisToFile(path, lines, false);
		 
	 }
	 
	 
	 public static void SaveHashSetToFile(String path, HashSet<String>lines) throws IOException{
		 File f = new File(path);
		 FileUtils.writeLines(f, lines);
	 }
	 
	
	

		 
		 public static String stringFromStream(InputStream in) throws IOException
		 {
		     BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		     StringBuilder out = new StringBuilder();
		     String newLine = System.getProperty("line.separator");
		     String line;
		     while ((line = reader.readLine()) != null) {
		         out.append(line);
		         out.append(newLine);
		     }
		     return out.toString();
		 }
   
		 
		 public static ArrayList<String> linesFromStream(InputStream in) throws IOException
		 {
		     BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		     ArrayList<String> lines = new ArrayList<>();
		     String line;
		     while ((line = reader.readLine()) != null) {
		    	 lines.add(line);
		     }
		     return lines;
		 }
		 
		 public static void copyFile(String sourcefilepath, String destfilepath) throws IOException{
			 copyFile(new File(sourcefilepath), new File(destfilepath));
		 }
		 
		 public static void copyFile(File source, File dest) throws IOException {
			    Files.copy(source.toPath(), dest.toPath());
			}

}
