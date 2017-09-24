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
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ComboBoxCellRenderer<E> extends JComboBox<E> implements TableCellRenderer{

	
	private static final long serialVersionUID = 1L;
	
	

	public ComboBoxCellRenderer() {
		super();
	}


	public ComboBoxCellRenderer(ComboBoxModel<E> aModel) {
		super(aModel);
	}


	public ComboBoxCellRenderer(E[] items) {
		super(items);
	}


	public ComboBoxCellRenderer(Vector<E> items) {
		super(items);
	}



	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,int row, int column) {

		if(isSelected) {
			setForeground(table.getSelectionForeground());
		    super.setBackground(table.getSelectionBackground());
		}
		else {
			setForeground(table.getForeground());
		    setBackground(table.getBackground());
		}
		
		setSelectedItem(value);
		
		return this;
	}
	
	
	

}
