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
package pt.ornrocha.swingutils.tables.subcomponents.utils;

import java.util.ArrayList;
import java.util.List;


public class MultiLineTooltipsTransformer {
    private static int TOOLTIP_MAX_SIZE = 75;
    private static final int SPACE = 10;

    public static String splitToolTip(String tip)
    {
        return splitToolTip(tip,TOOLTIP_MAX_SIZE);
    }
    public static String splitToolTip(String tip,int length)
    {
    	if(tip==null || tip.isEmpty()){
    		return null;
    	}
    	else{	
          if(tip.length()<=length + SPACE )
          {
            return tip;
          }
         else{
        	
        	List<String>  parts = new ArrayList<>();
        	int currentbackpos = 0;
        	int currentfrontpos=length+SPACE;
        	
        	while(currentbackpos<=tip.length())
            {
                
        		if(currentfrontpos>tip.length()){
        			parts.add(tip.substring(currentbackpos));
        			break;
        		}
        		else{
        		String line =tip.substring(currentbackpos, currentfrontpos);

        		int lastlinespace =line.lastIndexOf(" ");
        		
        		if(lastlinespace<=currentfrontpos){
        			
        			parts.add(tip.substring(currentbackpos, currentbackpos+lastlinespace));
        			currentbackpos+=lastlinespace;
        			
        		}
        		else{
        			String linereverse =new StringBuilder(line).reverse().toString();
        			int initlinespace=linereverse.indexOf(" ");
        			
        			parts.add(tip.substring(currentbackpos, currentbackpos+currentfrontpos-initlinespace));
        			currentbackpos+=currentfrontpos-initlinespace;
        		}
        	 }
        		
        		currentfrontpos+=length+SPACE;
        		
            }
        	
        	
        	
        	StringBuilder  sb = new StringBuilder("<html>");

            for(int i=0;i<parts.size()-1;i++){
                sb.append(parts.get(i)+"<br>");
            }
            
            sb.append(parts.get(parts.size()-1));
            sb.append(("</html>"));
            return sb.toString();
        	
          }
    	}
       
    }
    

    public static void main(String args[]){
    	String t = "We investigated the effect that intragenic s54 binding sites have on transcripton elongation. In an rpoN deletion we have over-expressed rpoN from a plasmid (with vector only contol) and then looked at changes in transcription elongation around intragenic s54 binding sites by RNA-seq. We are also looking at changes in transcription initiation/elongation at sites of sigma factor overlap.";
    	System.out.println(MultiLineTooltipsTransformer.splitToolTip(t,100));
    }
}