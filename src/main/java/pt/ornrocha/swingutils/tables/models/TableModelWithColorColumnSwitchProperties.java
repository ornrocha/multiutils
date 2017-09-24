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

import org.javatuples.Triplet;

public class TableModelWithColorColumnSwitchProperties extends GenericTableModel{

	private static final long serialVersionUID = 1L;
	
	
	// links String column to boolean column
	protected LinkedHashMap<Integer,Triplet<Integer, Color, Color>> colorswichscheme;
	
	
	
	public TableModelWithColorColumnSwitchProperties() {
		super();
		
	}
	
	public TableModelWithColorColumnSwitchProperties(String[] colNames) {
		super(colNames);
	}

	public TableModelWithColorColumnSwitchProperties(List<Object[]> listobj, String[] colNames, boolean isValueCellsEditable) {
		super(listobj, colNames, isValueCellsEditable);
		
	}

	public TableModelWithColorColumnSwitchProperties(String[] colNames, boolean isValueCellsEditable,ArrayList<Class<?>> editobjelem) {
		super(colNames, isValueCellsEditable, editobjelem);
	}

	public TableModelWithColorColumnSwitchProperties(String[] colNames, boolean isValueCellsEditable) {
		super(colNames, isValueCellsEditable);
	}

	public TableModelWithColorColumnSwitchProperties(String[] colNames, int[] editablecolumns, ArrayList<Class<?>> editobjelem) {
		super(colNames, editablecolumns, editobjelem);
	}

	public TableModelWithColorColumnSwitchProperties(String[] colNames, int[] editablecolumns) {
		super(colNames, editablecolumns);
	}
	
	
	public void addColumnColorSwitch(int stringcolumn, int boolcolumn, Color coloriftrue, Color coloriffalse) {
		if(colorswichscheme==null)
			colorswichscheme=new LinkedHashMap<>();
		
	     colorswichscheme.put(stringcolumn, new Triplet<>(boolcolumn, coloriftrue, coloriffalse));
		
	}
	
	


	public Color getRowColorAt(int row, int column){
	
		if(colorswichscheme!=null && colorswichscheme.containsKey(column)) {
			
			Triplet<Integer, Color, Color> colorlink=colorswichscheme.get(column);
			
			int cmdcolumn=colorlink.getValue0();
			if(getColumnClass(cmdcolumn).equals(Boolean.class)) {
				
				if(getColumnClass(column).equals(Boolean.class))
					return null;
				else {
					boolean state=(boolean) getValueAt(row, cmdcolumn);
					if(state) {
						Color iftrue=colorlink.getValue1();
						if(iftrue!=null)
							return iftrue;
						else
							return Color.WHITE;
					}
					else {
						Color iffalse=colorlink.getValue2();
						if(iffalse!=null)
							return iffalse;
						else
							return Color.WHITE; 
					}
				}	
			}
		}
		return Color.WHITE;
	}
	
	
	public ColorColumnCellRenderer getNewColorCellRendererInstance() {
		return new ColorColumnCellRenderer();
	}
	


	public static class ColorColumnCellRenderer extends DefaultTableCellRenderer {


		private static final long serialVersionUID = 1L;

		@Override
	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

			TableModelWithColorColumnSwitchProperties model =  (TableModelWithColorColumnSwitchProperties) table.getModel();
	        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	        
	        Color color=model.getRowColorAt(row, column);
	        if(color!=null)
	        	c.setBackground(color);

	        return c;
	    }
	}
	
	
	

}
