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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;
import org.javatuples.Pair;

import com.rits.cloning.Cloner;

public class MTUCollectionsUtils {

	public static int[] convertIntegerListToArray(List<Integer> list){
		return ArrayUtils.toPrimitive(list.toArray(new Integer[list.size()]));
	}

	
	public static ArrayList<Integer> convertArrayIntToArrayList(int[] array){
		if(array.length>0){
			ArrayList<Integer> list = new ArrayList<>(array.length);
			for (int i = 0; i < array.length; i++) {
				list.add(array[i]);
			}
			return list;
		}
		else
			throw new IndexOutOfBoundsException("Input Array have 0 elements");
	}
	
	
	public static boolean arrayHaveIntValue(int[] values, int value){
		
		for (int i = 0; i < values.length; i++) {
			if(value==values[i])
				return true;
		}
		return false;
	}
	
	public static String getStringFromList(List<?> list,String delimiter){
		int n=0;
		String str="";
		
		for (Object obj : list) {
			str+=String.valueOf(obj);
			if(n<list.size()-1)
				str+=delimiter;
			n++;
		}
		return str;
	}
	
	public static String getStringFromSet(Set<?> set,String delimiter){
		int n=0;
		String str="";
		
		for (Object obj : set) {
			str+=String.valueOf(obj);
			if(n<set.size()-1)
				str+=delimiter;
			n++;
		}
		return str;
	}
	
	public static boolean tupleContainPair(int key,int value, Pair<Integer, Integer> pair){
		int k=pair.getValue0();
		int v=pair.getValue1();
		if(k==key && v==value)
			return true;
		else
			return false;
	}
	
	

	
	public static Object deepCloneObject(Object toclone) throws Exception{
		Cloner cloner =new Cloner();
		return cloner.deepClone(toclone);
	}
	

	
	public static byte[] convertBooleanListTobyteArray(ArrayList<Boolean> boolstate){
		byte[] state=new byte[boolstate.size()];
		
		for (int i = 0; i < boolstate.size(); i++) {
			if(boolstate.get(i))
				state[i]=1;
			else
				state[i]=0;
		}
		return state;
	}
	
	public static ArrayList<Boolean> convertbyteArrayToBooleanList(byte[] bytestate){
		ArrayList<Boolean> res=new ArrayList<>(bytestate.length);
		
		for (int i = 0; i < bytestate.length; i++) {
			int value=(int)bytestate[i];
			if(value<=0)
				res.add(false);
			else
				res.add(true);
		}
		return res;
	}
	
	
 
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException{
		LinkedHashMap<String, Double> orig = new LinkedHashMap<>();
		orig.put("A", 3.0);
		orig.put("B", 5.0);
		orig.put("C", 2.0);
		
		LinkedHashMap<String, Double> clone =MTUMapUtils.deepClone(orig);
		
		orig.put("A", 6.0);
		orig.put("C", 6.0);
		System.out.println(orig);
		
		System.out.println(clone);
		
		
	}
	
	
	

}
