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

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

public class MTUGuavaUtils {
	
	
	public static String getConcatStringsValuesOfMultiMap(Multimap<String, String> map, String key){
		Collection<String> values = map.get(key);
		String res ="";
		for (String string : values) {
			res+=string+";";
		}
		return res;
	}
	
	public static LinkedHashMap<String, HashSet<String>> getConvertStringMultimapToLinkedHashMap(Multimap<String,String> multimap){
		
		 LinkedHashMap<String, HashSet<String>> res =new LinkedHashMap<>();
		  
		 Set<String> keys= multimap.keySet();
		 
		 for (String k : keys) {
		      res.put(k, new HashSet<>(multimap.get(k)));	
		 }
		 
		 return res;
		
	}
	
	@SuppressWarnings("unchecked")
	public static <K, V> Multimap<K,V> invertMultimap(Multimap<K, V> invertmap, Multimap newmap){
		return Multimaps.invertFrom(invertmap,newmap);
	}
	

	public static Multimap<String, String> invertStringMultimap(Multimap<String, String> toinvert){
		return Multimaps.invertFrom(toinvert, ArrayListMultimap.<String, String>create());
	}
	

}
