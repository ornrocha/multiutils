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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;

public class GeneralProcessProgressionChecker extends AbstractProcessProgressionChecker{

	

	
	public GeneralProcessProgressionChecker() {
		super();
	}

	public GeneralProcessProgressionChecker(Class<?> checkprocessfrom) {
		super(checkprocessfrom);
	}

	public GeneralProcessProgressionChecker(InputStream instream) throws IOException {
		super(instream);
	}
	
	public GeneralProcessProgressionChecker(InputStream instream,Class<?> clazz) throws IOException {
		super(instream);
		this.associatedclass=clazz;
	}

	@Override
	public boolean isResultsprocessed() {
		return true;
	}

	@Override
	public Object getResultsObject() {
		return null;
	}

	@Override
	protected Boolean doInBackground() throws Exception {
		
		if(stream!=null){
			
			BufferedReader inputFile = new BufferedReader(new InputStreamReader(stream));
			String currentline = null;
	        while((currentline = inputFile.readLine()) != null) {
	        	if(associatedclass!=null)
	        		LogMessageCenter.getLogger().toClass(associatedclass).addTraceMessage(currentline);
	        	else
	        	    LogMessageCenter.getLogger().addTraceMessage(currentline);	
	        	
	        }
			
		}
	
		return true;
	}

}
