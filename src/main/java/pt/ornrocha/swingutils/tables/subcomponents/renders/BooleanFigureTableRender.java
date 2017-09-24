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
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

public class BooleanFigureTableRender extends JLabel implements TableCellRenderer {


	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) {
		if(value!=null){
			if(value instanceof Boolean){
				ImageIcon icon =null;
				if((boolean)value)
					icon = new ImageIcon(getClass().getClassLoader().getResource("images/true.png"));
				else
					icon = new ImageIcon(getClass().getClassLoader().getResource("images/false.png"));
                
				Image image = icon.getImage(); // transform it
				Image newimg = image.getScaledInstance(20, 20,  java.awt.Image.SCALE_FAST); // scale it the smooth way 
                 icon = new ImageIcon(newimg);
                 setHorizontalAlignment(SwingConstants.CENTER);
				this.setIcon(icon);
				
				 if (isSelected) {
			          setForeground(table.getSelectionForeground());
			          setBackground(table.getSelectionBackground());
			      } else {
			          setForeground(table.getForeground());
			          setBackground(table.getBackground());
			      }
				 
				 if (hasFocus) {
			         setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
			         if (table.isCellEditable(row, column)) {
			            setForeground(UIManager.getColor("Table.focusCellForeground"));
			            setBackground(UIManager.getColor("Table.focusCellBackground"));
			         }
			     } else {
			        setBorder(new EmptyBorder(1, 2, 1, 2));
			       }
			}
		}
		
		return this;
	}

}
