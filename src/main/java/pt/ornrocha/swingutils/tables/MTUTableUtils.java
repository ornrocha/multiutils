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
package pt.ornrocha.swingutils.tables;

import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import pt.ornrocha.swingutils.tables.subcomponents.utils.TableColumnAdjuster;


public class MTUTableUtils {
	
	
	  public static void adjustColumns(JTable table){
		  TableColumnAdjuster adj = new TableColumnAdjuster(table);
		  adj.adjustColumns();
	  }
       
	  
	  public static void setWidthAsPercentages(JTable table,double... percentages) {
		    final double factor = 10000;
		 
		    TableColumnModel model = table.getColumnModel();
		    for (int columnIndex = 0; columnIndex < percentages.length; columnIndex++) {
		        TableColumn column = model.getColumn(columnIndex);
		        column.setPreferredWidth((int) (percentages[columnIndex] * factor));
		    }
		}
	  
	  public static void scrollCellToView(JViewport viewport, JTable table,int rowIndex, int vColIndex) {

		    Rectangle rect = table.getCellRect(rowIndex, vColIndex, true);
		    Rectangle viewRect = viewport.getViewRect();

		    int x = viewRect.x;
		    int y = viewRect.y;

		    if (rect.x >= viewRect.x && rect.x <= (viewRect.x + viewRect.width - rect.width)){

		    } else if (rect.x < viewRect.x){
		        x = rect.x;
		    } else if (rect.x > (viewRect.x + viewRect.width - rect.width)) {
		        x = rect.x - viewRect.width + rect.width;
		    }

		    if (rect.y >= viewRect.y && rect.y <= (viewRect.y + viewRect.height - rect.height)){

		    } else if (rect.y < viewRect.y){
		        y = rect.y;
		    } else if (rect.y > (viewRect.y + viewRect.height - rect.height)){
		        y = rect.y - viewRect.height + rect.height;
		    }

		    viewport.setViewPosition(new Point(x,y));
		}
	 
	  
	  public static void setColumnWidths(JTable table, int... widths) {
	        for (int i = 0; i < widths.length; i++) {
	            if (i < table.getColumnModel().getColumnCount()) {
	            	table.getColumnModel().getColumn(i).setMaxWidth(widths[i]);
	            }
	            else break;
	        }
	    }


}
