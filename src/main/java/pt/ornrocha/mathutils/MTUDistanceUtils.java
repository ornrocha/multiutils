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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.commons.math3.ml.distance.EuclideanDistance;

public class MTUDistanceUtils {
	
	
	public static double sumEuclideanDistanceRowMatrix(double[][] matrix){
		
		double sum=0.0;
		EuclideanDistance distance=new EuclideanDistance();
		Queue<double[]> queue=new LinkedList<>();
		for (int i = 0; i < matrix.length; i++) {
			queue.add(matrix[i]);
		}

		while (!queue.isEmpty()) {
			double[] topelem=queue.poll();
			Iterator<double[]> g=queue.iterator();
			while (g.hasNext()) {
				double[] nextelem= g.next();
				sum+=distance.compute(topelem, nextelem);
			}	
		}
		
		return sum;
	}
	

}
