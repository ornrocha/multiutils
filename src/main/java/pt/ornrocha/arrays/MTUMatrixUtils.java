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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.javatuples.Pair;

import pt.ornrocha.ioutils.readers.MTUReadUtils;

public class MTUMatrixUtils {
	
	
	public static String[][] addColumnToMatrix(String[] array,String[][] matrix, int position){
		
		
		int nrows=0;
		int arraysize=array.length;
		int matrixsize=matrix.length;
		
		if(arraysize<matrixsize)
			nrows=arraysize;
		else
			nrows=matrixsize;
		
		for (int i = 0; i < nrows; i++) {
			
			matrix[i][position]=array[i];
			
		}
		
		return matrix;
	}
	
	public static double[][] convertIntToDoubleMatrix(int[][] mat){
		double[][] res =new double[mat.length][mat[0].length];
		
		for (int i = 0; i < mat.length; i++) {
			int[] row= mat[i];
			
			for (int j = 0; j < row.length; j++) {
				res[i][j]=(double)row[j];
			}
		}
		return res;
	}
	
	public static boolean doubleMatrixContainsNANValues(double[][] matrix){
		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if(Double.isNaN(matrix[i][j]))
					return true;
			}
		}
		return false;
	}
	
	public static double[][] transposeMatrix(double [][] m){
        double[][] res = new double[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                res[j][i] = m[i][j];
        return res;
    }
	
	
	public static double[][] getdoubleMatrixElementsDifferenceRowByRow(double[][] matrix, boolean keepsizematrix){
		
		double[][] res=null;
		int storerowindex=0;
		if(keepsizematrix){
			res=new double[matrix.length][matrix[0].length];
			storerowindex=1;
		}
		else
			res=new double[matrix.length-1][matrix[0].length];
		
		
		for (int i = 1; i < res.length; i++) {
			double[] previousindex=matrix[i-1];
			double[] currindex=matrix[i];
			double[] store=new double[currindex.length];
			for (int j = 0; j < currindex.length; j++) {
				store[j]=currindex[j]-previousindex[j];
			}
			res[storerowindex]=store;
			storerowindex++;
		}
		
		return res;
		
	}
	
	

	public static double[][] getdoubleMatrixElementsRatioRowByRow(double[][] matrix, boolean keepsizematrix){
		
		double[][] res=null;
		int storerowindex=0;
		if(keepsizematrix){
			res=new double[matrix.length][matrix[0].length];
			storerowindex=1;
		}
		else
			res=new double[matrix.length-1][matrix[0].length];
		
		
		for (int i = 1; i < res.length; i++) {
			double[] previousindex=matrix[i-1];
			double[] currindex=matrix[i];
			double[] store=new double[currindex.length];
			for (int j = 0; j < currindex.length; j++) {
				store[j]=currindex[j]/previousindex[j];
			}
			res[storerowindex]=store;
			storerowindex++;
		}
		
		return res;
		
	}
	

	public static double[][] getdoubleMatrixElementsDifferenceColumnByRColumn(double[][] matrix, boolean keepsizematrix){
		
		double[][] matrixtranspose=transposeMatrix(matrix);
		double[][] res=getdoubleMatrixElementsDifferenceRowByRow(matrixtranspose, keepsizematrix);
		return transposeMatrix(res);
		
	}
	

	public static double[][] getdoubleMatrixElementsRatioColumnByRColumn(double[][] matrix, boolean keepsizematrix){
		
		double[][] matrixtranspose=transposeMatrix(matrix);
		double[][] res=getdoubleMatrixElementsRatioRowByRow(matrixtranspose, keepsizematrix);
		return transposeMatrix(res);
		
	}
	
	public static double maxValueOfMatrix(double [][] matrix){
		double currentmax=Double.MIN_VALUE;
		for (double[] row : matrix) {
			double rowmax=Arrays.stream(row).max().getAsDouble();
			currentmax=Math.max(rowmax, currentmax);
		}
		return currentmax;
	}
	
	public static double minValueOfMatrix(double [][] matrix){
		double currentmin=Double.MAX_VALUE;
		for (double[] row : matrix) {
			double rowmin=Arrays.stream(row).min().getAsDouble();
			currentmin=Math.min(rowmin, currentmin);
		}
		return currentmin;
	}
	
	public static Pair<Double, Double> getMinMaxValuesOfMatrix(double [][] matrix){
		double currentmax=Double.MIN_VALUE;
		double currentmin=Double.MAX_VALUE;
		for (double[] row : matrix) {
			double rowmax=Arrays.stream(row).max().getAsDouble();
			double rowmin=Arrays.stream(row).min().getAsDouble();
			currentmax=Math.max(rowmax, currentmax);
			currentmin=Math.min(rowmin, currentmin);
		}
		return new Pair<Double, Double>(currentmin, currentmax);
	}
	
	
	public static double[] getColumnofMatrixofdoubles(double[][] matrix, int column){
		if(column<matrix[0].length){
			double[] res=new double[matrix.length];
			for (int i = 0; i < matrix.length; i++) {
				res[i]=matrix[i][column];
			}
			return res;
		}
		return null;
	}
	
	
	public static double[] getNElementsOfColumnofMatrixofdoubles(double[][] matrix, int column, int numberelems){
		double[] vector=getColumnofMatrixofdoubles(matrix, column);
		
		double[] res=new double[numberelems];
		for (int i = 0; i < res.length; i++) {
			res[i]=vector[i];
		}
		return res;
	}
	
	
	public static double[][] convertStringFormatMatrixToMatrixofdoubles(String matrixasstring, String delimiter){
		
		String[] lines=matrixasstring.split("\n");
		
		String[] lineelems=lines[0].split(delimiter);
		
		double[][] matrix=new double[lines.length][lineelems.length];
		
		for (int i = 0; i < lines.length; i++) {
			String[] elems=lines[i].split(delimiter);
			for (int j = 0; j < elems.length; j++) {
				matrix[i][j]=Double.valueOf(elems[j]);
			}
		}
		return matrix;
	}
	
	public static double[][] loadMatrixOfdoublesFromFile(String filepath, String delimiter) throws IOException{
		ArrayList<String> lines=MTUReadUtils.readFileLinesRemoveEmptyLines(filepath);
		
		int ncol=lines.get(0).split(delimiter).length;
		
		double[][] matrix=new double[lines.size()][ncol];
		
		for (int i = 0; i < lines.size(); i++) {
			String[] lineelems=lines.get(i).trim().split(delimiter);
			for (int j = 0; j < lineelems.length; j++) {
				matrix[i][j]=Double.parseDouble(lineelems[j]);
			}
		}
		return matrix;
	}
	
	

}
