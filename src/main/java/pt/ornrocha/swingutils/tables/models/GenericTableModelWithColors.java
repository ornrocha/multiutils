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
package pt.ornrocha.swingutils.tables.models;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class GenericTableModelWithColors extends GenericTableModel{


	private static final long serialVersionUID = 1L;
	
	protected LinkedHashMap<Integer, LinkedHashMap<Integer,Color>> mapcoloratrowandcolumn;

	public GenericTableModelWithColors() {
		super();
	}

	public GenericTableModelWithColors(List<Object[]> listobj, String[] colNames, boolean isValueCellsEditable) {
		super(listobj, colNames, isValueCellsEditable);
	}

	public GenericTableModelWithColors(String[] colNames, boolean isValueCellsEditable,ArrayList<Class<?>> editobjelem) {
		super(colNames, isValueCellsEditable, editobjelem);
	}

	public GenericTableModelWithColors(String[] colNames, boolean isValueCellsEditable) {
		super(colNames, isValueCellsEditable);
	}

	public GenericTableModelWithColors(String[] colNames, int[] editablecolumns, ArrayList<Class<?>> editobjelem) {
		super(colNames, editablecolumns, editobjelem);
	}

	public GenericTableModelWithColors(String[] colNames, int[] editablecolumns) {
		super(colNames, editablecolumns);
	}

	public GenericTableModelWithColors(String[] colNames) {
		super(colNames);
	}
	
	
	public void setColorToRowColumn(int rowidx, int columnidx, Color color) {
		if (mapcoloratrowandcolumn==null)
			mapcoloratrowandcolumn=new LinkedHashMap<>();
		
		if(mapcoloratrowandcolumn.containsKey(rowidx)) {
			mapcoloratrowandcolumn.get(rowidx).put(columnidx, color);
		}
		else {
			LinkedHashMap<Integer,Color> rowcolors=new LinkedHashMap<>(getColumnCount());
			
			for (int i = 0; i < rowcolors.size(); i++) {
				rowcolors.put(i, Color.white);
			}
			
			rowcolors.put(columnidx, color);
			
			mapcoloratrowandcolumn.put(rowidx, rowcolors);
		}
			
	}
	
	
	public Color getCellColorAt(int rowindex, int columnindex) {
		if(mapcoloratrowandcolumn!=null && mapcoloratrowandcolumn.containsKey(rowindex) && mapcoloratrowandcolumn.get(rowindex).containsKey(columnindex))
			return mapcoloratrowandcolumn.get(rowindex).get(columnindex);
		return null;
	}
	
	
	public static ColorCellRenderer getColorCellRender() {
		return new ColorCellRenderer();
	}
	
	
	public static class ColorCellRenderer extends DefaultTableCellRenderer {


		private static final long serialVersionUID = 1L;

		@Override
	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

			GenericTableModelWithColors model =  (GenericTableModelWithColors) table.getModel();
	        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	        
	        Color color=model.getCellColorAt(row, column);
	        if(color!=null)
	        	c.setBackground(color);

	        return c;
	    }
	}

}
