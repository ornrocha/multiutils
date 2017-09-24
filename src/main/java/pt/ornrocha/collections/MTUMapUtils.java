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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;



public class MTUMapUtils {
	
	
	
	public static Map<String, Double> sortMapofDoubleValues(Map<String, Double> unsortMap, final boolean ascendingorder)
    {

        List<Entry<String, Double>> list = new LinkedList<Entry<String, Double>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Double>>()
        {
            public int compare(Entry<String, Double> o1,
                    Entry<String, Double> o2)
            {
                if (ascendingorder)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
        for (Entry<String, Double> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
	
	
	public static <K, V extends Comparable<? super V>> Map<K, V> sortMapByValues(Map<K, V> unsortedmap, final boolean ascendingorder){

        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(unsortedmap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Map.Entry<K, V>>()
        {
            public int compare(Map.Entry<K, V> o1,
            		Map.Entry<K, V> o2)
            {
                if (ascendingorder)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<K, V> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
	
	public static <K extends Comparable<? super K>, V> Map<K, V> sortMapByKeys(Map<K, V> unsortedmap, final boolean ascendingorder){

        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(unsortedmap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Map.Entry<K, V>>()
        {
            public int compare(Map.Entry<K, V> o1,
            		Map.Entry<K, V> o2)
            {
                if (ascendingorder)
                {
                    return o1.getKey().compareTo(o2.getKey());
                }
                else
                {
                    return o2.getKey().compareTo(o1.getKey());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<K, V> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
	
	

	
	public static <K, V> K getKeyAssociatedToValueInMap(Map<K,V> map, Object value){
		
		for (Map.Entry<K, V> element : map.entrySet()) {
		    if(element.getValue().equals(value))
		    	return element.getKey();
		}
		return null;
	}
	
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map invertStringMap(Map<String,String> inmap) throws InstantiationException, IllegalAccessException{
		Map res=inmap.getClass().newInstance();
		
		for (String key : inmap.keySet()) {
			res.put(inmap.get(key), key);
		}
		
		return res;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <K, V> Map invertMap(Map<K,V> maptoinvert) throws InstantiationException, IllegalAccessException{
		
		Map inverted=maptoinvert.getClass().newInstance();
		
		for (Map.Entry<K, V> elements : maptoinvert.entrySet()) {
			inverted.put(elements.getValue(), elements.getKey());
		}
		
		return inverted;
	}
	
	
	
	
	
	
	// Deep Clone Map
	////////////////////////////////////////// From: http://stackoverflow.com/questions/11296490/assigning-hashmap-to-hashmap
     @SuppressWarnings("unchecked")
	public static <X> X deepClone(final X input) {
	        if (input == null) {
	            return input;
	        } else if (input instanceof Map<?, ?>) {
	            return (X) deepCloneMap((Map<?, ?>) input);
	        } else if (input instanceof Collection<?>) {
	            return (X) deepCloneCollection((Collection<?>) input);
	        } else if (input instanceof Object[]) {
	            return (X) deepCloneObjectArray((Object[]) input);
	        } else if (input.getClass().isArray()) {
	            return (X) clonePrimitiveArray((Object) input);
	        }

	        return input;
	    }

	 
	 private static Object clonePrimitiveArray(final Object input) {
	        final int length = Array.getLength(input);
	        final Object copy = Array.newInstance(input.getClass().getComponentType(), length);
	        // deep clone not necessary, primitives are immutable
	        System.arraycopy(input, 0, copy, 0, length);
	        return copy;
	    }

	    private static <E> E[] deepCloneObjectArray(final E[] input) {
	        @SuppressWarnings("unchecked")
			final E[] clone = (E[]) Array.newInstance(input.getClass().getComponentType(), input.length);
	        for (int i = 0; i < input.length; i++) {
	            clone[i] = deepClone(input[i]);
	        }

	        return clone;
	    }

	    private static <E> Collection<E> deepCloneCollection(final Collection<E> input) {
	        Collection<E> clone;
	        // this is of course far from comprehensive. extend this as needed
	        if (input instanceof LinkedList<?>) {
	            clone = new LinkedList<E>();
	        } else if (input instanceof SortedSet<?>) {
	            clone = new TreeSet<E>();
	        } else if (input instanceof Set) {
	            clone = new HashSet<E>();
	        } else {
	            clone = new ArrayList<E>();
	        }

	        for (E item : input) {
	            clone.add(deepClone(item));
	        }

	        return clone;
	    }

	    private static <K, V> Map<K, V> deepCloneMap(final Map<K, V> map) {
	        Map<K, V> clone;
	        // this is of course far from comprehensive. extend this as needed
	        if (map instanceof LinkedHashMap<?, ?>) {
	            clone = new LinkedHashMap<K, V>();
	        } else if (map instanceof TreeMap<?, ?>) {
	            clone = new TreeMap<K, V>();
	        } else {
	            clone = new HashMap<K, V>();
	        }

	        for (Entry<K, V> entry : map.entrySet()) {
	            clone.put(deepClone(entry.getKey()), deepClone(entry.getValue()));
	        }

	        return clone;
	    }
      /////////////////////////////////////////////////////////////////////////////////////////////7
}
