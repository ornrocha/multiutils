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
package pt.ornrocha.propertyutils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.systemutils.OSystemUtils;

public class PropertiesUtilities {
	
	
	public static double getDoublePropertyValue(Properties propfile, String key,double defaultvalue, Class<?> methodclass){
		double value=Double.NaN;
		try {
			if(propfile.containsKey(key))
				value=Double.parseDouble(propfile.getProperty(key));
		} catch (Exception e) {
			LogMessageCenter.getLogger().toClass(methodclass).addWarnMessage("Error parsing value for ["+key+"], using default value "+defaultvalue);
			return defaultvalue;
		}
		if(value==Double.NaN)
			value=defaultvalue;
		return value;
	}
	
	
	public static double getDoublePropertyValueValidLimits(Properties propfile, String key,double defaultvalue, double lowerlimit, double upperlimit,boolean limitvaluesincluded, Class<?> methodclass){
		double value=Double.NaN;
		try {
			if(propfile.containsKey(key))
				value=Double.parseDouble(propfile.getProperty(key));
		} catch (Exception e) {
			LogMessageCenter.getLogger().toClass(methodclass).addWarnMessage("Error parsing value for ["+key+"], using default value "+defaultvalue);
			return defaultvalue;
		}
		
		if(limitvaluesincluded && value>=lowerlimit && value<=upperlimit )
			return value;
		else if(!limitvaluesincluded && value>lowerlimit && value<upperlimit)
			return value;
		
		LogMessageCenter.getLogger().toClass(methodclass).addWarnMessage("Error input value for ["+key+"], using default value "+defaultvalue);
		return defaultvalue;		
	}
	
	public static double getDoublePropertyValueValidLimits(Properties propfile, String key,double defaultvalue, double lowerlimit,boolean lowerlimitincluded, double upperlimit,boolean upperlimitincluded, Class<?> methodclass){
		double value=Double.NaN;
		try {
			if(propfile.containsKey(key))
				value=Double.parseDouble(propfile.getProperty(key));
		} catch (Exception e) {
			LogMessageCenter.getLogger().toClass(methodclass).addWarnMessage("Error parsing value for ["+key+"], using default value "+defaultvalue);
			return defaultvalue;
		}
		
		if(!lowerlimitincluded && value<=lowerlimit){
			LogMessageCenter.getLogger().toClass(methodclass).addWarnMessage("Error input value for ["+key+"], using default value "+defaultvalue);
			return defaultvalue;
			
		}
		else if(!upperlimitincluded && value>=upperlimit){
			LogMessageCenter.getLogger().toClass(methodclass).addWarnMessage("Error input value for ["+key+"], using default value "+defaultvalue);
			return defaultvalue;
		}
		
		return value;		
	}
	
	
	public static double getDoublePropertyValueValidLowerLimit(Properties propfile, String key,double defaultvalue, double lowerlimit,boolean limitvaluesincluded, Class<?> methodclass){
		return getDoublePropertyValueValidLimits(propfile, key, defaultvalue, lowerlimit, Double.MAX_VALUE,limitvaluesincluded, methodclass);
	}
	
	
	
	public static int getIntegerPropertyValue(Properties propfile, String key,int defaultvalue, Class<?> methodclass){
		int value=Integer.MAX_VALUE;
		try {
			if(propfile.containsKey(key))
				value=Integer.parseInt(propfile.getProperty(key));
		} catch (Exception e) {
			if(methodclass!=null)
				LogMessageCenter.getLogger().toClass(methodclass).addWarnMessage("Error parsing value for ["+key+"], using default value "+defaultvalue);
			return defaultvalue;
		}
		if(value==Integer.MAX_VALUE)
			value=defaultvalue;
		return value;
	}
	
	public static int getIntegerPropertyValueValidLimits(Properties propfile, String key,int defaultvalue, int lowerlimit, int upperlimit,boolean limitvaluesincluded, Class<?> methodclass){
		int value=Integer.MAX_VALUE;
		try {
			if(propfile.containsKey(key))
				value=Integer.parseInt(propfile.getProperty(key));
			else
				value=defaultvalue;
		} catch (Exception e) {
			LogMessageCenter.getLogger().toClass(methodclass).addWarnMessage("Error parsing value for ["+key+"], using default value "+defaultvalue);
			return defaultvalue;
		}
		
		if(limitvaluesincluded && value>=lowerlimit && value<=upperlimit)
			return value;
		else if(!limitvaluesincluded && value>lowerlimit && value<upperlimit)
			return value;
		
		LogMessageCenter.getLogger().toClass(methodclass).addWarnMessage("Error input value for ["+key+"], using default value "+defaultvalue);
		return defaultvalue;
		
	}
	

	public static int getIntegerPropertyValueValidLowerLimit(Properties propfile, String key, int defaultvalue,int lowerlimit,boolean limitvaluesincluded, Class<?> methodclass){
		 return getIntegerPropertyValueValidLimits(propfile, key, defaultvalue, lowerlimit, Integer.MAX_VALUE,limitvaluesincluded, methodclass);
	}
	
	
	
	
	public static boolean getBooleanPropertyValue(Properties propfile, String key,boolean defaultvalue, Class<?> methodclass){
		boolean value=false;
		try {
			if(propfile.containsKey(key))
				value=Boolean.parseBoolean(propfile.getProperty(key));
		} catch (Exception e) {
			if(methodclass!=null)
				LogMessageCenter.getLogger().toClass(methodclass).addWarnMessage("Error parsing value for ["+key+"], using default value "+defaultvalue);
			return defaultvalue;
		}

		return value;
	}
	
	
	public static String getStringPropertyValue(Properties propfile, String key,String defaultvalue, Class<?> methodclass){
		String value=null;
		try {
			if(propfile.containsKey(key)){
				value=propfile.getProperty(key);
				if(value.isEmpty())
					value=null;
			}
		} catch (Exception e) {
			if(methodclass!=null)
				LogMessageCenter.getLogger().toClass(methodclass).addWarnMessage("Error parsing value for ["+key+"], using default value "+defaultvalue);
			return null;
		}
		if(defaultvalue!=null && (value==null || value.isEmpty()))
		   value=defaultvalue;
		return value;
	}
	
	
	public static String getStringPropertyValue(Properties propfile, String key,String defaultvalue, String[] valuesallowed, Class<?> methodclass){
		String value=null;
		try {
			if(propfile.containsKey(key)){
				String tmpvalue=propfile.getProperty(key);
				
				for (String alw : valuesallowed) {
					if(tmpvalue.toLowerCase().equals(alw.toLowerCase()))
						value=tmpvalue;
				}
			}
		} catch (Exception e) {
			LogMessageCenter.getLogger().toClass(methodclass).addWarnMessage("Invalid value for ["+key+"], using default value "+defaultvalue
					+". One of the following parameters: "+getAllowedInputParametersString(valuesallowed)+" can be chosen");
			return null;
		}
		if(defaultvalue!=null && value==null)
		   value=defaultvalue;
		return value;
	}
	
	private static String getAllowedInputParametersString(String[] list){
		StringBuilder str=new StringBuilder();
		int n=0;
		for (String string : list) {
			str.append(string);
			if(n<list.length-1)
				str.append(";");
			n++;
		}
		return str.toString();
	}
	
	public static Properties loadFileProperties(String filepath) throws FileNotFoundException, IOException{
		Properties props=new Properties();
		if(OSystemUtils.isWindows()){
			BufferedReader metadataReader = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
			props.load(new StringReader(IOUtils.toString(metadataReader).replace("\\", "/")));
		}
		else
			props.load(new FileReader(filepath));
		return props;
	}
	
	

}
