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
package pt.ornrocha.rtools.connectors;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import pt.ornrocha.propertyutils.EnhancedProperties;
import pt.ornrocha.propertyutils.PropertiesUtilities;

public class RConnectorProperties extends EnhancedProperties{
	
	
	 /**
	 * 
	 */
	 private static final long serialVersionUID = 1L;
	 public static final String HOST="rserve_host";
	 public static final String PORT="rserve_port";
	 public static final String USERNAME="rserve_username";
	 public static final String PASSWORD="rserve_password";
	 public static final String RUSERLIBS="r_user_libs";

	 
	 public RConnectorProperties(){
		 super();
	 }
	 
	 
	 public RConnectorProperties(Properties props){
		 if(props.containsKey(HOST)){
			 setHost(props.getProperty(HOST));
			 
			 if(props.containsKey(PORT))
				 setPort(props.getProperty(PORT));
			 if(props.containsKey(USERNAME))
				 setUsername(props.getProperty(USERNAME));
			 if(props.containsKey(PASSWORD))
				 setPassword(props.getProperty(PASSWORD));
		 }
	 }
	 
	 public RConnectorProperties(String filepath) throws FileNotFoundException, IOException{
		 this(PropertiesUtilities.loadFileProperties(filepath));
	 }
	 
	 public void setHost(String host){
		 addPropertyKey(HOST, host);
	 }
	 
	 public String getHost(){
		 if(containsKey(HOST) && !getProperty(HOST).isEmpty())
			 return getProperty(HOST);
		 return null;
	 }
	 
	/* public void setPort(int port){
		 addPropertyKey(PORT, String.valueOf(port));
	 }*/
	 
	 public void setPort(String port){
		 addPropertyKey(PORT, port);
	 }
	 
	 public String getPort(){
		 if(containsKey(PORT) && !getProperty(PORT).isEmpty())
			 return getProperty(PORT);
		 return null;
	 }
	 
	/* public int getPort(){
		 int p=0;
		 if(containsKey(PORT) && !getProperty(PORT).isEmpty()){
			 try {
				p=Integer.parseInt(getProperty(PORT));
			} catch (Exception e) {
				p=0;
			}
		 }
         return p;
	 }*/
	 
	 public void setUsername(String username){
		 addPropertyKey(USERNAME, username);
	 }
	 
	 public String getUsername(){
		 if(containsKey(USERNAME) && !getProperty(USERNAME).isEmpty())
			 return getProperty(USERNAME);
		 return null;
	 }
	 
	 public void setPassword(String password){
		 addPropertyKey(PASSWORD, password);
	 }
	 
	 
	 public String getPassword(){
		 if(containsKey(PASSWORD) && !getProperty(PASSWORD).isEmpty())
			 return getProperty(PASSWORD);
		 return null;
	 }
	 
	 
	 public void setRUserLibFolder(String folderpath){
		 addPropertyKey(RUSERLIBS, folderpath);
	 }
	 
	 public String getRUserFolderPath(){
		 if(containsKey(RUSERLIBS) && !getProperty(RUSERLIBS).isEmpty())
			 return getProperty(RUSERLIBS);
		 return null;
	 }
	 
	 
	 
	 public static RConnectorProperties load(String filepath) throws FileNotFoundException, IOException{
		 return new RConnectorProperties(filepath);
	 }
	 
	 public static RConnectorProperties load(Properties props){
		 return new RConnectorProperties(props);
	 }
	 
	 
	 public static RConnectorProperties setupProperties(String[] keys,String[] defaultvalues, String[] comments) throws IOException{
		 RConnectorProperties props=new RConnectorProperties();
		 props.addProperties(keys, defaultvalues, comments);
		 return props;
	 }
	 

	 public static void writeSimpleRconnectorPropertiesToFile(String filepath) throws IOException{
		  RConnectorProperties props=simpleProps();
		  props.store(new FileWriter(filepath), true);  
	 }
	 
	 public static void writeCompleteRConenctorPropertiesToFile(String filepath) throws IOException{
		 RConnectorProperties props=simpleProps();
		 
		 String[] propkeys=new String[]{USERNAME,PASSWORD};
		 String[] defaultvalues=new String[]{"",""};
		 String[] comments=new String[]{"Username to connect to Rserve","User password"};
		 
		 props.addProperties(propkeys, defaultvalues, comments);
		 props.store(new FileWriter(filepath), true);  
		 
	 }
	 
	 private static RConnectorProperties simpleProps() throws IOException{
		 String[] propkeys=new String[]{HOST,PORT};
		 String[] defaultvalues=new String[]{"localhost","0"};
		 String[] comments=new String[]{"Local or remote (ip/url) of the computer where Rserve it is installed","TCP port where Rserve will listen (if value is 0 uses default Rserve port) "};
		 return RConnectorProperties.setupProperties(propkeys, defaultvalues, comments);
	 }
	 
}
