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
package pt.ornrocha.ioutils.readers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;



public class MTUReadUtils {
	
	
	public static String readFileToString(File file) throws IOException{
		FileInputStream instr=new FileInputStream(file);
		return IOUtils.toString(instr);
	}
	
	public static String readFileToString(String filepath) throws IOException{
		return readFileToString(new File(filepath));
	}
	
	public static List<String> readFileLines(String filepath) throws IOException{
		 return  FileUtils.readLines(new File(filepath));
	 }
	

	
	
	public static ArrayList<String> readFileLinesRemoveEmptyLines(String filepath) throws IOException{
		 ArrayList<String> lines=(ArrayList<String>) readFileLines(filepath);
		 ArrayList<String> filtered=new ArrayList<>();
		 for (int i = 0; i < lines.size(); i++) {
			if(lines.get(i)!=null && !lines.get(i).isEmpty())
				filtered.add(lines.get(i));
		}
		
		 return  filtered;
	 }
	
	public static List<String> readFileLines(File file) throws IOException{
		 return  readFileLines(file.getAbsolutePath());
	 }
	
	public static ArrayList<String> columnFileLines(String filepath, int columnindex,String delimiter) throws IOException{
		return columnFileLines(new File(filepath), columnindex, delimiter);
	}
	
	
	public static List<String> getLinesOfString(String input) throws IOException{
		return IOUtils.readLines(new StringReader(input));
	}
	
	public static ArrayList<String> columnFileLines(File file, int columnindex,String delimiter) throws IOException{
		ArrayList<String> res=new ArrayList<>();
		ArrayList<String> lines=(ArrayList<String>) readFileLines(file);
		for (int i = 0; i < lines.size(); i++) {
			String[] elems=lines.get(i).split(delimiter);
			if(columnindex>elems.length)
				res.add("");
			else
				res.add(elems[columnindex]);
		}
		//System.out.println(res);
		return res;
	}
	
	
	public static HashSet<String> readFileAndGetHashSet(String filepath) throws IOException{
		 return new HashSet<>(readFileLines(filepath));
	 }
	
/*	public static  IndexedHashMap<String, String> readIndexedHashMapFromFile(String file,String delimiter, int keypos, int valuepos) throws IOException{
		 ArrayList<String> input = (ArrayList<String>) readFileLines(file);
		 IndexedHashMap<String, String> map = new IndexedHashMap<>();
		 
		 for (String line : input) {
			String[] elems = line.split(delimiter);
			if(elems.length<=valuepos)
				map.put(elems[keypos], "");
			else
			   map.put(elems[keypos], elems[valuepos]);
		}
		 return map;
	 }*/
	
	public static  Map<String, String> readStringMapFromFile(String file,String delimiter, int keypos, int valuepos, Map<String,String> container) throws IOException{
		 ArrayList<String> input = (ArrayList<String>) readFileLines(file);
		 //IndexedHashMap<String, String> map = new IndexedHashMap<>();
		 
		 for (String line : input) {
			String[] elems = line.split(delimiter);
			if(elems.length<=valuepos)
				container.put(elems[keypos], "");
			else
				container.put(elems[keypos], elems[valuepos]);
		}
		 return container;
	 }
	
	
	
	public static Map<String, String> mapElementListFromFile(String filepath, String delimiter, int keyColumn, int valueColumn, boolean ignoreheader, Map<String, String> mapcontainer) throws InstantiationException, IllegalAccessException, IOException{
		return mapElementListFromFile(filepath, delimiter, keyColumn, valueColumn, ignoreheader, mapcontainer, null);
	}
	
	
	public static Map<String, String> mapElementListFromFile(String filepath, String delimiter, int keyColumn, int valueColumn, boolean ignoreheader, Map<String, String> mapcontainer, String ignoreelemsregex) throws InstantiationException, IllegalAccessException, IOException{
		
		Map<String, String> res=mapcontainer;
		
		BufferedReader br = null;


		String line=null;

		br = new BufferedReader(new FileReader(filepath));
		line = br.readLine();
        int linenumber=0;
		
        while (line!= null) {
		
        	if(ignoreheader && linenumber==0){
			
        		line=br.readLine();
				linenumber++;
				}
        	else{
        		String[] elems=line.split(delimiter);
        		if(keyColumn<elems.length && valueColumn<elems.length && !elems[keyColumn].isEmpty() && !elems[valueColumn].isEmpty())
        			if(ignoreelemsregex!=null){
        				if(!elems[keyColumn].matches(ignoreelemsregex) && !elems[valueColumn].matches(ignoreelemsregex))
        					res.put(elems[keyColumn], elems[valueColumn]);
        			}
        			else
        				res.put(elems[keyColumn], elems[valueColumn]);
        		
        		line=br.readLine();
        		linenumber++;
			   }
        	}
        
        br.close();

		 if(res.size()>0)
			 return res;
		 return null;
	   }
	
	public static double[][] readDoubleMatrixFromFile(String file, String delimiter) throws IOException{
		
		ArrayList<String> lines=(ArrayList<String>) readFileLines(file);
		
		double [][] res;
		String header=lines.get(0);
		boolean userowname=false;
		int totalrows=lines.size();
		int totalcolumns=0;
		
		String[] headerelems=header.split(delimiter);
		totalcolumns=headerelems.length;
		double[] tmpheaderval=new double[totalcolumns];
		
		for (int i = 0; i < headerelems.length; i++) {
			try {
				tmpheaderval[i]=Double.parseDouble(headerelems[i]);
			} catch (Exception e) {
				totalrows=totalrows-1;
				tmpheaderval=null;
				break;
			}	
		}
		
		
		try {
			double linerownamecheck=Double.parseDouble(lines.get(1).split(delimiter)[0]);
			if(lines.size()>=3)
				linerownamecheck=Double.parseDouble(lines.get(2).split(delimiter)[0]); 	
		} catch (Exception e) {
			userowname=true;
		}
		
		
		if(userowname)
			res=new double[totalrows][totalcolumns-1];
		else
			res=new double[totalrows][totalcolumns];
			
		boolean useheader=false;
		if(tmpheaderval!=null )
			res[0]=tmpheaderval;
		else
			useheader=true;
	
		for (int i = 1; i <lines.size(); i++) {
			String[] elems =lines.get(i).split(delimiter);
			
			double[] row;
			int start=0;
			if(userowname){
				row=new double[totalcolumns-1];
				start=1;
			}
			else
				row=new double[totalcolumns];
			
			for (int j = start; j < elems.length; j++) {
				if(userowname)
					row[j-1]=Double.valueOf(elems[j]);
				else
					row[j]=Double.valueOf(elems[j]);
			}
			
			if(useheader)
			   res[i-1]=row;
			else
				res[i]=row;
		}	
		
	 return res;
		
	}
	
	
	public static int[][] readIntegerMatrixFromFile(String file, String delimiter) throws IOException{
		
		ArrayList<String> lines=(ArrayList<String>) readFileLines(file);
		
		int [][] res;
		String header=lines.get(0);
		boolean userowname=false;
		int totalrows=lines.size();
		int totalcolumns=0;
		
		String[] headerelems=header.split(delimiter);
		totalcolumns=headerelems.length;
		int[] tmpheaderval=new int[totalcolumns];
		
		for (int i = 0; i < headerelems.length; i++) {
			try {
				tmpheaderval[i]=Integer.parseInt(headerelems[i]);
			} catch (Exception e) {
				totalrows=totalrows-1;
				tmpheaderval=null;
				break;
			}	
		}
		
		
		try {
			int linerownamecheck=Integer.parseInt(lines.get(1).split(delimiter)[0]);
			if(lines.size()>=3)
				linerownamecheck=Integer.parseInt(lines.get(2).split(delimiter)[0]); 	
		} catch (Exception e) {
			userowname=true;
		}
		
		
		if(userowname)
			res=new int[totalrows][totalcolumns-1];
		else
			res=new int[totalrows][totalcolumns];
			
		boolean useheader=false;
		if(tmpheaderval!=null )
			res[0]=tmpheaderval;
		else
			useheader=true;
	
		for (int i = 1; i <lines.size(); i++) {
			String[] elems =lines.get(i).split(delimiter);
			
			int[] row;
			int start=0;
			if(userowname){
				row=new int[totalcolumns-1];
				start=1;
			}
			else
				row=new int[totalcolumns];
			
			for (int j = start; j < elems.length; j++) {
				if(userowname)
					row[j-1]=Integer.valueOf(elems[j]);
				else
					row[j]=Integer.valueOf(elems[j]);
			}
			
			if(useheader)
			   res[i-1]=row;
			else
				res[i]=row;
		}	
		
	 return res;
		
	}

}
