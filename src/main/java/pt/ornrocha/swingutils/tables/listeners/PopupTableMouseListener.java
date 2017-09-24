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
package pt.ornrocha.swingutils.tables.listeners;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

public class PopupTableMouseListener extends MouseAdapter{

	private JTable table;
	private Point mousepos;
    
    public PopupTableMouseListener(JTable table) {
        this.table = table;
    }
    

    @Override
    public void mousePressed(MouseEvent event) {

    	mousepos = event.getPoint();
        int currentRow = table.rowAtPoint(mousepos);
        if(currentRow>-1)
           table.setRowSelectionInterval(currentRow, currentRow);

    }
    
    public Point getMousePointPosition(){
    	return mousepos;
    }
    
	
}
