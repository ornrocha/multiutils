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
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

import pt.ornrocha.swingutils.tables.subcomponents.utils.IHighLightFeatureTable;

public class TableInTableCellRenderer extends JScrollPane implements TableCellRenderer{
	

	private static final long serialVersionUID = 1L;
    protected IHighLightFeatureTable highlight=null;
    protected MultiLineTableCellRenderer containedrender=null;
    protected List<List<Integer>> rowColHeight = new ArrayList<List<Integer>>();


	public TableInTableCellRenderer(){
		
	}
	
	
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) {
 
		int prefHeight=0;
		JTable valuetable=null;
		
		if(value instanceof JTable){
			valuetable=((JTable)value);
					
			((JTable)value).setLayout(new FlowLayout(FlowLayout.LEFT));
			
			getViewport().add((JTable)value);

			prefHeight=((JTable)value).getHeight();
			if(((JTable)value) instanceof IHighLightFeatureTable){
				highlight=(IHighLightFeatureTable)value;
				TableCellRenderer render =highlight.getCellRenderer(row, column);
				if(render instanceof MultiLineTableCellRenderer)
					containedrender=(MultiLineTableCellRenderer) render;
			}
			
			
			if (isSelected) {
				valuetable.setBackground(table.getSelectionBackground());
			  }
		    else{
		    	valuetable.setBackground(table.getBackground());
			    valuetable.setForeground(table.getForeground());
		    }
			
			
			
		}
		else{
			return new MultiLineTableCellRenderer().getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		}
		
		
		   if (isSelected) {
			 getViewport().setBackground(table.getSelectionBackground());
			//setBackground(table.getSelectionBackground());
		    }else{
		       getViewport().setBackground(table.getBackground());
		       getViewport().setForeground(table.getForeground());
		
		    }
		 if (hasFocus == true) {
		      setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
		      if (table.isCellEditable(row, column)) {
		    	  getViewport().setForeground(UIManager.getColor("Table.focusCellForeground"));
		    	getViewport().setBackground(UIManager.getColor("Table.focusCellBackground"));
		    	 // setForeground(UIManager.getColor("Table.focusCellForeground"));
		    	  //setBackground(UIManager.getColor("Table.focusCellBackground"));
		      }
		    } else {
		    	setBorder(new EmptyBorder(1, 2, 1, 2));
		    }
		
		
		setSize(table.getColumnModel().getColumn(column).getWidth(),prefHeight);  
		
	    if (table.getRowHeight(row) < getPreferredSize().height) {  
	    	if(prefHeight>0)
	           table.setRowHeight(row, prefHeight+25);  
	    } 
		
		return this;
	}
	
	
	   public void setTextToHighlight(String str){
		  if(!str.isEmpty()){
			  if(containedrender!=null)
				  containedrender.setTextToHighlight(str);
		  }
	   }
	  
	  public void removeTextHighlight(){
		  if(containedrender!=null)
			  containedrender.removeTextHighlight();
	  }
	  
	 
	

}
