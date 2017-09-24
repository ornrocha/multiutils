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
package pt.ornrocha.rtools.rfunctions;

import org.math.R.Rsession;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RserveException;

public class MTURfunctions {

	

	public static double[] getSequence(Rsession rsession, double min, double max, double interval) throws RserveException, REXPMismatchException{

		if(rsession.connected){
			return (double[]) Rsession.cast(rsession.eval("seq("+String.valueOf(min)+","+String.valueOf(max)+",by="+String.valueOf(interval)+")"));
		}
		return null;
	}
	
	
	
}
