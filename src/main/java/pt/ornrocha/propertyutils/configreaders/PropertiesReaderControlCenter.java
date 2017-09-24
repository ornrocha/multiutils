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

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;

import pt.ornrocha.ioutils.writers.MTUWriterUtils;
import pt.ornrocha.printutils.MTUPrintUtils;

public class PropertiesReaderControlCenter {
	
	
	private ArrayList<PropertiesConfigReader> readers;
	private LinkedHashMap<String, Object> mapextractedproperties=new LinkedHashMap<>();
	
	public PropertiesReaderControlCenter(){
		this.readers=new ArrayList<>();
	}
	
	
	public void addReader(PropertiesConfigReader reader){
		readers.add(reader);
	}
	
	

	
	public PropertiesConfigReader getPropertiesReader(String name){
		
		for (PropertiesConfigReader propertiesReader : readers) {
			if(propertiesReader.getName().equals(name))
				return propertiesReader;
		}
		return null;
	}
	


	public Object getPropertyValue(String key){
		if(mapextractedproperties.containsKey(key))
			return mapextractedproperties.get(key);
		return null;
	}
	
	public Object getMandatoryPropertyValue(String key, Exception exception) throws Exception{
		if(mapextractedproperties.containsKey(key))
			return mapextractedproperties.get(key);
		else
			throw exception;
	}
	
	public boolean containsKey(String key){
		return mapextractedproperties.containsKey(key);
	}
	
	
	public void readPropertyFile(String filepath) throws Exception{
		Properties properties = PropertiesConfigReader.readPropertiesFile(filepath);
		loadInfoFromInternalReaders(properties,readers);
		loadInfoFromReaders(properties, readers);
	}
	
	
	private void loadInfoFromReaders(Properties properties, ArrayList<PropertiesConfigReader> readers) throws Exception{
		
		if(readers.size()>0 && properties!=null){
			
			for (int i = 0; i < readers.size(); i++) {
			 	   PropertiesConfigReader propreader=readers.get(i);
			 	   if(propreader.useglobalmappedproperties())
			 		   propreader.setGlobalMappedProperties(mapextractedproperties);
			 	   
				   String openfile=propreader.openBykeyFile();
				   if(openfile!=null){
					   if(properties.containsKey(openfile) && !properties.getProperty(openfile).isEmpty()){
						  propreader.loadPropertiesFromFile(properties.getProperty(openfile));
					   }
					   else
						  propreader.loadProperties(properties);
				   }
				   else
					  propreader.loadProperties(properties);
				
				LinkedHashMap<String, Object> extracted = propreader.getExtractedValues();
				if(extracted!=null)
				  mapextractedproperties.putAll(extracted);

			}
		}
	}
	
	
	
	
	private void loadInfoFromInternalReaders(Properties properties, ArrayList<PropertiesConfigReader> readers) throws Exception{
		ArrayList<PropertiesConfigReader> internalreaders = getInternalReaders(readers);

		if(internalreaders.size()>0)
			  loadInfoFromReaders(properties, internalreaders);

	}
	
	
	
	private ArrayList<PropertiesConfigReader> getInternalReaders(ArrayList<PropertiesConfigReader> tocheck ){
		ArrayList<PropertiesConfigReader> container =new ArrayList<>();
		
		for (int i = 0; i < tocheck.size(); i++) {
			PropertiesConfigReader reader=tocheck.get(i);
			addInternalReader(container, reader);
		}

		return container;
	}
	
	private void addInternalReader(ArrayList<PropertiesConfigReader> container, PropertiesConfigReader reader){
		
		ArrayList<PropertiesConfigReader> readers= reader.callReaders();
		if(readers!=null){
			for (PropertiesConfigReader inreader : readers) {
				addInternalReader(container, inreader);
				container.add(inreader);
			}
		}
	 }
	
	
	
	
	public void writeTemplatesToDir(String dirpath,String filename, boolean singlefile){
		String mainfilepath=null;
		
		if(filename!=null)
		   mainfilepath=FilenameUtils.concat(dirpath, filename+".txt");
		else
		   mainfilepath=FilenameUtils.concat(dirpath, "MainConfigurationFile.txt");
		
		if(singlefile){
			String filepath=FilenameUtils.concat(dirpath, mainfilepath);
			for (PropertiesConfigReader reader : readers) {
				reader.writeTemplate(filepath, true);
			}
		}
		else{
			StringBuilder data=new StringBuilder();
			ArrayList<PropertiesConfigReader> internalreadersdata=getInternalReaders(readers);
			
			if(internalreadersdata.size()>0)
				writeTemplatesToStringBuilder(dirpath, data, internalreadersdata);
			
			writeTemplatesToStringBuilder(dirpath, data, readers);

			try {
				MTUWriterUtils.writeDataTofile(mainfilepath, data.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void writeTemplateToSingleFile(String filepath){
		String dir=FilenameUtils.getPath(filepath);
		String name=FilenameUtils.getBaseName(filepath);
		writeTemplatesToDir(dir, name, true);
	}
	
	public void writeTemplateFiles(String dirpath,String mainfilename){
		writeTemplatesToDir(dirpath, mainfilename, false);
	}
	
	
	private void writeTemplatesToStringBuilder(String dirpath,StringBuilder data,ArrayList<PropertiesConfigReader> readers){
		
		for (PropertiesConfigReader reader : readers) {
			String url=FilenameUtils.concat(dirpath, reader.templateDefaultName()+".txt");
			String linkurl=reader.openBykeyFile()+" = "+url;
			data.append(linkurl+"\n");
			reader.writeTemplate(dirpath, false);
		}
		
		
	}
	
	public void printKeySet(){
		LinkedHashSet<String> keys=new LinkedHashSet<>(mapextractedproperties.keySet());
		
		for (String string : keys) {
			System.out.println(string);
		}
	}
	
	public void printExtractedData(){
		MTUPrintUtils.printMap(mapextractedproperties);
	}
	
	
	public LinkedHashMap<String, Object> getExtractedProperties(){
		return mapextractedproperties;
	}
	

	public static void main(String[] args) {
		String file="/home/orocha/MEOCloud/TRABALHO/Models/irj904_xml_alterado_acordo_estudo_covert/loadmodel.txt";
		
		PropertiesReaderControlCenter props = new PropertiesReaderControlCenter();
        //props.addReader(new IntegratedModelPropertiesReader());
  
        try {
			props.writeTemplatesToDir("/home/orocha/MEOCloud/TRABALHO/Model_Simulations/teste", null, true);
			System.out.println(props.getExtractedProperties());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}
