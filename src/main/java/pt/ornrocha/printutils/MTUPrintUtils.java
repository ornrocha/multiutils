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
package pt.ornrocha.printutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;

import pt.ornrocha.logutils.MTULogUtils;


/**
 * The Class MTUPrintUtils.
 */
public class MTUPrintUtils {
	

	
	 /**
 	 * Gets the map to string.
 	 *
 	 * @param map the map
 	 * @param keyvaluedelimiter the keyvaluedelimiter
 	 * @param delimiter the delimiter
 	 * @return the map to string
 	 */
 	public static String getMapToString(final Map<?,?> map,final String keyvaluedelimiter, final String delimiter){
		 StringBuilder str =new StringBuilder();
		 
		/* if(map instanceof IndexedHashMap){
			 IndexedHashMap<?, ?> mapcast=(IndexedHashMap<?,?>)map;
			 
			 for (int i = 0; i < mapcast.size(); i++) {
				 str.append(String.valueOf(mapcast.getKeyAt(i))+keyvaluedelimiter+String.valueOf(mapcast.getValueAt(i))+delimiter);
			  }
			 
		 }
		 else*/
			 for (Map.Entry<?, ?> mapelems: map.entrySet()) {
				 str.append(String.valueOf(mapelems.getKey())+keyvaluedelimiter+String.valueOf(mapelems.getValue())+delimiter);
			 }
		 
		 return str.toString(); 
	 }
	
	
	 /**
 	 * Prints the map.
 	 *
 	 * @param map the map
 	 */
 	public static void printMap(final Map<?,?> map){
		 System.out.println(getMapToString(map, "-->","\n"));
	 }
	
	 
	 /**
 	 * Prints the map.
 	 *
 	 * @param map the map
 	 * @param delimiter the delimiter
 	 */
 	public static void printMap(final Map<?,?> map, final String delimiter){
		 System.out.println(getMapToString(map, delimiter,"\n"));
	 }
	
	
	 
	 /**
 	 * Gets the sets the to string.
 	 *
 	 * @param <E> the element type
 	 * @param list the list
 	 * @param delimiter the delimiter
 	 * @return the sets the to string
 	 */
 	public static <E> String getSetToString(final Set<E> list, final String delimiter){
		 StringBuilder str =new StringBuilder();
		 for (E elem : list) {
			 str.append(String.valueOf(elem)+delimiter);
		 }
		 return str.toString();
	 }
	 
	 /**
 	 * Prints the set.
 	 *
 	 * @param <E> the element type
 	 * @param list the list
 	 */
 	public static <E> void printSet(final Set<E> list){
		System.out.println(getSetToString(list, "\n"));
	 }
	 
	 
	 /**
 	 * Prints the set.
 	 *
 	 * @param <E> the element type
 	 * @param list the list
 	 * @param delimiter the delimiter
 	 */
 	public static <E> void printSet(final Set<E> list, final String delimiter){
			System.out.println(getSetToString(list, delimiter));
		 }
	 
	 
	 /**
 	 * Gets the list to string.
 	 *
 	 * @param <E> the element type
 	 * @param list the list
 	 * @param delimiter the delimiter
 	 * @return the list to string
 	 */
 	public static <E> String getListToString(final List<E> list, final String delimiter){
		 StringBuilder str =new StringBuilder();

		 for (E elem : list) {
			 str.append(String.valueOf(elem)+delimiter);
		 }
		 
		 return str.toString();
	 }
	 
	 
	 /**
 	 * Printlist.
 	 *
 	 * @param <E> the element type
 	 * @param list the list
 	 */
 	public static <E> void printlist(List<E> list){
		 System.out.println(getListToString(list, "\n"));
	 }
	 
	 /**
 	 * Printlist.
 	 *
 	 * @param <E> the element type
 	 * @param list the list
 	 * @param delimiter the delimiter
 	 */
 	public static <E> void printlist(List<E> list, String delimiter){
		 System.out.println(getListToString(list, delimiter));
	 }
	 
	 
	 /**
 	 * Gets the array object to string.
 	 *
 	 * @param <E> the element type
 	 * @param input the input
 	 * @param delimiter the delimiter
 	 * @return the array object to string
 	 */
 	public static <E> String getArrayObjectToString(final E[] input, String delimiter){
		 StringBuilder str=new StringBuilder();
		 
		 Class<?> clazz=input.getClass().getComponentType();
			 
		if(clazz.equals(Double.class) || 
				clazz.equals(String.class) || 
				clazz.equals(Integer.class) || 
				clazz.equals(Boolean.class) || 
				clazz.equals(Float.class) || 
				clazz.equals(Character.class) || 
				clazz.equals(Byte.class) || 
				clazz.equals(Long.class) || 
				clazz.equals(Short.class)){
		
			 for (E e : input) {
			    str.append(e+delimiter);	
			 }
			 
		 }
		 else if(clazz.isAssignableFrom(Map.class)){
			 
			 for (E e : input) {
			   str.append(getMapToString((Map<?, ?>) e, " -> ",delimiter));	
			}
			
		 }
		 else if(clazz.isAssignableFrom(List.class)){
			 for (E e : input) {
				str.append(getListToString((List<E>) e,",")+delimiter);
			}
		
		 }
		 else if(clazz.isAssignableFrom(Set.class)){
			 for (E e : input) {
				str.append(getSetToString((Set<E>) e, ",")+delimiter);
			}
		 }
		 else{
			 for (E e : input) {
				str.append(e.toString()+delimiter);
			 }
		 }
		 
		 return str.toString();
	 }
	 
	 
	 /**
 	 * Prints the array oject.
 	 *
 	 * @param <E> the element type
 	 * @param input the input
 	 */
 	public static <E> void printArrayOject(final E[] input){
		 System.out.println(getArrayObjectToString(input, "\n"));
	 }
	 
	 
	 /**
 	 * Prints the array oject.
 	 *
 	 * @param <E> the element type
 	 * @param input the input
 	 * @param delimiter the delimiter
 	 */
 	public static <E> void printArrayOject(final E[] input, String delimiter){
		 System.out.println(getArrayObjectToString(input, delimiter));
	 }
	 
	 
	 /**
 	 * Gets the int array to string.
 	 *
 	 * @param array the array
 	 * @param delimiter the delimiter
 	 * @return the int array to string
 	 */
 	public static String getintArrayToString(int[] array, String delimiter){
		 return getArrayObjectToString(ArrayUtils.toObject(array), delimiter);
	 }
	 
	 /**
 	 * Prints the arrayof ints.
 	 *
 	 * @param array the array
 	 */
 	public static void printArrayofInts(int[] array){
		 System.out.println(getintArrayToString(array, "\n"));
	 }
	 
	 /**
 	 * Prints the arrayof ints.
 	 *
 	 * @param array the array
 	 * @param delimiter the delimiter
 	 */
 	public static void printArrayofInts(int[] array, String delimiter){
		 System.out.println(getintArrayToString(array, delimiter));
	    }
	 
	 
	 /**
 	 * Gets the double array to string.
 	 *
 	 * @param array the array
 	 * @param delimiter the delimiter
 	 * @return the double array to string
 	 */
 	public static String getdoubleArrayToString(double[] array, String delimiter){
		 return getArrayObjectToString(ArrayUtils.toObject(array), delimiter);
	 }
	 
	 
	 /**
 	 * Prints the arrayof doubles.
 	 *
 	 * @param array the array
 	 */
 	public static void printArrayofDoubles(double[] array){
		 System.out.println(getdoubleArrayToString(array, "\n"));
	    }
	 
	 /**
 	 * Prints the arrayof doubles.
 	 *
 	 * @param array the array
 	 * @param delimiter the delimiter
 	 */
 	public static void printArrayofDoubles(double[] array,String delimiter){
		 System.out.println(getdoubleArrayToString(array, delimiter));
	    }
	 
	 /**
 	 * Gets the byte array to string.
 	 *
 	 * @param array the array
 	 * @param delimiter the delimiter
 	 * @return the byte array to string
 	 */
 	public static String getbyteArrayToString(byte[] array, String delimiter){
		 return getArrayObjectToString(ArrayUtils.toObject(array), delimiter);
	 }
	 
	 /**
 	 * Prints the arrayof bytes.
 	 *
 	 * @param array the array
 	 */
 	public static void printArrayofBytes(byte[] array){
		 System.out.println(getbyteArrayToString(array, "\n"));
	    }
	 
	 /**
 	 * Prints the arrayof bytes.
 	 *
 	 * @param array the array
 	 * @param delimiter the delimiter
 	 */
 	public static void printArrayofBytes(byte[] array, String delimiter){
		 System.out.println(getbyteArrayToString(array, delimiter));
	    }
	 
	 /**
 	 * Printlist with logger.
 	 *
 	 * @param <E> the element type
 	 * @param list the list
 	 * @param clazz the clazz
 	 */
 	public static <E> void printlistWithLogger(List<E> list, Class<?> clazz){
		 for (E elem : list) {
			MTULogUtils.addDebugMsgToClass(clazz.getClass(), String.valueOf(elem));
		}
	 }
	 

	/* public static void printIndexedHashMap(IndexedHashMap<?,?> map, String delim){
		for (int i = 0; i < map.size(); i++) {
			System.out.println(map.getKeyAt(i)+delim+map.getValueAt(i));
		} 
	 }*/
	
	 
	 /**
	 * Prints the string matrix.
	 *
	 * @param m the m
	 */
	public static void printStringMatrix(String[][] m){
		 
		 for (int i = 0; i < m.length; i++) {
			 
			 for (int j = 0; j < m[0].length; j++) {
				System.out.print(m[i][j]+"\t");
			 }
			 System.out.println();
		}
	 }
	
    /**
     * Prints the double matrix.
     *
     * @param m the m
     */
    public static void printDoubleMatrix(double[][] m){
		 
		 for (int i = 0; i < m.length; i++) {
			 
			 for (int j = 0; j < m[0].length; j++) {
				System.out.print(m[i][j]+"\t");
			 }
			 System.out.println();
		}
	 }
    
    /**
     * Prints the integer matrix.
     *
     * @param m the m
     */
    public static void printIntegerMatrix(int[][] m){
		 
		 for (int i = 0; i < m.length; i++) {
			 
			 for (int j = 0; j < m[0].length; j++) {
				System.out.print(m[i][j]+"\t");
			 }
			 System.out.println();
		}
	 }
    

    

    
    /**
     * Prints the list of boolean values.
     *
     * @param list the list
     * @param delimiter the delimiter
     */
    public static void printListOfBooleanValues(ArrayList<Boolean> list, String delimiter){
    	for (Boolean t : list) {
    		if(delimiter!=null){
    			System.out.print(t+delimiter);	
    		}
    		else
			  System.out.println(t);
		}
    }
    
    
    
    /**
     * Prints the arrayof strings.
     *
     * @param array the array
     */
    public static void printArrayofStrings(String[] array){
    	for (String t : array) {
			System.out.print(t+"\t");
		}
    }
    

    
    /**
     * Gets the object to string.
     *
     * @param object the object
     * @param delimiter the delimiter
     * @return the object to string
     */
    public static  String getObjectToString(final Object object, String delimiter){
    	
    	if(delimiter==null)
    		delimiter="\n";
    	
    	if(object==null)
    		return null;
    	else if(object instanceof Map<?,?>){
    		return getMapToString((Map<?, ?>) object, " -> ",delimiter);
    	}
    	else if(object instanceof List<?>)
    		return getListToString((List<?>) object, delimiter);
    	else if(object instanceof Set<?>)
    		return getSetToString((Set<?>) object, delimiter);
    	else if(object instanceof int[])
    		return getArrayObjectToString(ArrayUtils.toObject((int[])object), delimiter);	
    	else if(object instanceof double[])
    		return getArrayObjectToString(ArrayUtils.toObject((double[])object), delimiter);	
    	else if(object instanceof boolean[])
    		return getArrayObjectToString(ArrayUtils.toObject((boolean[])object), delimiter);
    	else if(object instanceof char[])
    		return getArrayObjectToString(ArrayUtils.toObject((char[])object), delimiter);
    	else if(object instanceof float[])
    		return getArrayObjectToString(ArrayUtils.toObject((float[])object), delimiter);
    	else if(object instanceof long[])
    		return getArrayObjectToString(ArrayUtils.toObject((long[])object), delimiter);
    	else if(object instanceof short[])
    		return getArrayObjectToString(ArrayUtils.toObject((short[])object), delimiter);
    	else if(object instanceof Object[])
    		return getArrayObjectToString((Object[]) object, delimiter);
    	else{
    		String msg=object.toString();
    		if(msg!=null && !msg.isEmpty())
    			return msg;
    		else
    			return String.valueOf(object);
    	}
    		
    }
    
    /**
     * Gets the object to string.
     *
     * @param object the object
     * @return the object to string
     */
    public static  String getObjectToString(final Object object){
    	return getObjectToString(object, null);
    }
    
    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args){
    	
    	HashMap<String, String> test=new HashMap<>();
    	test.put("A", "a");
    	test.put("B", "b");
    	test.put("C", "c");
    	System.out.println(getObjectToString(test,"\n"));
    	
    }
    

}
