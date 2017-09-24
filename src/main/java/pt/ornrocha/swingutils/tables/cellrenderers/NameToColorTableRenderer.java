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
package pt.ornrocha.swingutils.tables.cellrenderers;

import java.awt.Color;
import java.awt.Component;
import java.util.LinkedHashMap;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class NameToColorTableRenderer extends DefaultTableCellRenderer {


	private static final long serialVersionUID = 1L;
	
	private LinkedHashMap<String, Color> mapnamecolor;
	
	public NameToColorTableRenderer() {};
	
	public NameToColorTableRenderer(LinkedHashMap<String, Color> mapnamecolor) {
		this.mapnamecolor=mapnamecolor;
	}

	public void appendNameToColorLink(String name, Color color) {
		if(mapnamecolor==null)
			mapnamecolor=new LinkedHashMap<>();
		mapnamecolor.put(name, color);
	}
	
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		//setBackground(null);
		Component tableCellRendererComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
		if(value instanceof String) {
		    String val = (String) value;	
			
		    if(mapnamecolor!=null && mapnamecolor.containsKey(val))
		    	setBackground(mapnamecolor.get(val));
			
		}
		return tableCellRendererComponent;
	}
	
	
	
}
