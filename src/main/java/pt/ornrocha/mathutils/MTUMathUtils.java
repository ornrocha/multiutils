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
package pt.ornrocha.mathutils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import pt.ornrocha.collections.MTUSetUtils;

public class MTUMathUtils {
	
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
 
 
 
 
	public static <T>  List<T> randomSample(List<T> original, int n) {
	    List<T> result = new ArrayList<T>(n);
	    for (int i = 0; i < original.size(); i++) {
	        if (result.size() == n)
	            return result;
	        if ((n - result.size()) >= (original.size() - i)) {
	            result.add(original.get(i));
	        } else if (Math.random() < ((double)n / original.size())) {
	            result.add(original.get(i));
	        }
	    }

	    return result;
	}


	

 

 
	public static <E> ArrayList<E> createRandomList(List<E> list, int nelems){
	    ArrayList<E> copy=new ArrayList<>(list);
	    Collections.shuffle(copy);
	    return  new ArrayList<>(copy.subList(0, nelems));
    }
 
 
  public static ArrayList<Integer> generateSortedRandomIntegerListNumber(int samplesize, int maxnumber){
	  Random rng = new Random();
	  Set<Integer> generated = new LinkedHashSet<Integer>();
		while (generated.size() < samplesize)
		{
		    Integer next = rng.nextInt(maxnumber) + 1;
		    generated.add(next);
		}
		
		TreeSet<Integer> sorted = new TreeSet<Integer>();
		sorted.addAll(generated);
		
		return new ArrayList<>(sorted);
  }
  
  
  public static ArrayList<Integer> generateRandomIntegerSampleList(int sizesample, int sizeset, boolean sort){
		
		ArrayList<Integer> res=new ArrayList<>(sizesample);
		Integer[] arr = new Integer[sizeset];
		
		for (int i = 0; i < arr.length; i++) {
	        arr[i] = i;
	    }
		
		ArrayList<Integer> tosample=new ArrayList<>(Arrays.asList(arr));
		Collections.shuffle(tosample);
		
		for (int i = 0; i < sizesample; i++) {
			res.add(tosample.get(i));
		}
		
		if(sort)
			Collections.sort(res);
		
		return res;
	}
  
  
 
  public static ArrayList<Integer> generateRandomIntegerSampleFromListofIntegers(int samplesize, List<Integer> list, List<Integer> listtoexclude, boolean sort){
	  
	  ArrayList<Integer> res=new ArrayList<>(samplesize);
	  
	  ArrayList<Integer> filtered=null;
	  
	  if(listtoexclude!=null && listtoexclude.size()>0) {
		  filtered=new ArrayList<>();
		  
		  for (int i = 0; i < list.size(); i++) {
			  if(!listtoexclude.contains(list.get(i)))
				  filtered.add(list.get(i));
		  } 
	  }
	  else
		  filtered=new ArrayList<>(list);
	  
	  
	  Collections.shuffle(filtered);
	  
	  for (int i = 0; i < samplesize; i++) {
			res.add(filtered.get(i));
		}
	  
	  if(sort)
			Collections.sort(res);
	  return res;
  }
  
  public static ArrayList<Integer> generateRandomIntegerSampleFromListofIntegers(int samplesize, List<Integer> list,boolean sort){
	  return generateRandomIntegerSampleFromListofIntegers(samplesize, list, null, sort);
  }
  
  
  
  

	public static <T> Set<T> getUnion(T[] a, T[] b) {
		return getUnion(MTUSetUtils.convertArrayToSet(a),
				MTUSetUtils.convertArrayToSet(b));
		
	}
	
	public static <T> Set<T> getUnion(List<T> a, List<T> b){
		return getUnion(new HashSet<>(a), new HashSet<>(b));
	}
	
	
	public static <T> Set<T> getUnion(Set<T> a, Set<T> b) {
      Set<T> union = new HashSet<T>(a);
      union.addAll(b);
      return union;  
	}
	
	
	public static <T> Set<T> getIntersection(T[] a, T[] b){
		return getIntersection(MTUSetUtils.convertArrayToSet(a),
				        MTUSetUtils.convertArrayToSet(b));
	}
	
	public static <T> Set<T> getIntersection(List<T> a, List<T> b){
		return getIntersection(new HashSet<>(a), new HashSet<>(b));
	}
	
	
	public static <T> Set<T> getIntersection(Set<T> a, Set<T> b){
		 Set<T> intersection = new HashSet<T>(a);
		  intersection.retainAll(b);
		  return intersection;
	}
	
	
    // code from http://www.java2s.com/Code/Java/Collections-Data-Structure/LinearInterpolation.htm
	public static final double[] interpLinear(double[] x, double[] y, double[] xi) throws IllegalArgumentException {

        if (x.length != y.length) {
            throw new IllegalArgumentException("X and Y must be of the same length");
        }
        if (x.length == 1) {
            throw new IllegalArgumentException("X must contain more than one value");
        }
        double[] dx = new double[x.length - 1];
        double[] dy = new double[x.length - 1];
        double[] slope = new double[x.length - 1];
        double[] intercept = new double[x.length - 1];

        // Calculate the line equation (i.e. slope and intercept) between each point
        for (int i = 0; i < x.length - 1; i++) {
            dx[i] = x[i + 1] - x[i];
            if (dx[i] == 0) {
                throw new IllegalArgumentException("X must be montotonic. A duplicate " + "x-value was found");
            }
            if (dx[i] < 0) {
                throw new IllegalArgumentException("X must be sorted");
            }
            dy[i] = y[i + 1] - y[i];
            slope[i] = dy[i] / dx[i];
            intercept[i] = y[i] - x[i] * slope[i];
        }

        // Perform the interpolation here
        double[] yi = new double[xi.length];
        for (int i = 0; i < xi.length; i++) {
            if ((xi[i] > x[x.length - 1]) || (xi[i] < x[0])) {
                yi[i] = Double.NaN;
            }
            else {
                int loc = Arrays.binarySearch(x, xi[i]);
                if (loc < -1) {
                    loc = -loc - 2;
                    yi[i] = slope[loc] * xi[i] + intercept[loc];
                }
                else {
                    yi[i] = y[loc];
                }
            }
        }

        return yi;
    }
	
	
	public static double[] getArrayofIndexesWithConstantInterval(double interval, int arraylength){
		
		int sizevector=(int) Math.round(arraylength/interval);
		
		double[] res=new double[sizevector];
		res[0]=0;
		
		for (int i = 1; i < res.length; i++) {
			res[i]=res[i-1]+interval;
		}

		return res;
	}
	
	


}