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

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import pt.ornrocha.collections.MTUCollectionsUtils;
import pt.ornrocha.swingutils.tables.models.GenericTableModel;
import pt.ornrocha.swingutils.tables.subcomponents.renders.ButtonColumn;
import pt.ornrocha.swingutils.tables.subcomponents.renders.MultiLineTableCellRenderer;
import pt.ornrocha.swingutils.tables.subcomponents.renders.TableInTableCellRenderer;

public class TableInTableMultiLineTable extends MultiLineJTable{


	private static final long serialVersionUID = 1L;
	protected TableInTableCellRenderer tablerender;
	protected MultiLineTableCellRenderer insidemultilinerender;

	public TableInTableMultiLineTable(TableModel dm){
		super(dm);
	}
	
	public TableInTableMultiLineTable(TableModel dm,int[]applyrendertocolumns, boolean showtooltips){
		super(dm,applyrendertocolumns,showtooltips);
	}
	
	public TableInTableMultiLineTable(TableModel dm,int[]applyrendertocolumns, boolean showtooltips, int[] tooltipofcolumns){
		super(dm,applyrendertocolumns,showtooltips,tooltipofcolumns);
	}
	
	public TableInTableMultiLineTable(TableModel dm, boolean showtooltips){
		super(dm,showtooltips);
	}
	
	public TableInTableMultiLineTable(TableModel dm,int[] tooltipofcolumns){
		super(dm,tooltipofcolumns);
	}
	
	
	public TableInTableMultiLineTable(TableModel dm, TableColumnModel cm){
		super(dm,cm);
	}
	
	public TableInTableMultiLineTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm){
		super(dm,cm,sm);
	}
	
	@Override
	protected void changeDefaultRenders(){
		stringrenderer=new MultiLineTableCellRenderer();
		setDefaultRenderer(String.class, stringrenderer);
		tablerender=new TableInTableCellRenderer();
		setDefaultRenderer(JTable.class, tablerender);

	}
	
	
	
	
	
	@Override
	public void searchAndHighlightText(String text) {
		if(!text.isEmpty()){
		super.searchAndHighlightText(text);
		if(tablerender!=null)
		  tablerender.setTextToHighlight(text);
		
        this.repaint();
		}
	}

	@Override
	public void removeHighlightText() {
		super.removeHighlightText();
		if(tablerender!=null)
			tablerender.removeTextHighlight();
		
		this.repaint();
		
	}
	

	
	@Override
    public TableCellRenderer getCellRenderer(int row, int column) {	

		if(applytocolumns!=null){
			
			if(MTUCollectionsUtils.arrayHaveIntValue(applytocolumns, column))
				return tablerender;	
			
			else if(auxcolumnrenderers!=null){
				if(auxcolumnrenderers.containsKey(column)){
					return auxcolumnrenderers.get(column);
				}
				else{
					//return super.getDefaultRenderer(getColumnClass(column));
					return super.getCellRenderer(row, column);
				}
			}
			
			else{		
				return super.getCellRenderer(row, column);
				//return super.getDefaultRenderer(getColumnClass(column));
			}
		}
		if(auxcolumnrenderers!=null){
			if(auxcolumnrenderers.containsKey(column))
				return auxcolumnrenderers.get(column);
		}

		return tablerender;
	
    }
	
	
	
	public static void main(String s[]) {
	    JFrame frame = new JFrame("Popup Menu Example");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    ArrayList<Class<?>> edit = new ArrayList<>();
	    edit.add(MultiLineJTable.class);
	    edit.add(TableInTableMultiLineTable.class);
	    edit.add(ButtonColumn.class);
	    String[] colnames1 ={"header1","header2"};
	    
	    
	    GenericTableModel tmodel1 = new GenericTableModel(colnames1, false,edit);
	    
	    
	    Object[] values1 = new Object[2];
	    values1[0]="valor";
	    values1[1]="nao sei valor";
	    
	    
	    
	     tmodel1.addRow(values1);
	     
	    
	    MultiLineJTable table1 = new MultiLineJTable(tmodel1);
	    Action delete = new AbstractAction()
	    {
	        public void actionPerformed(ActionEvent e)
	        {
	            JTable table = (JTable)e.getSource();
	            int modelRow = Integer.valueOf( e.getActionCommand() );
	            System.out.println(modelRow);
	        }
	    };
	    ButtonColumn b = new ButtonColumn(table1,delete,1);
	    table1.setRenderToColumn(1, b);
	    
	    Object[] values2 = new Object[2];
	    values2[0]="valor rer";
	    values2[1]=b;
	    tmodel1.addRow(values2);
	    String[] colnames2={"header3","header4"}; 
	    GenericTableModel tmodel2 = new GenericTableModel(colnames2, false,edit);

	    TableInTableMultiLineTable table2 = new TableInTableMultiLineTable(tmodel2);
	    table2.setDefaultRenderer(ButtonColumn.class, new ButtonColumn(table1, delete, 1));
	   // table2.setDefaultRenderer(MultiLineJTable.class, new MultiLineTableCellRenderer());
	   

	    Object[] values3 = new Object[2];
	    values3[0]="valor";
	    values3[1]=table1;
	    tmodel2.addRow(values3);
	    
	    Object[] values4 = new Object[2];
	    values4[0]="valor rer";
	    values4[1]="da";
	    tmodel2.addRow(values4);
	    
	    
 	    
	    String[] colnames3={"principal1","principal2"}; 
	    GenericTableModel tmodel3 = new GenericTableModel(colnames3, false,edit);
	    
	    //MultiLineJTable table3 = new MultiLineJTable(tmodel3);
	  TableInTableMultiLineTable table3 = new TableInTableMultiLineTable(tmodel3);
	   /* HashMap<Integer,TableCellRenderer> auxrenders = new HashMap<>();
	    table3.setDefaultRenderer(JTable.class, new TableInTableCellRenderer());
	    auxrenders.put(1, new TableInTableCellRenderer());
	    table3.setAditionalColumnsRenders(auxrenders);*/
	 
	   /* Object[] values5 = new Object[2];
	    values5[0]="valor";
	    values5[1]=table2;
	    tmodel3.addRow(values5);
	    
	    Object[] values6 = new Object[2];
	    values6[0]="dsgfsdfg";
	    values6[1]="dsdg";
	    tmodel3.addRow(values6);
	    
	  
	    //SimpleTablePopupMenu p = new SimpleTablePopupMenu(table2,true);
	    //table2.setPopupMenu(p);
	    
	   // SimpleTablePopupMenu p = new SimpleTablePopupMenu(table,true);
	    //table.setPopupMenu(p); */
	    JScrollPane pan = new JScrollPane();
	    pan.getViewport().add(table2);
	    frame.setContentPane(pan);
	    frame.setSize(1000, 1000);
	    frame.setVisible(true);
	  }

}
