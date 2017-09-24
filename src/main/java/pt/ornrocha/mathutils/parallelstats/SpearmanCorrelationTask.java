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
package pt.ornrocha.mathutils.parallelstats;

import java.util.concurrent.Callable;

import org.javatuples.Triplet;

public class SpearmanCorrelationTask implements Callable<Triplet<Integer, Integer, Double>>{

	
	private SpearmanCorrelationThread thread;
	
	public SpearmanCorrelationTask(double[] sample1, double[] sample2, int rowpos, int colpos){
		this.thread=new SpearmanCorrelationThread(sample1, sample2, rowpos, colpos);
	}
	
	

	@Override
	public Triplet<Integer, Integer, Double> call() throws Exception {
		thread.run();
		return thread.getCorrelationToMatrixPosition();
	}

}
