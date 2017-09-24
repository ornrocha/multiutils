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
package pt.ornrocha.swingutils.tables.subcomponents.copies;

import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.ToolTipManager;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import pt.ornrocha.collections.MTUCollectionsUtils;
import pt.ornrocha.swingutils.tables.models.GenericTableModel;
import pt.ornrocha.swingutils.tables.popupmenu.ITablePopupMenu;
import pt.ornrocha.swingutils.tables.popupmenu.SimpleTablePopupMenu;
import pt.ornrocha.swingutils.tables.subcomponents.renders.MultiLineTableCellRenderer;
import pt.ornrocha.swingutils.tables.subcomponents.utils.IHighLightFeatureTable;
import pt.ornrocha.swingutils.tables.subcomponents.utils.MultiLineTooltipsTransformer;

public class CopyOfMultiLineJTable extends JTable implements IHighLightFeatureTable{
	

	private static final long serialVersionUID = 1L;
	protected MultiLineTableCellRenderer renderer = new MultiLineTableCellRenderer();
	protected boolean showtooltips=false;
	protected int[] tooltipofcolumns;
	protected int[] applytocolumns;
    protected HashMap<Integer,TableCellRenderer> renders;
	
	
	public CopyOfMultiLineJTable(TableModel dm){
		super(dm);
	}
	
	public CopyOfMultiLineJTable(TableModel dm,int[]applyrendertocolumns, boolean showtooltips){
		super(dm);
		this.showtooltips=showtooltips;
		this.applytocolumns=applyrendertocolumns;
		setToolTipTime();
	}
	
	public CopyOfMultiLineJTable(TableModel dm,int[]applyrendertocolumns, boolean showtooltips, int[] tooltipofcolumns){
		super(dm);
		this.showtooltips=showtooltips;
		this.applytocolumns=applyrendertocolumns;
		this.tooltipofcolumns=tooltipofcolumns;
		setToolTipTime();
	}
	
	public CopyOfMultiLineJTable(TableModel dm, boolean showtooltips){
		super(dm);
		this.showtooltips=showtooltips;
		setToolTipTime();
	}
	
	public CopyOfMultiLineJTable(TableModel dm, boolean showtooltips, int[] tooltipofcolumns){
		super(dm);
		this.showtooltips=showtooltips;
		this.tooltipofcolumns=tooltipofcolumns;
		setToolTipTime();
	}
	
	
	public CopyOfMultiLineJTable(TableModel dm, TableColumnModel cm){
		super(dm,cm);
	}
	
	public CopyOfMultiLineJTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm){
		super(dm,cm,sm);
	}
	
	private void setToolTipTime(){
		ToolTipManager.sharedInstance().setInitialDelay(0);
        ToolTipManager.sharedInstance().setDismissDelay(80000);
	}
	
	@Override
	public void searchAndHighlightText(String text){
		this.renderer.setTextToHighlight(text);
		this.repaint();
	}
	
	@Override
	public void removeHighlightText() {
		this.renderer.removeTextHighlight();
		this.repaint();
	}
	
	public void setColumnWidth(double[] factorvalues){
		// example if equal two size columns set double array = {0.5,0.5}
		int ncolums=this.getColumnCount();

		if(ncolums==factorvalues.length){
			TableColumnModel model = getColumnModel();
			double factor =getPreferredScrollableViewportSize().getWidth();
			for (int i = 0; i < factorvalues.length; i++) {
				model.getColumn(i).setPreferredWidth((int) (factor*factorvalues[i]));
			}
		}
	}
	
	public void setAditionalColumnsRenders(HashMap<Integer, TableCellRenderer> renders){
		this.renders=renders;
	}
	
	
	public void setPopupMenu(ITablePopupMenu popup){
		this.addMouseListener(popup.getTableMouseListener());
		this.setComponentPopupMenu(popup.getPopupMenu());
	}
	
	
	@Override
    public TableCellRenderer getCellRenderer(int row, int column) {	
		if(applytocolumns!=null){
			if(MTUCollectionsUtils.arrayHaveIntValue(applytocolumns, column))
				return renderer;	
			else if(renders!=null){
				if(renders.containsKey(column)){
					
					return renders.get(column);
				}
				else{
					return super.getCellRenderer(row, column);
				}
			}
			else
				return super.getCellRenderer(row, column);
		}
		if(renders!=null){
			if(renders.containsKey(column))
				return renders.get(column);
		}
        return renderer;
    }
	

	
	@Override
	public String getToolTipText(MouseEvent e) {
		if(showtooltips){
           String tip = null;
           java.awt.Point p = e.getPoint();
           int rowIndex = rowAtPoint(p);
           int colIndex = columnAtPoint(p);
           
           int realColumnIndex =-1;
           
           if(tooltipofcolumns!=null){
        	   if(MTUCollectionsUtils.arrayHaveIntValue(tooltipofcolumns, colIndex))
        		   realColumnIndex=colIndex;
           }
           else
        	   realColumnIndex=colIndex;
           
           if(realColumnIndex >-1 && rowIndex>-1){
               realColumnIndex = convertColumnIndexToModel(colIndex);
               int realRowIndex=-1;
               
              TableModel model =null;
              RowSorter<?> rownmodel =getRowSorter();
               if(rownmodel!=null){
            	  model=(TableModel) rownmodel.getModel();
            	  realRowIndex=convertRowIndexToModel(rowIndex);
               }

               else{
            	  model = getModel();
            	  realRowIndex=rowIndex;
               }
               
        	   tip=getTootipString(model.getValueAt(realRowIndex,realColumnIndex));
           }
           
           if (tip == null)
               tip = getToolTipText(); 
           
          String out = MultiLineTooltipsTransformer.splitToolTip(tip);
          if(out!=null)
        	  return out;
          else 
        	  return getToolTipText();

		}
		
		return getToolTipText();
    }
	
	
	public String getTootipString(Object value){
		String res="";
		if(value instanceof List<?>){
			for (Object v : ((List<?>)value)) {
				res+=String.valueOf(v)+"\n";
			  }
			return res;
		}
		else if(value instanceof Set<?>){
			  for (Object v : ((Set<?>)value)) {
				res+=String.valueOf(v)+"\n";
			  }
			  return res;
		  }
		else
			return String.valueOf(value);
	}
	
	
	 public static void main(String s[]) {
		    JFrame frame = new JFrame("Popup Menu Example");
		    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    
		    String[] colnames ={"header1","header2"}; 
		    GenericTableModel tmodel = new GenericTableModel(colnames, false);
		    
		    Object[] values1 = new Object[2];
		    values1[0]="valor";
		    values1[1]="nao sei valor";
		    
		    tmodel.addRow(values1);
		    int[] apply =new int[]{1};
		    CopyOfMultiLineJTable table = new CopyOfMultiLineJTable(tmodel, apply,true);
		    SimpleTablePopupMenu p = new SimpleTablePopupMenu(table,true);
		    table.setPopupMenu(p);
		    
		    frame.setContentPane(table);
		    frame.setSize(300, 300);
		    frame.setVisible(true);
		  }

	
	

}
