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

import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.javatuples.Triplet;

import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;

public class SpearmanCorrelationThread implements Runnable{

	
	double[] sample1;
	double[] sample2;
	int rowpos;
	int colpos;
	double correlation;
	
	
	public SpearmanCorrelationThread(double[] sample1, double[] sample2, int rpos, int cpos) {
		this.sample1=sample1;
		this.sample2=sample2;
		this.rowpos=rpos;
		this.colpos=cpos;
	}
	
	
	@Override
	public void run() {
		LogMessageCenter.getLogger().toClass(getClass()).addTraceMessage("Running Spearman correlation to sample: "+rowpos+" and "+colpos);
		//System.out.println("Running Spearman correlation to sample: "+rowpos+" and "+colpos);
		SpearmansCorrelation scorr=new SpearmansCorrelation();
		this.correlation=scorr.correlation(sample1, sample2);
	}
	
	
	public double getCorrelation(){
		return correlation;
	}
	
	public Triplet<Integer, Integer, Double> getCorrelationToMatrixPosition(){
		return new Triplet<Integer, Integer, Double>(rowpos, colpos, correlation);
	}

}
