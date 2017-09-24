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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import pt.ornrocha.stringutils.MTUStringUtils;

public class EnhancedPropertiesWithSubGroups extends Properties{

	private static final long serialVersionUID = 1L;
	
	// main groups mapping 
	protected HashMap<String, ArrayList<String>> categoryitemsmap=new HashMap<>();
	protected HashMap<String, ArrayList<String>> subgroupcategorymap=new HashMap<>();
	protected HashMap<String, String> keycommentmap=new HashMap<>();
	protected ArrayList<String> maincategoryinputorder=new ArrayList<>();
	protected ArrayList<String> subcategoryinputorder=new ArrayList<>();
	
	
	
	
	
	public HashMap<String, ArrayList<String>> getCategoryItemsMap() {
		return categoryitemsmap;
	}


	public HashMap<String, ArrayList<String>> getSubGroupCategoryMap() {
		return subgroupcategorymap;
	}


	public HashMap<String, String> getKeyCommentsMap() {
		return keycommentmap;
	}


	public ArrayList<String> getMainCategoryInputOrder() {
		return maincategoryinputorder;
	}


	public synchronized void addPropertyToGroupCategory(String maincategoryname, String key, String value, String comment){
		if(!maincategoryinputorder.contains(maincategoryname))
			maincategoryinputorder.add(maincategoryname);
		
		if(categoryitemsmap.containsKey(maincategoryname))
			categoryitemsmap.get(maincategoryname).add(key);
		else{
			ArrayList<String> categoryitems=new ArrayList<>();
			categoryitems.add(key);
			categoryitemsmap.put(maincategoryname, categoryitems);
		}
		this.setProperty(key, value);
		if(comment!=null && !comment.isEmpty())
			keycommentmap.put(key, comment);
	}
	
	
	public synchronized void addPropertyToSubGroupCategory(String maincategory, String subcategory, String key, String value, String comment){
		if(!maincategoryinputorder.contains(maincategory))
			maincategoryinputorder.add(maincategory);
		
		if(!subcategory.contains(subcategory))
			subcategoryinputorder.add(subcategory);
		
		if(categoryitemsmap.containsKey(maincategory)){
			ArrayList<String> subcats=categoryitemsmap.get(maincategory);
			if(!subcats.contains(subcategory)){
				subcats.add(subcategory);
				ArrayList<String> subcategorykeylistmap=new ArrayList<>();
				subcategorykeylistmap.add(key);
				subgroupcategorymap.put(subcategory, subcategorykeylistmap);
			}
			else
				subgroupcategorymap.get(subcategory).add(key);
			//categoryitemsmap.get(maincategory).add(subcategory);
			
		}
		else{
			ArrayList<String> subcategories=new ArrayList<>();
			subcategories.add(subcategory);
			categoryitemsmap.put(maincategory, subcategories);
			
			ArrayList<String> subcategorykeylistmap=new ArrayList<>();
			subcategorykeylistmap.add(key);
			subgroupcategorymap.put(subcategory, subcategorykeylistmap);
		}
		
		this.setProperty(key, value);
		if(comment!=null && !comment.isEmpty())
			keycommentmap.put(key, comment);
		
	}
	
	
	public void appendProperties(EnhancedPropertiesWithSubGroups inputproperties){
		
		ArrayList<String> maincategorytoappend=inputproperties.getMainCategoryInputOrder();
		
		for (int i = 0; i < maincategorytoappend.size(); i++) {
			if(!maincategoryinputorder.contains(maincategorytoappend.get(i)))
				maincategoryinputorder.add(maincategorytoappend.get(i));
		}
		
		HashMap<String, String> keycommentmaptoappend=inputproperties.getKeyCommentsMap();
		
		for (String key : keycommentmaptoappend.keySet()) {
			if(!keycommentmap.containsKey(key))
				keycommentmap.put(key, keycommentmaptoappend.get(key));
		}
		
		HashMap<String, ArrayList<String>> categoryitemsmaptoappend=inputproperties.getCategoryItemsMap();
		
		for (Map.Entry<String,ArrayList<String>> m : categoryitemsmaptoappend.entrySet()) {
			if(!categoryitemsmap.containsKey(m.getKey())){
				categoryitemsmap.put(m.getKey(), m.getValue());
			}
			else{
				ArrayList<String> origval=categoryitemsmap.get(m.getKey());
				ArrayList<String> valtoappend=m.getValue();
				for (int i = 0; i < valtoappend.size(); i++) {
					if(!origval.contains(valtoappend.get(i)))
						origval.add(valtoappend.get(i));
				}
			}	
		}
		
		
		HashMap<String, ArrayList<String>> subgroupcategorymaptoappend=inputproperties.getSubGroupCategoryMap();
		for (Map.Entry<String,ArrayList<String>> m : subgroupcategorymaptoappend.entrySet()) {
			if(!subgroupcategorymap.containsKey(m.getKey()))
				subgroupcategorymap.put(m.getKey(), m.getValue());
			else{
				ArrayList<String> origval=subgroupcategorymap.get(m.getKey());
				ArrayList<String> valtoappend=m.getValue();
				for (int i = 0; i < valtoappend.size(); i++) {
					if(!origval.contains(valtoappend.get(i)))
						origval.add(valtoappend.get(i));
				}
			}
			
		}
		
		for (Object key : inputproperties.keySet()) {
			if(!containsKey(key))
				setProperty((String)key, (String) inputproperties.get(key));
		}
		
	}
	
	

	public void addPropertiesToGroupCategory(String maincategoryname, String[] keys, String[] values, String[] comments) throws IOException{
		
		if(keys.length!=values.length || keys.length!=comments.length)
			throw new IOException("Input arrays must have the same dimension");
		else{
			
			for (int i = 0; i < keys.length; i++) {
				addPropertyToGroupCategory(maincategoryname, keys[i], values[i], comments[i]);
			}	
		}
	}
	
	
	public void addPropertiesToSubGoupCategory(String maincategoryname, String subgroupcategory, String[] keys, String[] values, String[] comments) throws IOException{
		if(keys.length!=values.length || keys.length!=comments.length)
			throw new IOException("Input arrays must have the same dimension");
		else{
			for (int i = 0; i < keys.length; i++) {
				addPropertyToSubGroupCategory(maincategoryname, subgroupcategory, keys[i], values[i], comments[i]);
			}
		}
	}
	
	
	
	public void store(Writer writer, boolean writecomments)throws IOException{
	        store0((writer instanceof BufferedWriter)?(BufferedWriter)writer: new BufferedWriter(writer),writecomments,false);
	 }
	
	
	private String getCommentCategoryString(String text,String commentstr, int ncom, String textprefix){
		String comment=MTUStringUtils.getRepeatedWordXTimes(commentstr, ncom);
		if(textprefix!=null)
			return comment+" "+textprefix+" "+text+" "+comment;
		return comment+" "+text+" "+comment;
	}
	
	 private void store0(BufferedWriter bw, boolean writecomments, boolean escUnicode)throws IOException{
		
		        synchronized (this) {
		        	if(maincategoryinputorder.size()>0){
		        		for (int i = 0; i < maincategoryinputorder.size(); i++) {
		        			String maincategory=maincategoryinputorder.get(i);
		        			
		        			writeComments(bw, getCommentCategoryString(maincategory,"#",20,null));
		        			
		        			ArrayList<String> items=categoryitemsmap.get(maincategory);
		        			//System.out.println("Items: "+items);
		        			
		        			for (int j = 0; j < items.size(); j++) {
								String itemkey=items.get(j);
								String comment=null;
								String key=null;
								String value="";
								String tmpvalue=null;
		        				
								if(subgroupcategorymap.containsKey(itemkey)){
									//bw.newLine();
									writeComments(bw, getCommentCategoryString(itemkey, "@@@@", 3, "Begin"));
									bw.newLine();
		        					ArrayList<String> subitems=subgroupcategorymap.get(itemkey);
		        					if(subitems.size()>0){
		        						for (int k = 0; k < subitems.size(); k++) {
		        							comment=null;
		        							key=null;
		        							value="";
		        							tmpvalue=null;
		        							
											String subkey=subitems.get(k);
											//System.out.println(subkey);
											if(keycommentmap.containsKey(subkey))
												comment=keycommentmap.get(subkey);
											
											//System.out.println(subkey+" --> "+containsKey(subkey));
											tmpvalue=(String) get(subkey);
											key=saveConvert(subkey, true, escUnicode);
											if(tmpvalue!=null)
												value = saveConvert(tmpvalue, false, escUnicode);
											
											if(comment!=null){
												writeComments(bw,comment);
											}
											bw.write(key + "=" + value);
								            bw.write("\n\n");
										}
		        						//bw.newLine();
		        						
		        					}
		        					
		        					writeComments(bw, getCommentCategoryString(itemkey, "@@@@", 3,"End"));
		        					bw.newLine();
		        				}else{
		        					
		        					if(keycommentmap.containsKey(itemkey))
										comment=keycommentmap.get(itemkey);
									
									tmpvalue=(String) get(itemkey);
									key=saveConvert(itemkey, true, escUnicode);
									if(tmpvalue!=null)
										value = saveConvert(tmpvalue, false, escUnicode);
									
									if(comment!=null){
										writeComments(bw,comment);
									}
									bw.write(key + "=" + value);
						            bw.newLine();

		        				}
								bw.write("\n");
								
							}
		        			bw.write("\n\n\n");
		        		}
		        		 
		        	}
		        }
		        bw.flush();
		    }
	 
	  /**
	   * Same of parent Properties.class 
	   * @param bw
	   * @param comments
	   * @throws IOException
	   */
	  private static void writeComments(BufferedWriter bw, String comments)
		        throws IOException {
		        bw.write("#");
		        int len = comments.length();
		        int current = 0;
		        int last = 0;
		        char[] uu = new char[6];
		        uu[0] = '\\';
		        uu[1] = 'u';
		        while (current < len) {
		            char c = comments.charAt(current);
		            if (c > '\u00ff' || c == '\n' || c == '\r') {
		                if (last != current)
		                    bw.write(comments.substring(last, current));
		                if (c > '\u00ff') {
		                    uu[2] = toHex((c >> 12) & 0xf);
		                    uu[3] = toHex((c >>  8) & 0xf);
		                    uu[4] = toHex((c >>  4) & 0xf);
		                    uu[5] = toHex( c        & 0xf);
		                    bw.write(new String(uu));
		                } else {
		                    bw.newLine();
		                    if (c == '\r' &&
		                        current != len - 1 &&
		                        comments.charAt(current + 1) == '\n') {
		                        current++;
		                    }
		                    if (current == len - 1 ||
		                        (comments.charAt(current + 1) != '#' &&
		                        comments.charAt(current + 1) != '!'))
		                        bw.write("#");
		                }
		                last = current + 1;
		            }
		            current++;
		        }
		        if (last != current)
		            bw.write(comments.substring(last, current));
		        bw.newLine();
		    }
	  
	  
	  /**
	   *  Same of parent Properties.class 
	   * 
	   */
	  private String saveConvert(String theString,
              boolean escapeSpace,
              boolean escapeUnicode) {
		  int len = theString.length();
		  int bufLen = len * 2;
		  if (bufLen < 0) {
			  bufLen = Integer.MAX_VALUE;		  
		  }
		  StringBuffer outBuffer = new StringBuffer(bufLen);

		  for(int x=0; x<len; x++) {
			  char aChar = theString.charAt(x);
			  // Handle common case first, selecting largest block that
			  // avoids the specials below
			  if ((aChar > 61) && (aChar < 127)) {
				  if (aChar == '\\') {   
					  outBuffer.append('\\'); outBuffer.append('\\');  
					  continue;
				  }
				  outBuffer.append(aChar);
				  continue;
			  }
			  switch(aChar) {
			  case ' ':  
				  if (x == 0 || escapeSpace)       
					  outBuffer.append('\\');  
				  outBuffer.append(' ');   
				  break;
			  case '\t':outBuffer.append('\\'); outBuffer.append('t');     
			  break;
			  case '\n':outBuffer.append('\\'); outBuffer.append('n');     
			  break;
			  case '\r':outBuffer.append('\\'); outBuffer.append('r'); 
			  break;
			  case '\f':outBuffer.append('\\'); outBuffer.append('f');       
			  break;
			  case '=': // Fall through
			 // case ':': // Fall through
			  case '#': // Fall through
			  case '!':  
				  outBuffer.append('\\'); outBuffer.append(aChar);   
				  break;
			  default:  
				  if (((aChar < 0x0020) || (aChar > 0x007e)) & escapeUnicode ) { 
					  outBuffer.append('\\');  
					  outBuffer.append('u');    
					  outBuffer.append(toHex((aChar >> 12) & 0xF));    
					  outBuffer.append(toHex((aChar >>  8) & 0xF));   
					  outBuffer.append(toHex((aChar >>  4) & 0xF));     
					  outBuffer.append(toHex( aChar        & 0xF));  
				  } else {    
					  outBuffer.append(aChar); 
				  }
			  }
		  }
		  return outBuffer.toString();
	  }
	  
	  /**
	     *  Same of parent Properties.class 
	     * Convert a nibble to a hex character
	     * @param   nibble  the nibble to convert.
	     */
	    private static char toHex(int nibble) {
	        return hexDigit[(nibble & 0xF)];
	    }

	    /**
	     *  Same of parent Properties.class 
	     *  A table of hex digits 
	     * */
	    private static final char[] hexDigit = {
	        '0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'
	    };
	

	    public static void main(String[] args) throws IOException{
	    	EnhancedPropertiesWithSubGroups ep=new EnhancedPropertiesWithSubGroups();
	    	ep.addPropertyToGroupCategory("Data to analyse", "dataset_filepath", "", "File containing expression data");
	    	ep.addPropertyToSubGroupCategory("Testgroup1", "subgroup1", "key1", "value1", "");
	    	ep.addPropertyToSubGroupCategory("Testgroup1", "subgroup2", "key3", "value3", "test groups");
	    	ep.addPropertyToSubGroupCategory("Testgroup1", "subgroup1", "key2", "value2", "test groups");
	    	ep.addPropertyToGroupCategory("Testgroup1", "key4", "value4", "");
	    	ep.store(new FileWriter("/home/orocha/Imagens/testeProps.txt"), true);
	    }
	

}
