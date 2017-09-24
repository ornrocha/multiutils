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

import java.awt.Component;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ImageRendererToBooleanOption extends DefaultTableCellRenderer{


	private static final long serialVersionUID = 1L;
	private ImageIcon trueicon;
	private ImageIcon falseicon;
	private JLabel label;
	
	public ImageRendererToBooleanOption(String truevalueimagepath, String falsevalueimagepath){
		this.trueicon=new ImageIcon(new ImageIcon(truevalueimagepath).getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));
		this.falseicon=new ImageIcon(new ImageIcon(falsevalueimagepath).getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));
	}
	
	public ImageRendererToBooleanOption(URL truevalueimagepath, URL falsevalueimagepath){
	    this(truevalueimagepath.getPath(), falsevalueimagepath.getPath());
	}
	
	
	 @Override
	 public Component getTableCellRendererComponent(JTable table, Object value,
             boolean isSelected, boolean hasFocus, int row, int column) {
		 

		 if(value instanceof Boolean){
			 label=new JLabel();
			 if((boolean) value){
				 label.setIcon(trueicon);
			 }
			 else
				 label.setIcon(falseicon); 
		 }
		if(label!=null){
			label.setHorizontalAlignment(JLabel.CENTER);
			return label;
		}
		return this;
	 }
	

}
