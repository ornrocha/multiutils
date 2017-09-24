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
package pt.ornrocha.jsonutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.javatuples.Triplet;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.common.collect.Multimap;

import pt.ornrocha.arrays.MTUMatrixUtils;
import pt.ornrocha.stringutils.MTUStringUtils;

public class MTUJsonUtils {
	
	
	public static ArrayList<JSONObject> getJSONObjElemsFromJSONArray(JSONArray array){
		ArrayList<JSONObject> elems = new ArrayList<>();
		
		for (int i = 0; i < array.length(); i++) {
			elems.add(array.getJSONObject(i));
		}
		return elems;
	}
	
	
	public static ArrayList<String> getStringElemsFromJSONArray(JSONArray array){
		ArrayList<String> elems = new ArrayList<>();
		for (int i = 0; i < array.length(); i++) {
			String str =array.getString(i);
			elems.add(str);
		}
		return elems;
	}
	
	
	public static ArrayList<Integer> getIntegerElemsFromJSONArray(JSONArray array){
		ArrayList<Integer> elems = new ArrayList<>();
		for (int i = 0; i < array.length(); i++) {
			int r =array.getInt(i);
			elems.add(r);
		}
		return elems;
	}
	
	
	
	
	public static HashSet<String> getHashSetStringElemsFromJSONArray(JSONArray array){
		HashSet<String> elems = new HashSet<>();
		
		for (int i = 0; i < array.length(); i++) {
			String str =array.getString(i);
			elems.add(str);
		}
		return elems;

	}
	
	
	public static boolean getBooleanStateFromJSONobj(JSONObject info, String key){
		
		if(info.has(key)){
			return (boolean) info.get(key);
		}
		return false;
	}
	
	
	
	public static String getStringElement(JSONObject jobj, String key){
		if(jobj.has(key))
		  return jobj.getString(key);
		
		return null;
	}
	
	
	public static String getStringElementIgnorePrimitiveType(JSONObject jobj, String key){
		if(jobj.has(key)){
			
			Object obj = jobj.get(key);
			
			if(!(obj instanceof String)){
				return String.valueOf(obj);
			}
			else
				return (String) jobj.get(key);
		}
		
		return null;
	}
	
	public static int getIntegerElement(JSONObject jobj, String key){
		if(jobj.has(key))
		  return jobj.getInt(key);
       
		return -1;
	}
	
	

	
	public static void putJSonArrayElemsIntoMultiMap(Multimap<String, String> map, JSONArray arrayinfo, String key){
		
		for (int i = 0; i < arrayinfo.length(); i++) {
			String value=String.valueOf(arrayinfo.get(i));
			map.put(key, value);
		}
		
	}
	
	
   
	public static void putJSonobjectsElemsIntoMultiMap(Multimap<String, String> map, JSONObject jobj){
		
         
		Iterator<String> keys = jobj.keys();
		
		while(keys.hasNext()){
	        String key = keys.next();
	        String value=String.valueOf(jobj.get(key));
	        map.put(key, value);
		}

	}
	
	 
		public static LinkedHashMap<String, String> getJSonStringElemsLinkedHashMap(JSONObject jobj){
			LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
	         
			Iterator<String> keys = jobj.keys();
			
			while(keys.hasNext()){
		        String key = keys.next();
		        String value=String.valueOf(jobj.get(key));
		        map.put(key, value);
			}   
			   return map;
		}
		
		@SuppressWarnings("unchecked")
		public static <V> Map<String, V> getJSONObjectElementsToMap(JSONObject obj, Map<String, V> container, Class<?> valueclass) throws ClassCastException{
			
            Iterator<String> keys = obj.keys();
			
			while(keys.hasNext()){
		         String key = keys.next();
		         
		         Object value = obj.get(key);
		   
		         if(!valueclass.isAssignableFrom(value.getClass())){
		        	 if(valueclass.equals(Double.class)){
		        		 if(value instanceof Integer)
		        			 value=((Integer) value).doubleValue();
		        		 else if(value instanceof String)
		        			 value=Double.valueOf((String) value);
		        		 else
		        			 throw new ClassCastException("It was not possible to perform the cast of "+value+" to "+ valueclass);
		        	 }
		        	 else if(valueclass.equals(Integer.class)){
		        		 if(value instanceof Double)
		        			 value=((Double) value).intValue();
		        		 else if(value instanceof String)
		        			 value=Integer.valueOf((String) value);
		        		 else
		        			 throw new ClassCastException("It was not possible to perform the cast of "+value+" to "+ valueclass);
		        	 }
		        	 else if(valueclass.equals(Boolean.class)){
		        		 if(value instanceof String){
		        			 try {
								value=Boolean.getBoolean((String) value);
							  } catch (Exception e) {
								  throw new ClassCastException("It was not possible to perform the cast of "+value+" to "+ valueclass);
							 }
		        		 }
		        		 else
		        			 throw new ClassCastException("It was not possible to perform the cast of "+value+" to "+ valueclass);
		        	 }
		         }
		        	

		         if(value instanceof String){
		        	 if(!((String)value).isEmpty())
		        		 container.put(key, (V) value);
		         }
		         else
		          container.put(key, (V) value);
			}   	
			return container;	
		}
		
        public static Map<String, Object> getJSONObjectElementsToMap(JSONObject obj){
			Map<String, Object> container=new HashMap<>();
           
			Iterator<String> keys = obj.keys();
			
			while(keys.hasNext()){
		         String key = keys.next();
		         Object value=obj.get(key);
		         container.put(key, value);
			}   	
			return container;	
		}
		
		public static LinkedHashMap<String, Integer> getJSonStringToIntegerElemsLinkedHashMap(JSONObject jobj){
			LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
	         
			Iterator<String> keys = jobj.keys();
			
			while(keys.hasNext()){
		        String key = keys.next();
		        Integer value=jobj.getInt(key);
		        map.put(key, value);
			}
			   
			   return map;
		}
		
		
		
		
		public static HashMap<String, String> getJSonStringElemsHashMap(JSONObject jobj){
			return new HashMap<>(getJSonStringElemsLinkedHashMap(jobj));
		}
		
		
		public static LinkedHashMap<String,JSONObject> getJSonObjectsElemsLinkedHashMap(JSONObject jobj){
			LinkedHashMap<String, JSONObject> res =new LinkedHashMap<String, JSONObject>();
			
            Iterator<String> keys = jobj.keys();
			
			while(keys.hasNext()){
		        String key = keys.next();
		        JSONObject value=jobj.getJSONObject(key);
		        res.put(key, value);
			}
			return res;
			
		}
	
       public static HashMap<String,JSONObject> getJSonObjectsElemsHashMap(JSONObject jobj){
    	   return new HashMap<>(getJSonObjectsElemsLinkedHashMap(jobj));
       }
	
       
       public static JSONObject assembleJSONObjectFromStringLinkedHashMap(LinkedHashMap<String, String> stringmap){
    	   JSONObject objout=new JSONObject();
    	   for (String key : stringmap.keySet()) {
			   objout.put(key, stringmap.get(key));
    	   }
    	   return objout;
    	   
       }
       
       public static JSONObject assembleJSONObjectFromKStringVObjectLinkedHashMap(LinkedHashMap<String, Object> map){
    	   JSONObject objout=new JSONObject();
    	   for (String key : map.keySet()) {
    		   objout.put(key, map.get(key));
    	   }
    	   return objout;
    	   
       }
       
       
       public static <E> JSONArray assembleJSONArrayFromList(List<E> list){
    	   JSONArray array=new JSONArray();
    	   for (int i = 0; i < list.size(); i++) {
			   array.put(list.get(i));
		    }
    	   return array;
       }
       
       public static JSONArray assembleJSONArrayFromdoubleArray(double[] array){
    	   JSONArray objarray=new JSONArray();
    	   for (int i = 0; i < array.length; i++) {
			   objarray.put(array[i]);
		   }
    	   return objarray;
       }
       
       public static double[] recoverdoubleArrayFromJSONArray(JSONArray objarray){
    	   
    	   double[] array=new double[objarray.length()];
    	   for (int i = 0; i < objarray.length(); i++) {
			    array[i]=objarray.getDouble(i);
		    }
    	   return array;
    	   
       }
       
       public static JSONArray assembleJSONArrayFromintArray(int[] array){
    	   JSONArray objarray=new JSONArray();
    	   for (int i = 0; i < array.length; i++) {
			   objarray.put(array[i]);
		   }
    	   return objarray;
       }
       
       public static JSONArray assembleJSONArrayFromStringArray(String[] array){
    	   JSONArray objarray=new JSONArray();
    	   for (int i = 0; i < array.length; i++) {
			   objarray.put(array[i]);
		   }
    	   return objarray;
       }
       
       public static JSONArray assembleJSONArrayFromlongArray(long[] array){
    	   JSONArray objarray=new JSONArray();
    	   for (int i = 0; i < array.length; i++) {
			   objarray.put(array[i]);
		   }
    	   return objarray;
       }
       
       public static final String ROWNAMES="rows names";
       public static final String COLUMNNAMES="column names";
       public static final String DOUBLEMATRIX="double matrix";
       public static final String NUMBERROWS="number rows";
       public static final String NUMBERCOLUMNS="number columns";
       public static JSONObject assembleJSONOjectForMatrixOfdoubles(double[][] matrix, ArrayList<String> rownames, ArrayList<String> colnames){
    	   
    	   JSONObject objmatrix=new JSONObject();
    	   
    	   if(rownames!=null)
    		   objmatrix.put(ROWNAMES, assembleJSONArrayFromList(rownames));
    	   if(colnames!=null)
    		   objmatrix.put(COLUMNNAMES, assembleJSONArrayFromList(colnames));
    	   
    	   JSONObject matrixout=new JSONObject();
    	   
    	   matrixout.put(NUMBERROWS, matrix.length);
    	   matrixout.put(NUMBERCOLUMNS, matrix[0].length);
    	   
    	   String matrixstring=MTUStringUtils.convertDoubleMatrixToString(matrix, "\t");
    	   
    	   /*for (int i = 0; i < matrix.length; i++) {
			   double[] row=matrix[i];
    		   matrixout.put(String.valueOf(i), assembleJSONArrayFromdoubleArray(row)); 
		    }*/
    	   objmatrix.put(DOUBLEMATRIX, matrixstring);
    	   
    	   return objmatrix;
       }
       
      /* public static Triplet<ArrayList<String>, ArrayList<String>, double[][]> getMatrixElementsFromJSONObject(JSONObject obj){
    	   
    	   ArrayList<String> rownames=null;
    	   if(obj.has(ROWNAMES))
    		   rownames=getStringElemsFromJSONArray(obj.getJSONArray(ROWNAMES));
    	   ArrayList<String> columnnames=null;
    	   if(obj.has(COLUMNNAMES))
    		   columnnames=getStringElemsFromJSONArray(obj.getJSONArray(COLUMNNAMES));
    	   
    	   JSONObject matobj=obj.getJSONObject(DOUBLEMATRIX);
    	   
    	   int nrows=matobj.getInt(NUMBERROWS);
    	   int ncols=matobj.getInt(NUMBERCOLUMNS);
    	   
    	   double [][] outmatrix=new double[nrows][ncols];
    	   
    	   TreeSet<Integer> orderedkeys=new TreeSet<>();
    	   for (String key : matobj.keySet()) {
			    orderedkeys.add(Integer.valueOf(key));
    	   }
    	   
    	   for (Integer index : orderedkeys) {
			   JSONArray row=matobj.getJSONArray(String.valueOf(index));
			   outmatrix[index]=recoverdoubleArrayFromJSONArray(row);
    	   }
    	   
    	   return new Triplet<ArrayList<String>, ArrayList<String>, double[][]>(rownames, columnnames, outmatrix);
       }*/
       public static Triplet<ArrayList<String>, ArrayList<String>, double[][]> getMatrixElementsFromJSONObject(JSONObject obj){
    	   ArrayList<String> rownames=null;
    	   if(obj.has(ROWNAMES))
    		   rownames=getStringElemsFromJSONArray(obj.getJSONArray(ROWNAMES));
    	   ArrayList<String> columnnames=null;
    	   if(obj.has(COLUMNNAMES))
    		   columnnames=getStringElemsFromJSONArray(obj.getJSONArray(COLUMNNAMES));
    	   
    	  // JSONObject matobj=obj.getJSONObject(DOUBLEMATRIX);
    	   String matrixstring=obj.getString(DOUBLEMATRIX);
    	   double[][] matrix=MTUMatrixUtils.convertStringFormatMatrixToMatrixofdoubles(matrixstring, "\t");
    	   return new Triplet<ArrayList<String>, ArrayList<String>, double[][]>(rownames, columnnames, matrix);
    	   
       }
       
       
       
       public static double[][] getMatrixofdoublesFromJSONObject(JSONObject obj){
    	   Triplet<ArrayList<String>, ArrayList<String>, double[][]> resobj=getMatrixElementsFromJSONObject(obj);
    	   return resobj.getValue2();
       }

}
