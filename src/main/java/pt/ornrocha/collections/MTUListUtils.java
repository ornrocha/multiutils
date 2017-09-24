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
package pt.ornrocha.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MTUListUtils {
	
	
	public static <T> List<List<T>> splitByNumberElements(List<T> list, final int nelems) {
	    List<List<T>> parts = new ArrayList<List<T>>();
	    final int N = list.size();
	    for (int i = 0; i < N; i += nelems) {
	    	if((i+nelems)>N)
	    		parts.get(parts.size()-1).addAll((Collection<? extends T>) list.subList(i, N));
	    	else
	    		parts.add(new ArrayList<T>(list.subList(i,(i+nelems))));
	     
	    }
	    return parts;
	}
	
	public static <T> List<List<T>> splitByNumberGroups(List<T> list, final int numbergroups){
		final int ng=list.size()/numbergroups;
		return splitByNumberElements(list, ng);
	}
	
	
	public static ArrayList<Integer> getIntegerElementsFromStringArrayNumber(String arraynumber, String delimiter){
		
		ArrayList<Integer> res=new ArrayList<>();
		
		String[] elems=arraynumber.split(delimiter);
		for (int i = 0; i < elems.length; i++) {
			try {
				int value=Integer.parseInt(elems[i]);
				res.add(value);
			} catch (Exception e) {
				throw new NumberFormatException();
			}
		}
		return res;
	}
	
	

	public static ArrayList<String> getElementsFromString(String line, String delimiter){
		
		ArrayList<String> res=new ArrayList<>();
		
		String[] elems=line.split(delimiter);
		for (int i = 0; i < elems.length; i++) {
			res.add(elems[i]);
		}
		return res;
	}
	
	
	public static ArrayList<Integer> getListAsShuffleIndexes(int numberelems){
		
		ArrayList<Integer> list=new ArrayList<>();
		for (int i = 0; i < numberelems; i++) {
			list.add(i);
		}
		
		Collections.shuffle(list);
		return list;
	}
	
	
	public static ArrayList<String> getStringsFromIndexes(ArrayList<String> list, ArrayList<Integer> indexes){
		
		if(indexes!=null) {
			ArrayList<String> res=new ArrayList<>();
			int listsize=list.size();
			for (int i = 0; i < indexes.size(); i++) {
				int index=indexes.get(i);
				if(index<=listsize)
					res.add(list.get(index));
			}
			return res;
		}
		return null;
	}
	
	public static ArrayList<Integer> getIntegerList(int size){
		ArrayList<Integer> res=new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			res.add(i);
		}
		return res;
	}
	
	
	public static void main(String[] args){
		
		ArrayList<Integer> l=getListAsShuffleIndexes(20);
		System.out.println(l);
	}

}
