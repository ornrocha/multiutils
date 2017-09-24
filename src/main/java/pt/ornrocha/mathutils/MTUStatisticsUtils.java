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

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.javatuples.Pair;

import pt.ornrocha.arrays.MTUArrayUtils;

public class MTUStatisticsUtils {
	
	
	
	public static double[] scaleArrayByZScores(double[] inputarray){
		DescriptiveStatistics stats = new DescriptiveStatistics();
		for (int i = 0; i < inputarray.length; i++) {
			stats.addValue(inputarray[i]);
		}
		
		double mean=stats.getMean();
		double std_dev=stats.getStandardDeviation();
		
		double[] res=new double[inputarray.length];
		for (int i = 0; i < res.length; i++) {
			res[i]=(inputarray[i]-mean)/std_dev;
		}
		
		return res;
	}
	
	public static double[][] scaleMatrixToZScoresByColumns(double[][] inputmatrix){
		
		RealMatrix inmatrix = MatrixUtils.createRealMatrix(inputmatrix);
		RealMatrix outmatrix=MatrixUtils.createRealMatrix(inmatrix.getRowDimension(),inmatrix.getColumnDimension());
		
		int ncolumns=inmatrix.getColumnDimension();


		for (int i = 0; i < ncolumns; i++) {
			outmatrix.setColumn(i, scaleArrayByZScores(inmatrix.getColumn(i)));
		}
		
		return outmatrix.getData();
		
	}
	

	
	public static double[] scaleArray(double[] inputarray){
		double sumsquare =0;
		for (int i = 0; i < inputarray.length; i++) {
			sumsquare+=Math.pow(inputarray[i],2);
		}
		
		double sqrsum=Math.sqrt(sumsquare/(inputarray.length-1));
		
		double[] res=new double[inputarray.length];
		for (int i = 0; i < res.length; i++) {
			res[i]=inputarray[i]/sqrsum;
		}
		
		return res;
	}
	
	public static double[] centerArray(double[] inputarray){
		
		DescriptiveStatistics stats = new DescriptiveStatistics();
		for (int i = 0; i < inputarray.length; i++) {
			stats.addValue(inputarray[i]);
		}
		
		double mean=stats.getMean();
		
		double[] res=new double[inputarray.length];
		for (int i = 0; i < res.length; i++) {
			res[i]=inputarray[i]-mean;
		}
		
		return res;
		
	}
	
	
	public static double[][] scaleMatrix(double[][] inputmatrix, boolean center, boolean scale){
		
		RealMatrix inmatrix = MatrixUtils.createRealMatrix(inputmatrix);
		RealMatrix outmatrix=MatrixUtils.createRealMatrix(inmatrix.getRowDimension(),inmatrix.getColumnDimension());
		
		int ncolumns=inmatrix.getColumnDimension();


		for (int i = 0; i < ncolumns; i++) {
			
			double[] tempres=inmatrix.getColumn(i);
			if(center)
				tempres=centerArray(tempres);
			if(scale)
				tempres=scaleArray(tempres);
			
			outmatrix.setColumn(i, tempres);
		}
		
		return outmatrix.getData();
	}
	
	
	
	public static double[][] normalizeByColums(double[][] inputmatrix){
		RealMatrix inmatrix = MatrixUtils.createRealMatrix(inputmatrix);
		RealMatrix outmatrix=MatrixUtils.createRealMatrix(inmatrix.getRowDimension(),inmatrix.getColumnDimension());
		int ncolumns=inmatrix.getColumnDimension();
		
		for (int i = 0; i < ncolumns; i++) {
			outmatrix.setColumn(i, StatUtils.normalize(inmatrix.getColumn(i)));
		}
		return outmatrix.getData();
	}
	
	public static double getQuantileValue(double[] sortedvector, double quantile){
		if(quantile<1.0)
			quantile=quantile*100;
		
		Percentile p =new Percentile(quantile);
	    p.setData(sortedvector);
	    return p.evaluate();
	}

	public static double getQuantileValueSortDataFirst(double[] vector, double quantile){
		Arrays.sort(vector);
		return getQuantileValue(vector, quantile);
	}
	
	
	public static double[] getMatrixColumnsMean(double[][] mat){
		double[] total=new double[mat[0].length];
		 
		 for (int i = 0; i < mat[0].length; i++) {
			 double [] colum=MTUArrayUtils.getMatrixColumn(mat, i);
			 DescriptiveStatistics stats=new DescriptiveStatistics(colum);
			 total[i]=stats.getMean();
			 
		  }
		 return total;
	}
	
	
	public static Pair<Double, Double> calculateMeanAndStandardDeviationOfAnArrayOfExpectedSize(double[] array, Integer expectedsize){
		
		int sizearray=0;
		if(expectedsize==null)
			sizearray=array.length;
		else if(expectedsize<array.length)
			sizearray=array.length;
		else
			sizearray=expectedsize;
		
		double acc=0;
		
		for (int i = 0; i < array.length; i++) {
			acc+=array[i];
		}
		
		double mean=acc/sizearray;
		
		StandardDeviation SD=new StandardDeviation();
		
		double sdv=SD.evaluate(array, mean);
		//double sdv=SD.evaluate(array);
		
		return new Pair<Double, Double>(mean, sdv);
	}
	
	public static Pair<Double, Double> calculateMeanAndStandardDeviationOfAnArray(double[] array){
		return calculateMeanAndStandardDeviationOfAnArrayOfExpectedSize(array, null);
	}
	
	public static Pair<Double, Double> calculateMeanAndStandardDeviationOfAnArrayOfExpectedSize(ArrayList<Double> vallist, Integer expectedsize){
		
		double [] array=ArrayUtils.toPrimitive(vallist.toArray(new Double[vallist.size()]));
        return calculateMeanAndStandardDeviationOfAnArrayOfExpectedSize(array, expectedsize);		
	}
	

	public static Pair<Double, Double> calculateMeanAndStandardDeviationOfListDoubles(ArrayList<Double> vallist){
		
		double [] array=ArrayUtils.toPrimitive(vallist.toArray(new Double[vallist.size()]));
        return calculateMeanAndStandardDeviationOfAnArrayOfExpectedSize(array, null);		
	}
	
	
	public static void main(String[] args){
		double[] x =new double[]{1,1,2,2,4,4,5,5,5,5,6,8,8,8,9,9,9,9,10,10};
		double[] sc =scaleArrayByZScores(x);
		for (int i = 0; i < sc.length; i++) {
			System.out.println(sc[i]);
		}

	}
	
	
}
