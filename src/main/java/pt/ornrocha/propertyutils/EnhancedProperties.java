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
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import pt.ornrocha.stringutils.MTUStringUtils;

public class EnhancedProperties extends Properties{


	private static final long serialVersionUID = 1L;
	
	
	
	protected String currentselectedkey=null;
	protected HashMap<String, String> subkeymap=new HashMap<>();
	protected HashMap<String, String> keycommentmap=new HashMap<>();
	protected ArrayList<String> inputkeyorder=new ArrayList<>();
	protected String commentssource;
	//private LinkedHashMap<String, V>

	public synchronized void addPropertyKey(String key){
		 this.setProperty(key, "empty");
		 inputkeyorder.add(key);
	 }
	
	
	public synchronized void addPropertyKey(String key,String value){
		 if(value!=null && !value.isEmpty()){
			 this.setProperty(key, value);
			 inputkeyorder.add(key);
		 }
		 else
		     addPropertyKey(key);
	 }
	
	public synchronized void addPropertyKey(String key,String value, String comment){
		 addPropertyKey(key, value);
		 if(comment!=null && !comment.isEmpty())
			 keycommentmap.put(key, comment);
	 }
	
	public synchronized void addPropertyKey(String key,String value, String comment, String subkey){
		 addPropertyKey(key, value,comment);
		 if(subkey!=null && !subkey.isEmpty())
			 subkeymap.put(key, subkey);
	 }
	
	
/*	public synchronized AlgorithmProperties selectPropertyKey(String key){
		this.currentselectedkey=key;
		return this;
	}*/
	
	public synchronized void changeValueTo(Object value){
		if(currentselectedkey!=null){
			replace(currentselectedkey, value);
		}
	}
	
	public void setCommentsSource(String source){
		this.commentssource=source;
	}
	
	@Override
	public String getProperty(String key) {
        Object oval = super.get(key);
        String sval = (oval instanceof String) ? (String)oval : null;
        if(sval!=null && sval.equals("empty"))
        	return null;
        return ((sval == null) && (defaults != null)) ? defaults.getProperty(key) : sval;
    }
	
	public void addProperties(String[] keys, String[] defaultvalues) throws IOException{
		if(keys.length!=defaultvalues.length)
			throw new IOException("keys array must have the same dimension of defaultvalues array");
		else{
			for (int i = 0; i < keys.length; i++) {
				addPropertyKey(keys[i], defaultvalues[i]);
			}
		}
	}
	
	public void addProperties(String[] keys,String[] defaultvalues, String[] comments) throws IOException{
		if(keys.length!=defaultvalues.length || keys.length!=comments.length)
			throw new IOException("Input arrays must have the same dimension");
		else{
			for (int i = 0; i < keys.length; i++) {
				addPropertyKey(keys[i], defaultvalues[i], comments[i]);
			}
		}
	}
	
	public void addProperties(String[] keys,String[] defaultvalues, String[] comments, String commentssource) throws IOException{
		if(keys.length!=defaultvalues.length || keys.length!=comments.length)
			throw new IOException("Input arrays must have the same dimension");
		else{
			for (int i = 0; i < keys.length; i++) {
				addPropertyKey(keys[i], defaultvalues[i], comments[i]);
			}
			
			if(commentssource!=null)
				setCommentsSource(commentssource);
		}
	}
	
	public void addProperties(String[] keys,String[] defaultvalues,  String[] comments, String[] subkeys) throws IOException{
		if(keys.length!=defaultvalues.length || 
				keys.length!=comments.length || 
				keys.length!=subkeys.length)
			throw new IOException("Input arrays must have the same dimension");
		else{
			for (int i = 0; i < keys.length; i++) {
				addPropertyKey(keys[i], defaultvalues[i],comments[i], subkeys[i]);
			}
		}
	}
	
	public ArrayList<String> getOrderedInputOrderKeys(){
		return inputkeyorder;
	}
	
	public void store(Writer writer, boolean writecomments)
	        throws IOException
	    {
	        store0((writer instanceof BufferedWriter)?(BufferedWriter)writer
	                                                 : new BufferedWriter(writer),
	               writecomments,
	               false);
	    }
	
	
	 private void store0(BufferedWriter bw, boolean writecomments, boolean escUnicode)
		        throws IOException
		    {
		        if (commentssource != null) {
		        	bw.write(MTUStringUtils.getRepeatedWordXTimes("#", commentssource.length()+1));
		        	bw.newLine();
		            writeComments(bw, commentssource);
		            bw.write(MTUStringUtils.getRepeatedWordXTimes("#", commentssource.length()+1));
		        	bw.newLine();
		        	bw.newLine();
		        }
		       // bw.write("#" + new Date().toString());
		        //bw.newLine();
		        synchronized (this) {
		        	if(inputkeyorder.size()>0)
		        	for (int i = 0; i < inputkeyorder.size(); i++) {
						String key=inputkeyorder.get(i);
						
						String comment=null;
						if(writecomments && keycommentmap.containsKey(key))
							comment=keycommentmap.get(key);
						
						String val=(String)get(key);
						key = saveConvert(key, true, escUnicode);
						if(val.equals("empty"))
							val="";
						val = saveConvert(val, false, escUnicode);
						 
						if(comment!=null){
							writeComments(bw,comment);
							//bw.newLine();
						}
						bw.write(key + "=" + val);
			            bw.newLine();
			            if(comment!=null)
			            	bw.newLine();
					}
		        	else{
		            for (Enumeration<?> e = keys(); e.hasMoreElements();) {
		                String key = (String)e.nextElement();
		                String val = (String)get(key);
		                key = saveConvert(key, true, escUnicode);
		                // No need to escape embedded and trailing spaces for value, hence
		                 //* pass false to flag.
		                 
		                val = saveConvert(val, false, escUnicode);
		                bw.write(key + "=" + val);
		                bw.newLine();
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
			  case ':': // Fall through
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
	

}
