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
package pt.ornrocha.ioutils.writers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MTUWriterUtils {
	
	
	public static synchronized void appendDataTofile(String filepath, String data) throws IOException{
		File file=new File(filepath);
		if(!file.exists())
			file.createNewFile();
		
		FileWriter fileWritter = new FileWriter(filepath,true);
		BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
        bufferWritter.write(data);
        bufferWritter.close();
        fileWritter.close();
	}
	
	
	
	public static synchronized void writeDataTofile(String filepath, String data) throws IOException{
		writeDataTofile(filepath, data, null);
	}
	
	public static synchronized void writeDataTofile(String filepath, String data,String ext) throws IOException{
			File file=null;
			if(ext!=null)
				file=new File(filepath+"."+ext);
			else
				file=new File(filepath);

		   if(!file.exists())
			  file.createNewFile();
		
		   FileWriter fileWritter = new FileWriter(filepath,false);
		   BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
           bufferWritter.write(data);
           bufferWritter.close();
           fileWritter.close();
	}
	
	public static <E> void writeListToFile(String filepath, List<E> list) throws IOException{
		
		StringBuilder strb=new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			strb.append(list.get(i)+"\n");
		}
		
		writeDataTofile(filepath, strb.toString());
	}
	

	public static <E> void writeListToFile(String filepath, Set<E> list) throws IOException{
		
		StringBuilder strb=new StringBuilder();
		for (E e : list) {
			strb.append(e+"\n");
		}
		writeDataTofile(filepath, strb.toString());
	}
	

	public static <K, V> void writeMapToFile(String filepath, Map<K, V> map, String delimiter) throws IOException{
		
		StringBuilder strb=new StringBuilder();
		for (Map.Entry<K, V> elem : map.entrySet()) {
			strb.append(elem.getKey()+delimiter+elem.getValue()+"\n");
		}

		writeDataTofile(filepath, strb.toString());
	}
	
	
	public static void writeStringWithFileChannel(String towrite, String outputfilepath, int buffercapacity) throws IOException{
		
		byte [] inputBytes = towrite.getBytes();
		ByteBuffer buffer =null;
		if(buffercapacity>0){
			ByteBuffer buf = ByteBuffer.allocate(buffercapacity);
			buf.clear();
			buf.put(inputBytes);
		}
		else
		   buffer = ByteBuffer.wrap(inputBytes);

		FileOutputStream fos = new FileOutputStream(outputfilepath);
		FileChannel fileChannel = fos.getChannel();
		fileChannel.write(buffer);
		fileChannel.close();
		fos.close();
	}
	
	public static void writeDoubleMatrixToFile(String outputfile, double[][] matrix,String delimiter, ArrayList<String> header, ArrayList<String> rownames) throws IOException{
		
		StringBuilder str=new StringBuilder();
		
		if(header!=null){
			int colcount=matrix[0].length;
			if(colcount!=header.size())
				throw new InputMismatchException("The input matrix have a different number of columns comparing to header list size");
			else{
				if(rownames!=null)
					str.append("matrix"+delimiter);
				
				for (int i = 0; i < header.size(); i++) {
					str.append(header.get(i));
					if(i<header.size()-1)
						str.append(delimiter);
				}
		       str.append("\n");
			}
				
		}
		if(rownames!=null){
			int rowscount=matrix.length;
			if(rowscount!=rownames.size())
				throw new InputMismatchException("The input matrix have a different number of rows comparing to rownames list size");	
		}
		
		
		for (int i = 0; i < matrix.length; i++) {
			double[] row=matrix[i];
			if(rownames!=null)
				str.append(rownames.get(i)+delimiter);
			
			for (int j = 0; j < row.length; j++) {
				str.append(row[j]);
				if(j<row.length-1)
					str.append(delimiter);
			}
			str.append("\n");
		}
	  
		writeStringWithFileChannel(str.toString(), outputfile, 0);
	}
	
	
	
	public static void writeIntegerMatrixToFile(String outputfile, int[][] matrix,String delimiter, ArrayList<String> header, ArrayList<String> rownames) throws IOException{
		
		StringBuilder str=new StringBuilder();
		
		if(header!=null){
			int colcount=matrix[0].length;
			if(colcount!=header.size())
				throw new InputMismatchException("The input matrix have a different number of columns comparing to header list size");
			else{
				if(rownames!=null)
					str.append("matrix"+delimiter);
				
				for (int i = 0; i < header.size(); i++) {
					str.append(header.get(i));
					if(i<header.size()-1)
						str.append(delimiter);
				}
		       str.append("\n");
			}
				
		}
		if(rownames!=null){
			int rowscount=matrix.length;
			if(rowscount!=rownames.size())
				throw new InputMismatchException("The input matrix have a different number of rows comparing to rownames list size");	
		}
		
		
		for (int i = 0; i < matrix.length; i++) {
			int[] row=matrix[i];
			if(rownames!=null)
				str.append(rownames.get(i)+delimiter);
			
			for (int j = 0; j < row.length; j++) {
				str.append(row[j]);
				if(j<row.length-1)
					str.append(delimiter);
			}
			str.append("\n");
		}
	  
		writeStringWithFileChannel(str.toString(), outputfile, 0);
	}
	
	
	
	
	public static void writeIntegerMatrixToFile(String outputfile, int[][] matrix,String delimiter) throws IOException{
		
		StringBuilder str=new StringBuilder();
		for (int i = 0; i < matrix.length; i++) {
			int[] row=matrix[i];
			for (int j = 0; j < row.length; j++) {
				str.append(String.valueOf(row[j]));
				if(j<row.length-1)
					str.append(delimiter);
			}
			str.append("\n");
			
		}
		writeStringWithFileChannel(str.toString(), outputfile, 0);
	}
	
	
	/*public static void writeIntegerMatrixToFile(String outputfile, int[][] matrix,String delimiter, ArrayList<String> header, ArrayList<String> rownames) throws IOException{
		
		StringBuilder str=new StringBuilder();
		
		if(header!=null){
			int colcount=matrix[0].length;
			if(colcount!=header.size())
				throw new InputMismatchException("The input matrix have a different number of columns comparing to header list size");
			else{
				if(rownames!=null)
					str.append("matrix"+delimiter);
				
				for (int i = 0; i < header.size(); i++) {
					str.append(header.get(i));
					if(i<header.size()-1)
						str.append(delimiter);
				}
		       str.append("\n");
			}
				
		}
		if(rownames!=null){
			int rowscount=matrix.length;
			if(rowscount!=rownames.size())
				throw new InputMismatchException("The input matrix have a different number of rows comparing to rownames list size");	
		}
		
		
		for (int i = 0; i < matrix.length; i++) {
			int[] row=matrix[i];
			if(rownames!=null)
				str.append(rownames.get(i)+delimiter);
			
			for (int j = 0; j < row.length; j++) {
				str.append(row[j]);
				if(j<row.length-1)
					str.append(delimiter);
			}
			str.append("\n");
		}
	  
		writeStringWithFileChannel(str.toString(), outputfile, 0);
	}*/

}
