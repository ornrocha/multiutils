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

import pt.ornrocha.stringutils.ReusableInputStream;

public class SimpleCMDOutputChecker implements Runnable{

	
	private ReusableInputStream stream;
	private String output=null;
	public  SimpleCMDOutputChecker(InputStream instream) throws IOException{
		this.stream=new ReusableInputStream(instream);
	}
	

	@Override
	public void run() {
		if(stream!=null){
			
			BufferedReader inputFile = new BufferedReader(new InputStreamReader(stream));
			String currentline = null;
	        try {
				while((currentline = inputFile.readLine()) != null) {
					//System.out.println(currentline);
					if(currentline!=null && !currentline.isEmpty())
						output=currentline;
				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public String getOutput(){
		return output;
	}

}
