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

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class TwoTableColumnSelectionListener implements TableModelListener{

	
	protected JTable table1;
	protected JTable table2;
	protected boolean enabled=false;
	protected int table1cmdcolumn=0;
	protected int table2cmdcolumn=0;
	
	public TwoTableColumnSelectionListener(JTable table1,JTable table2) {
        this.table1=table1;
        this.table2=table2;
        table1.getModel().addTableModelListener(this);
        table2.getModel().addTableModelListener(this);
	}
	
	public TwoTableColumnSelectionListener(JTable table1,int table1boolcolumn,JTable table2, int table2boolcolumn) {
		this(table1,table2);
		table1cmdcolumn=table1boolcolumn;
		table2cmdcolumn=table2boolcolumn;
	}
	
	
	public void enable() {
		this.enabled=true;
	}
	
	public void disable() {
		this.enabled=false;
	}
	

	@Override
	public void tableChanged(TableModelEvent e) {
		
		TableModel model = (TableModel)e.getSource();

		if(enabled) {
			int changedrow=e.getFirstRow();
			if(model.equals(table1.getModel()) && table1.getSelectedRow()==changedrow) {
				boolean t1val=(boolean) model.getValueAt(changedrow, table1cmdcolumn);
				table2.setValueAt(!t1val, changedrow, table2cmdcolumn);
				table1.clearSelection();
				table1.getSelectionModel().setAnchorSelectionIndex(-1);
				table1.getSelectionModel().setLeadSelectionIndex(-1); 
				table2.updateUI();
			}
			else if(model.equals(table2.getModel()) && table2.getSelectedRow()==changedrow) {
				boolean t2val=(boolean) model.getValueAt(changedrow, table2cmdcolumn);
				table1.setValueAt(!t2val, changedrow, table1cmdcolumn);
				table2.clearSelection();
				table2.getSelectionModel().setAnchorSelectionIndex(-1);
				table2.getSelectionModel().setLeadSelectionIndex(-1);
				table1.updateUI();
			}
			
		}
	}
	
	
	

	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
