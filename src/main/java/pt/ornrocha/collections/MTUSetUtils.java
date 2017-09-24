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

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class MTUSetUtils {
	

	public static <E> HashSet<E> cloneHashSet(HashSet<E> elems){
		
		HashSet<E> clone = new HashSet<>(elems.size());
		
		for (E e : elems) {
			E copy=null;
			Class<?> eclass=e.getClass();
			try {
				Method m = eclass.getMethod("copy");
				if(m==null)
					m=eclass.getMethod("clone");
				if(m!=null)
					copy=(E) m.invoke(m);
			} catch (Exception e2) {
				// TODO: handle exception
			}
			if(copy!=null)
				clone.add(copy);
			else
				clone.add(e);
		}
		return clone;
	
	}
	

	public static <T> Set<T> convertArrayToSet(T[] a){
		
		Set<T> res=new HashSet<T>();
		for (int i = 0; i < a.length; i++) {
			res.add(a[i]);
		}
		return res;
	}

}
