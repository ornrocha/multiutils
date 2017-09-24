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
package pt.ornrocha.rtools.installutils.components;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.swingutils.progress.GeneralProcessProgressionChecker;

public class InstallationProgressionChecker extends GeneralProcessProgressionChecker{
	
	
	@Override
	protected Boolean doInBackground() throws Exception {
		
		if(stream!=null){
			
			BufferedReader inputFile = new BufferedReader(new InputStreamReader(stream));
			String currentline = null;
	        while((currentline = inputFile.readLine()) != null) {
	        	LogMessageCenter.getLogger().viewMessage(currentline);	
	        	
	        }
			
		}
	
		return true;
	}

}
