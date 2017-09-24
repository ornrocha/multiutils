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
package pt.ornrocha.propertyutils.configreaders;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;

import pt.ornrocha.ioutils.writers.MTUWriterUtils;
import pt.ornrocha.propertyutils.EnhancedProperties;

public abstract class PropertiesConfigReader {
	
	
	protected Properties props;
	protected String readername;
	protected LinkedHashMap<String, Object> mapextractedvalues; 
    protected LinkedHashMap<String, Object> globalextractedvalues;
	
	
	
	public PropertiesConfigReader(String namereader){
		this.readername=namereader;
		this.mapextractedvalues=new LinkedHashMap<>();
	}
	
	
	
	
	public String getName(){
		return readername;
	}
	
	protected void addPropertyObjectValue(String key,Object value){
		mapextractedvalues.put(key, value);
	}
	
	protected void addPropertyObjectValue(String key,Object value, Object defaultvalue){
		if(value!=null)
		   mapextractedvalues.put(key, value);
		else
		   mapextractedvalues.put(key, defaultvalue);
	}
	
	public abstract void readParameters()throws Exception;
	
	public abstract String openBykeyFile();
	
	public abstract String getTemplate();
	
	public abstract EnhancedProperties getPropertiesTemplate();
	
	public abstract String templateDefaultName();
	
	public abstract boolean useglobalmappedproperties();
	
	public abstract ArrayList<PropertiesConfigReader> callReaders();
	
	
	public LinkedHashMap<String, Object> getExtractedValues(){
		return mapextractedvalues;
	}
	
	
	public void loadPropertiesFromFile(String filepath) throws Exception{
		this.props=readPropertiesFile(filepath);
		readParameters();
	}
	
	public void loadProperties(Properties props) throws Exception{
		this.props=props;
		readParameters();
	}
	
	public void setGlobalMappedProperties(LinkedHashMap<String, Object> globalprops){
		this.globalextractedvalues=globalprops;
	}
	

	
	
	protected String getValueToKey(String key, boolean mandatory) throws IOException{
		if(!props.containsKey(key) && mandatory)
			throw new IOException("The mandatory: "+key+" field is not defined");
		else{
			if(props.containsKey(key) && props.getProperty(key).isEmpty() && mandatory)
				throw new IOException("The Value to "+key+" field is not defined");
			else{
				
				if(props.containsKey(key)){
				    String value=props.getProperty(key);
				    if(value.isEmpty())
					   return null;
				    else
					   return value;
				}
				else
					return null;
			}
		}
	}
	
	
	
	
	protected String getValueToKeyWithDefaultValue(String key, String defaultvalue,boolean mandatory) throws IOException{
		String value=getValueToKey(key, mandatory);
		if(value!=null)
			return value;
		else
			return defaultvalue;
	}
	
	
	protected String getValueToKeyWithValidation(String key, String[] validinputs, boolean mandatory) throws IOException{
		String value=getValueToKey(key, mandatory);
		String out="";
		if(value!=null)
		   for (int i = 0; i < validinputs.length; i++) {
			  out+=validinputs[i]+" ";
			  if(value.toLowerCase().equals(validinputs[i].toLowerCase()))
				return value;
		   }
		if(value==null && mandatory)
			throw new IOException("Please define one of these correct values: "+out+" in field: "+key);
		
		return null;
	}
	
	protected Object getGlobalProperty(String key){
		if(globalextractedvalues.containsKey(key))
			return globalextractedvalues.get(key);
		return null;
	}
	
	
	public static Properties readPropertiesFile(String filepath) throws FileNotFoundException, IOException{
		Properties props = new Properties();
		props.load(new FileInputStream(filepath));
		return props;
	}
	
	
	public void writeTemplate(String filename, boolean appendtofile){
		
		String template = getTemplate();
		
		
		ArrayList<PropertiesConfigReader> associatedreaders=callReaders();
		
		if(appendtofile){
			try {
				
				if(associatedreaders!=null){
					for (PropertiesConfigReader reader : associatedreaders) {
						reader.writeTemplate(filename, true);
					}
				}
				MTUWriterUtils.appendDataTofile(filename, template);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			String filepath=FilenameUtils.concat(filename, templateDefaultName()+".txt");
			try {

				MTUWriterUtils.writeDataTofile(filepath, template);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
	

}
