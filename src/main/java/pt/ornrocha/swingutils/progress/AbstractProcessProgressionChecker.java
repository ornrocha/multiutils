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
package pt.ornrocha.swingutils.progress;

import java.io.IOException;
import java.io.InputStream;

import javax.swing.SwingWorker;

import pt.ornrocha.stringutils.ReusableInputStream;

public abstract class AbstractProcessProgressionChecker extends SwingWorker<Boolean, String> {

	
	protected ReusableInputStream stream;
	protected Class<?> associatedclass=null;
	
	public AbstractProcessProgressionChecker(){}
	
	public AbstractProcessProgressionChecker(Class<?> checkprocessfrom){
		this.associatedclass=checkprocessfrom;
	}
	
	public AbstractProcessProgressionChecker(InputStream instream) throws IOException{
		this.stream=new ReusableInputStream(instream);
	}
	
	public AbstractProcessProgressionChecker(InputStream instream, Class<?> checkprocessfrom) throws IOException{
		this.stream=new ReusableInputStream(instream);
		this.associatedclass=checkprocessfrom;
	}
	
	public void setInputStream(InputStream instream) throws IOException{
		this.stream=new ReusableInputStream(instream);
	}
	
	public abstract boolean isResultsprocessed();
	public abstract Object getResultsObject();



}
