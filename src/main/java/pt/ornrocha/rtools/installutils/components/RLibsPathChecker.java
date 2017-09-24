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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.ornrocha.swingutils.progress.GeneralProcessProgressionChecker;

public class RLibsPathChecker extends GeneralProcessProgressionChecker{
	
	
	ArrayList<String> libs=new ArrayList<>();

	
	public RLibsPathChecker(InputStream instream) throws IOException {
		super(instream);
	}
	
	
	@Override
	protected Boolean doInBackground() throws Exception {
		
		if(stream!=null){
			
			//Pattern pat=Pattern.compile("[\\d+]\\s+\"(.*)\"");
			Pattern pat=Pattern.compile("]\\s+\"(.*)\"");
			
			BufferedReader inputFile = new BufferedReader(new InputStreamReader(stream));
			String currentline = null;
	        while((currentline = inputFile.readLine()) != null) {
	        	//LogMessageCenter.getLogger().toClass(getClass()).addDebugMessage(currentline);
	        	Matcher m=pat.matcher(currentline);
	        	if(m.find())
	        		libs.add(m.group(1));
	        }
			
		}
	
		return true;
	}
	
	
	public ArrayList<String> getRLibs(){
		return libs;
	}
	


}
