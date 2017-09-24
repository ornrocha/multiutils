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
package pt.ornrocha.stringutils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.text.StrBuilder;

import pt.ornrocha.collections.MTUCollectionsUtils;

public class MTUStringUtils {
	
	 public static ArrayList<String> replacetoUnderscore(ArrayList<String> orig){
			ArrayList<String> res = new ArrayList<String>();
			
			for (String st : orig) {
				if(st.contains("-")){
					String repl=st.replace("-", "_");
					res.add(repl);
				}
				else
					res.add(st);
					
			}
			
			return res;
			
		}
	 
	 
	 public static String getStringFromObject(Object obj, String delimiter){
		 String delm=",";
		 
		 if(delimiter!=null)
			 delm=delimiter;
		 
		 if(obj instanceof List<?>){
            return MTUCollectionsUtils.getStringFromList((List<?>) obj, delm);
		 }
		 else if(obj instanceof Set<?>){
			 return MTUCollectionsUtils.getStringFromSet((Set<?>) obj, delm);
		 }
		 else
		    return String.valueOf(obj);
	 }
	 
	 public static String setWordlowercasefirstletteruppercase(String word){
		 return word.substring(0, 1).toUpperCase()+word.substring(1, word.length()).toLowerCase();
	 }
	 
	 public static String setFirstLetterLowerCase(String word){
		 return word.substring(0, 1).toLowerCase()+word.subSequence(1, word.length());
	 }
	 
	 public static String setFirstLetterUpperCase(String word){
		 return word.substring(0, 1).toUpperCase()+word.subSequence(1, word.length());
	 }
	 
	 public static String setOnlyLastLetterUpperCaseIfLenghHigherThan(String word, int frompos){
		 if(word.length()<frompos)
			 return word.toLowerCase();
		 else
			 return word.substring(0, word.length()-1).toLowerCase()+word.substring(word.length()-1, word.length()).toUpperCase();
	 }
	 
	 public static void appendTextToFile(String filename, String appendtext) throws IOException{
		    FileWriter fw = new FileWriter(filename,true);
            fw.write(appendtext);
            fw.close();
	 }
	 
	 public static String convertStringArrayToString(String[] array, String delimiter){
		 //StringBuilder str=new StringBuilder();
		 StrBuilder str=new StrBuilder();
	    	for (int i = 0; i < array.length; i++) {
				str.append(array[i]);
				if(i<array.length-1)
					str.append(delimiter);
			}
	    	return str.toString();
	 }
	 
	 public static String convertDoubleArrayToString(double[] array, String delimiter){
		 //StringBuilder str=new StringBuilder();
		 StrBuilder str=new StrBuilder();
	    	for (int i = 0; i < array.length; i++) {
				str.append(array[i]);
				if(i<array.length-1)
					str.append(delimiter);
			}
	    	return str.toString();
	 }
	 
	 
	 public static  String convertIntegerArrayToString(int[] array, String delimiter){
		 StringBuilder str=new StringBuilder();
	    	for (int i = 0; i < array.length; i++) {
				str.append(array[i]);
				if(i<array.length-1)
					str.append(delimiter);
			}
	    	return str.toString();
	 }
	 
	 

	  public static String convertDoubleMatrixToString(double[][] m, String delimiter){
		     //StringBuilder str=new StringBuilder();
		     StrBuilder str=new StrBuilder();
			 for (int i = 0; i < m.length; i++) {
				 for (int j = 0; j < m[0].length; j++) {
					str.append(m[i][j]+delimiter);
				 }
				 str.append("\n");
			}
			 return str.toString();
		 }
	  
	  public static String convertDoubleMatrixToString(double[][] m, String delimiter, ArrayList<String> appendfirst){
		     //StringBuilder str=new StringBuilder();
		     StrBuilder str=new StrBuilder();
		     for (int i = 0; i < appendfirst.size(); i++) {
				str.append(appendfirst.get(i)+"\n");
			  }
		     
		     
			 for (int i = 0; i < m.length; i++) {
				 for (int j = 0; j < m[0].length; j++) {
					str.append(m[i][j]+delimiter);
				 }
				 str.append("\n");
			}
			 return str.toString();
		 }
	  
	  
	  public static String convertIntegerMatrixToString(int[][] m, String delimiter, ArrayList<String> appendfirst){
		     //StringBuilder str=new StringBuilder();
		     StrBuilder str=new StrBuilder();
		     for (int i = 0; i < appendfirst.size(); i++) {
				str.append(appendfirst.get(i)+"\n");
			  }
		     
		     
			 for (int i = 0; i < m.length; i++) {
				 for (int j = 0; j < m[0].length; j++) {
					str.append(m[i][j]+delimiter);
				 }
				 str.append("\n");
			}
			 return str.toString();
		 }
	  
	  public static String convertArrayListToString(ArrayList<String> liststr,String delimiter){
		  StringBuilder sb = new StringBuilder();
		  
		  for (int i = 0; i < liststr.size(); i++) {
			  sb.append(liststr.get(i));
			  if(i<(liststr.size()-1))
				  sb.append(delimiter);
		  }
		return sb.toString();
	  }
	  
	  
	  // code from: http://stackoverflow.com/questions/4666647/how-to-create-user-friendly-unique-ids-uuids-or-other-unique-identifiers-in-jav
	  public static String shortUUID() {
		  UUID uuid = UUID.randomUUID();
		  long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
		  return Long.toString(l, Character.MAX_RADIX);
		}
	  
	  public static List<String> getElementsListFromString(String str, String delimiter, List<String> listcontainer){
		  
		  String[] elems=str.split(delimiter);
		  if(elems.length>0){
			  for (int i = 0; i < elems.length; i++) {
				listcontainer.add(elems[i].trim());
			  }
			  
		  }
		  return listcontainer;
	  }
	  
	  public static String getRepeatedWordXTimes(String word, int ntimes){
		  //StringBuilder str=new StringBuilder();
		  StrBuilder str=new StrBuilder();
		  for (int i = 0; i < ntimes; i++) {
			  str.append(word);
		  }
		 return str.toString(); 
	  }
	  
	  
	  public static int getHashForString(String str){
		  int hash = 7;
			for (int i = 0; i < str.length(); i++) {
			    hash = hash*31 + str.charAt(i);
			}
			
			return hash;
	  }
	  
	  public static void main(String[] args){
		  System.out.println(shortUUID());
		  
	  }
}
