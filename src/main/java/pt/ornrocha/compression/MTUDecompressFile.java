/************************************************************************** 
 * Copyright 2012 - 2017, Orlando Rocha (ornrocha@gmail.com)
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
package pt.ornrocha.compression;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.FilenameUtils;

public class MTUDecompressFile {
	
	
	public static String GZip(String compressedfilepath, String newfilename, String destinationdir){
		 String decompressedfilepath=FilenameUtils.concat(destinationdir, newfilename);
		 
		 byte[] buffer = new byte[1024];
		 try{
			 
			 GZIPInputStream gzstream=new GZIPInputStream(new FileInputStream(compressedfilepath));
			 FileOutputStream outfile = new FileOutputStream(decompressedfilepath);
			 
			 int filelength;
		     while ((filelength = gzstream.read(buffer)) > 0) {
		    	 outfile.write(buffer, 0, filelength);
		        }
		 
		     gzstream.close();
		     outfile.close();
			 return decompressedfilepath; 
		 } catch (IOException ex) {
			ex.printStackTrace();
		}
       return null;
	}
	
	public static String GZip(String compressedfilepath, String destinationdir){
	     String filename=FilenameUtils.getBaseName(compressedfilepath);
	     return GZip(compressedfilepath, filename, destinationdir);
	}
	

}
