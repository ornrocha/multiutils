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

import java.util.ArrayList;

import org.javatuples.Triplet;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;

import pt.ornrocha.threadutils.MTUMultiThreadCallableExecutor;

public class ParallelSpearmanCorrelation {

	
	
	private double[][] inputdata;
	private ArrayList<SpearmanCorrelationTask> toprocess;
	private int simultaneousprocesses=Runtime.getRuntime().availableProcessors();
	//private int simultaneousprocesses=1;
	private double[][] correlationmatrix;
	
	
	public ParallelSpearmanCorrelation(double[][] inputdata){
		this.inputdata=inputdata;
		setup();
	}
	
	public ParallelSpearmanCorrelation(int[][] inputdata){
		this.inputdata=convertMatrix(inputdata);
		setup();
	}
	
	
	public void setNumberSimultaneousProcesses(int np){
		this.simultaneousprocesses=np;
	}
	
	
	private double[][] convertMatrix(int[][] data){
		
		double[][] converted=new double[data.length][data[0].length];
		for (int i = 0; i < data.length; i++) {
			double[] cv=Doubles.toArray(Ints.asList(data[i]));
			converted[i]=cv;
		}
		return converted;
	}
	
	
	private void setup(){
		this.toprocess=new ArrayList<>();
		
		for (int i = 0; i < inputdata.length-1; i++) {
			double[] sample1=inputdata[i];
			for (int j = i+1; j < inputdata.length; j++) {
				double[]sample2=inputdata[j];
				SpearmanCorrelationTask t=new SpearmanCorrelationTask(sample1, sample2, i, j);
				
				toprocess.add(t);
			}
		}
	}
	
	public void execute() throws Exception{
		ArrayList<Triplet<Integer, Integer, Double>> results=(ArrayList<Triplet<Integer, Integer, Double>>) MTUMultiThreadCallableExecutor.run(toprocess, simultaneousprocesses);
		
		correlationmatrix=new double[inputdata.length][inputdata.length];
		for (int i = 0; i < results.size(); i++) {
			Triplet<Integer, Integer, Double> rcres=results.get(i);
			correlationmatrix[rcres.getValue0()][rcres.getValue1()]=rcres.getValue2();
			correlationmatrix[rcres.getValue1()][rcres.getValue0()]=rcres.getValue2();
		}
	}
	
	
	
	public double[][] getCorrelationMatrix() throws Exception{
		if(correlationmatrix==null)
			execute();
		return correlationmatrix;
	}
	
	
	
	
	public static double[][] calculateCorrelation(int[][] matrix, int nprocesses) throws Exception{
		ParallelSpearmanCorrelation sp=new ParallelSpearmanCorrelation(matrix);
		sp.setNumberSimultaneousProcesses(nprocesses);
		sp.execute();
		return sp.getCorrelationMatrix();
	}
	
/*	public double[][] processSpearmanCorrelationforGeneCoexpression(int[][] frequencyatconditions){
		ExpressionData data=biclusterset.getExpressionDataset();
		double[][] gene2gene=new double[data.getGeneNamesList().size()][data.getGeneNamesList().size()];
		SpearmansCorrelation corr=new SpearmansCorrelation();
		for (int y = 0; y < frequencyatconditions.length-1; y++) {
			int[] genearray1=frequencyatconditions[y];
			LogMessageCenter.getLogger().toClass(getClass()).addTraceMessage("Calculating spearman correlation of row: "+y+" and all post rows");
			double[] genearray1d=Doubles.toArray(Ints.asList(genearray1));
			for (int x = y+1; x < frequencyatconditions.length; x++) {
				//LogMessageCenter.getLogger().toClass(getClass()).addTraceMessage("Calculating spearman correlation of row: "+y+" vs row: "+x);
				int[] genearray2=frequencyatconditions[x];
				double[] genearray2d=Doubles.toArray(Ints.asList(genearray2));
				//double spearmanrank=CorrelationDistance.spearman(genearray1, genearray2);
				//double spearmanrank=smile.math.Math.spearman(genearray1,genearray2);
				double spearmanrank=corr.correlation(genearray1d, genearray2d);
				gene2gene[y][x]=spearmanrank;
			}
		}
		return gene2gene;*/
	
	
}
