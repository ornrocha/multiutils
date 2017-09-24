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
package pt.ornrocha.stringutils;

import java.text.DecimalFormat;

public class MTUStringFormat {
	
	
	
	public static String formatDouble(double value) {
		if(value>0.1)
			return String.format("%.1f",value);
		else if(value<0.1 && value>0.01)
			return String.format("%.2f",value);
		else if(value<0.01 && value>0.001)
			return String.format("%.3f",value);
		else if(value<0.001 && value>0.0001)
			return String.format("%.4f",value);
		else if(value<0.0001 && value>0.00001)
			return String.format("%.5f",value);
		else if(value<0.00001 && value>0.000001)
			return String.format("%.6f",value);
		else
			return formatDoubleToStringExp(value);
	}
	
	public static String formatDoubleToStringExp(double number){
	    //DecimalFormat formatter = new DecimalFormat("0.000000E000");
	    DecimalFormat formatter = new DecimalFormat("0.0E0");
	    String fnumber = formatter.format(number);
	    if (!fnumber.contains("E-")) {
	        fnumber = fnumber.replace("E", "E+");
	    }
	    return fnumber;
	}
	
	public static String formatDoubleToStringExp(double number,String expformat){
	    DecimalFormat formatter = new DecimalFormat(expformat);
	    String fnumber = formatter.format(number);
	    if (!fnumber.contains("E-")) {
	        fnumber = fnumber.replace("E", "E+");
	    }
	    return fnumber;
	}
	
	public static String formatDoubleToStringWithExponentialIfLowerThan(double number,double limit, String expformat){
	    if(number<limit)
	    	return formatDoubleToStringExp(number, expformat);
	    else
	    	return formatDouble(number);
	}

}
