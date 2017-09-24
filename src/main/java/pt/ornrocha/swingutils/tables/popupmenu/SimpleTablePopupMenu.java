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
package pt.ornrocha.swingutils.tables.popupmenu;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import pt.ornrocha.stringutils.MTUStringUtils;
import pt.ornrocha.swingutils.tables.listeners.PopupTableMouseListener;
import pt.ornrocha.swingutils.tables.models.GenericTableModel;
import pt.ornrocha.swingutils.tables.subcomponents.utils.IHighLightFeatureTable;

public class SimpleTablePopupMenu implements ITablePopupMenu,ActionListener{

	
	protected PopupTableMouseListener mouseadp;
	protected JPopupMenu popupMenu;
	protected JTable table;
	protected JMenuItem menuitemremove;
	protected JMenu menucopy;
	protected JMenuItem menuitemcopyvalue;
	protected JMenuItem menuitemcopyrow;
	protected JMenu menusearch;
	protected JMenuItem menuitemsearchword;
	protected JMenuItem menuitemremovehighlight;
	protected boolean allowdelete=false;
	protected boolean canhighlight=false;
	
	
	public SimpleTablePopupMenu(JTable table){
		this.table=table;
		mouseadp=new PopupTableMouseListener(table);
		
		if(table instanceof IHighLightFeatureTable)
			this.canhighlight=true;
		setupPopupmenu();
	}
	
	public SimpleTablePopupMenu(JTable table, boolean allowdelete){
		this.allowdelete=allowdelete;
		this.table=table;
		mouseadp=new PopupTableMouseListener(table);
		
		if(table instanceof IHighLightFeatureTable){
			this.canhighlight=true;
		
		}
		setupPopupmenu();
	}
	
	protected void setupPopupmenu(){
		popupMenu=new JPopupMenu();
		if(allowdelete){
		   menuitemremove=new JMenuItem("Delete row");
		   menuitemremove.addActionListener(this);
		   popupMenu.add(menuitemremove);
		}
		
		menucopy = new JMenu("Copy");
		
		menuitemcopyvalue=new JMenuItem("Cell content");
		menuitemcopyvalue.addActionListener(this);
		
		menuitemcopyrow=new JMenuItem("Row content");
		menuitemcopyrow.addActionListener(this);
		
		menucopy.add(menuitemcopyvalue);
		menucopy.add(menuitemcopyrow);
		popupMenu.add(menucopy);
		
		if(canhighlight){
			menusearch=new JMenu("Search");
			menuitemsearchword=new JMenuItem("Word(s)");
			menuitemsearchword.addActionListener(this);
			
			menuitemremovehighlight=new JMenuItem("Remove highlights");
			menuitemremovehighlight.addActionListener(this);
			
			menusearch.add(menuitemsearchword);
			menusearch.add(menuitemremovehighlight);
			popupMenu.add(menusearch);
		}
		
		
		
		
		

	}
	
	
	@Override
	public MouseAdapter getTableMouseListener() {
		return mouseadp;
	}

	@Override
	public JPopupMenu getPopupMenu() {
		return popupMenu;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JMenuItem menu = (JMenuItem) e.getSource();
		
		if(menu == menuitemremove && allowdelete){
			int selectedRow = table.getSelectedRow();
            AbstractTableModel model = (AbstractTableModel) table.getModel();
           
            if(selectedRow>-1){
               if(model instanceof DefaultTableModel)
            	  ((DefaultTableModel)model).removeRow(selectedRow);
               else if(model instanceof GenericTableModel)
            	   ((GenericTableModel)model).removeRowAtPos(selectedRow);
            }

		}
		
		else if(menu ==menuitemcopyvalue){
	
			int row = table.getSelectedRow();
            int columnpos = table.columnAtPoint( mouseadp.getMousePointPosition() );
            
            if(row>-1 && columnpos>-1){
              int column=table.convertColumnIndexToModel(columnpos);
            
              Object value=table.getValueAt(row, column);
              String str = MTUStringUtils.getStringFromObject(value, ", ");
            
              StringSelection stringSelection = new StringSelection(str);
              Clipboard clipb = Toolkit.getDefaultToolkit().getSystemClipboard();
              clipb.setContents(stringSelection, null);
            }
  
		}
        else if(menu ==menuitemcopyrow){
        	int row = table.getSelectedRow();          

            if(row>-1){
            int ncolumns =table.getColumnCount();
            
            StringBuilder stb = new StringBuilder();
            
            for (int i = 0; i < ncolumns; i++) {
				Object obj =table.getValueAt(row, i);
				String str = MTUStringUtils.getStringFromObject(obj, ",");
				stb.append(str);
				if(i<ncolumns-1)
					stb.append("\t");
			}
            StringSelection stringSelection = new StringSelection(stb.toString());
            Clipboard clipb = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipb.setContents(stringSelection, null); 
           }
		}
        else if(menu==menuitemsearchword){

        	JFrame frame = (JFrame)table.getTopLevelAncestor();
        	ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/search.png"));
        	String find =(String) JOptionPane.showInputDialog(frame, "Find word/expression","",JOptionPane.QUESTION_MESSAGE,icon,null,null);
        	((IHighLightFeatureTable)table).searchAndHighlightText(find);
        }
        else if(menu==menuitemremovehighlight){
        	((IHighLightFeatureTable)table).removeHighlightText();
        }
		
	}
	
	

}
