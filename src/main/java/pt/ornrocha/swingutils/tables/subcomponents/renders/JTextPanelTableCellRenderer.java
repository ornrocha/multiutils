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
package pt.ornrocha.swingutils.tables.subcomponents.renders;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

public class JTextPanelTableCellRenderer extends JTextPane implements TableCellRenderer{


	private static final long serialVersionUID = 1L;
	protected String textforsearch=null;
	protected List<List<Integer>> rowColHeight = new ArrayList<List<Integer>>();
	
	
	
	
	
	
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		 if (isSelected) {
	          setForeground(table.getSelectionForeground());
	          setBackground(table.getSelectionBackground());
	      } else {
	          setForeground(table.getForeground());
	          setBackground(table.getBackground());
	      }
		 
		
	    setFont(table.getFont());
	  
	     if (hasFocus) {
	         setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
	         if (table.isCellEditable(row, column)) {
	            setForeground(UIManager.getColor("Table.focusCellForeground"));
	            setBackground(UIManager.getColor("Table.focusCellBackground"));
	         }
	     } else {
	        setBorder(new EmptyBorder(1, 2, 1, 2));
	       }
	    if (value != null) {
	   
	    	if(value instanceof List<?> || value instanceof Set<?>)
	    		setText(getString(value));
	    		
	    	else
	            setText(value.toString());
	      
	      
	    } else {
	       setText("");
	    }

	    
	    adjustRowHeight(table, row, column);
	    //setTextHighlight();
	    
	    return this;
	}
	
	
	private void adjustRowHeight(JTable table, int row, int column) {

		    int cWidth = table.getTableHeader().getColumnModel().getColumn(column).getWidth();
		    setSize(new Dimension(cWidth, 1000));
		  
		    int prefH = getPreferredSize().height;
		  
		    
		    while (rowColHeight.size() <= row) {
		        rowColHeight.add(new ArrayList<Integer>(column));
		    }
		    List<Integer> colHeights = rowColHeight.get(row);
		    while (colHeights.size() <= column) {
		      colHeights.add(0);
		     }
		   colHeights.set(column, prefH);
		   
		   int maxH = prefH;
		  
		   for (Integer colHeight : colHeights) {
		    if (colHeight > maxH) {
		      maxH = colHeight;
		    }
		  }
		    if (table.getRowHeight(row) != maxH) {
		      table.setRowHeight(row, maxH);
		    }

		    
		  }

		  private String getString(Object value){
			 // System.out.println("MultilineRender input value: "+value);
			  String res="";
			  int n=0;
			  if(value instanceof List<?>){
				  int size =((List<?>)value).size();
				  for (Object v : ((List<?>)value)) {
					res+=String.valueOf(v);
					if(n<size-1)
						res+="\n";
					n++;
				  }
			  }
			  else if(value instanceof Set<?>){
				  int size =((Set<?>)value).size();
				  for (Object v : ((Set<?>)value)) {
					res+=String.valueOf(v);
					if(n<size-1)
						res+="\n";
					n++;
				  }
			  }
			//  System.out.println("MultilineRender output value: "+res);
			  return res;
		  }


}
