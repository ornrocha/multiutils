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
package pt.ornrocha.arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.lang3.ArrayUtils;
import org.javatuples.Pair;


public class MTUArrayUtils {
	
	
	public static boolean arrayStringContains(String value, String[] array){
		for (String string : array) {
			if (string.contains(value)) {
				System.out.println(value);
				return true;
			}
		}
		return false;
	}
	
   public static boolean doubleArrayContainsNANValues(double[]array){
		
		for (int i = 0; i < array.length; i++) {
				if(Double.isNaN(array[i]))
					return true;
		}
		return false;
	}

 
   public static int countNANs(double[] vector){
		int count=0;
		for (int i = 0; i < vector.length; i++) {
			if(Double.isNaN(vector[i]))
				count++;
		}
		return count;
	}
   
   public static double[] makeSequenceDoubleIndexes(int beginvalue, int lenght){
	   double res[]=new double[lenght-beginvalue];
	   for (int i = beginvalue; i < lenght; i++) {
		 res[i]=(double)i;
	   }
	   return res;
   }
   
   public static double[] convertMatrixToArray(double[][] mat, boolean orderalongcolumn){
	   double[] res=new double[mat.length*mat[0].length];
	   
	   if(orderalongcolumn){
		   for (int i = 0; i < mat[0].length; i++) {
			   double[] col=getMatrixColumn(mat, i);
			   
			   for (int j = 0; j < col.length; j++) {
				   res[i*col.length+j]=col[j];
			   }
		   }
	   }
	   else{
		   for (int i = 0; i < mat.length; i++) {
			
			   double[] row=mat[i];
			   for (int j = 0; j < row.length; j++) {
				
				   res[i*row.length+j]=row[j];
				   
			   }
		   }
	   }
	   return res;
   }
   
   
   public static double[] reshapeAsRow(double[][] a) {
       double[] reshaped = new double[a.length * a[0].length];
       int ir = 0;
       for (int j = 0; j < a[0].length; j++) {
           for (int i = 0; i < a.length; i++) {
               reshaped[ir] = a[i][j];
               ir++;
           }
       }
       return reshaped;
   }
   
  
   public static double[] reshapeAsRow(Double[][] a) {
       double[] reshaped = new double[a.length * a[0].length];
       int ir = 0;
       for (int j = 0; j < a[0].length; j++) {
           for (int i = 0; i < a.length; i++) {
               reshaped[ir] = a[i][j];
               ir++;
           }
       }
       return reshaped;
   }
   
   public static int[] reshapeAsRow(int[][] a) {
       int[] reshaped = new int[a.length * a[0].length];
       int ir = 0;
       for (int j = 0; j < a[0].length; j++) {
           for (int i = 0; i < a.length; i++) {
               reshaped[ir] = a[i][j];
               ir++;
           }
       }
       return reshaped;
   }
   
  
   public static int[] reshapeAsRow(Integer[][] a) {
       int[] reshaped = new int[a.length * a[0].length];
       int ir = 0;
       for (int j = 0; j < a[0].length; j++) {
           for (int i = 0; i < a.length; i++) {
               reshaped[ir] = a[i][j];
               ir++;
           }
       }
       return reshaped;
   }
   
   
   public static String[] reshapeAsRow(String[][] a) {
       String[] reshaped = new String[a.length * a[0].length];
       int ir = 0;
       for (int j = 0; j < a[0].length; j++) {
           for (int i = 0; i < a.length; i++) {
               reshaped[ir] = a[i][j];
               ir++;
           }
       }
       return reshaped;
   }
   
   
   public static double[] getMatrixColumn(double[][] mat, int columnindex){
	   double[]res=new double[mat.length];
	   
	   for (int i = 0; i < res.length; i++) {
		     double[] row=mat[i];
		     res[i]=row[columnindex];
	   }
	   return res;
   }
   
   public static int[] getArrayIndexesWithBinaryValue(int[] binaryarray, boolean ontag){
		
		ArrayList<Integer> indexes=new ArrayList<>();
		for (int i = 0; i < binaryarray.length; i++) {
			int value=binaryarray[i];
			if(ontag && value==1)
				indexes.add(i);
			else if(value==0 && !ontag)
				indexes.add(i);
		}
		
		return org.apache.commons.lang3.ArrayUtils.toPrimitive(indexes.toArray(new Integer[indexes.size()]));
	}
	
   public static int[] getIntegerValuesMappedtoArrayOfIndexes(int[] originalarray, int[] arrayindex){

		int[] res=new int[arrayindex.length];
		
		for (int i = 0; i < arrayindex.length; i++) {
			res[i]=originalarray[arrayindex[i]];
		}
		return res;	
	}
	
    public static double[] getDoubleValuesMappedtoArrayOfIndexes(double[] originalarray, int[] arrayindex){
		
		double[] res=new double[arrayindex.length];
		
		for (int i = 0; i < arrayindex.length; i++) {
			res[i]=originalarray[arrayindex[i]];
		}
		return res;		
	}
    



    public static int[] indexSortDoubleArray(final double[] v, final boolean ascending) {
    	final Integer[] tmpres = new Integer[v.length];
    	for (int i = 0; i < v.length; i++) 
    		tmpres[i] = i;
    	
    	Arrays.sort(tmpres, new Comparator<Integer>() {
        
    		@Override
            public int compare(Integer o1, Integer o2) {
    			
    			if(ascending)
    				return Double.compare(v[o1],v[o2]);
    			else
    				return Double.compare(v[o2],v[o1]);
    			
    		}
    	});
    	
    	int[] res = new int[v.length];
    	for (int i = 0; i < v.length; i++) 
    		res[i] = tmpres[i];
    	return res;
    }
    
    
    public static Pair<double[], int[]> getSortedDoubleArrayAndIndexes(final double[] v, final boolean ascending) {
    	
    	int[] sortedindexes=indexSortDoubleArray(v, ascending);
    	double[] res=new double[v.length];
    	
    	for (int i = 0; i < sortedindexes.length; i++) {
			res[i]=v[sortedindexes[i]];
		}
    	
    	return new Pair<double[], int[]>(res, sortedindexes);
    }
    
    
    public static double[] convertDoubleListToArrayofdoubles(ArrayList<Double> list){
    	return ArrayUtils.toPrimitive(list.toArray(new Double[list.size()]));
    }
    
    
    
    
    
    public static void main(String[] args) {
    	double[] a=new double[]{0.4,7.2,4.3,5.0,1.2};
    	//int[] sort=indexSortDoubleArray(a,false);
    	
    	Pair<double[], int[]> sorted=getSortedDoubleArrayAndIndexes(a, true);
    	double[] sv=sorted.getValue0();
    	int[] si=sorted.getValue1();
    	for (int i = 0; i < sv.length; i++) {
			System.out.println(sv[i]+" -> "+si[i]);
		}
    }


	

}
