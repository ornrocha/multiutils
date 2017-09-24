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

import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.ToolTipManager;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.javatuples.Pair;

import pt.ornrocha.collections.MTUCollectionsUtils;
import pt.ornrocha.swingutils.tables.popupmenu.ITablePopupMenu;
import pt.ornrocha.swingutils.tables.subcomponents.utils.MultiLineTooltipsTransformer;

public class JTableMod extends JTable{
	

	private static final long serialVersionUID = 1L;
	protected boolean showtooltips=false;
	protected int[] tooltipofcolumns;
	protected int[] applytocolumns;
    protected LinkedHashMap<Integer,TableCellRenderer> auxColumnRenders;
    protected LinkedHashMap<Pair<Integer, Integer>, TableCellRenderer> auxRowColumnRenders;
    
    
    public JTableMod(TableModel dm){
		super(dm);
	}
	
	public JTableMod(TableModel dm,boolean showtooltips){
		super(dm);
		this.showtooltips=showtooltips;
		setToolTipTime();
	}
	
	public JTableMod(TableModel dm, int[] tooltipofcolumns){
		super(dm);
		this.showtooltips=true;
		this.tooltipofcolumns=tooltipofcolumns;
		setToolTipTime();
	}
	
    
	public JTableMod(TableModel dm, TableColumnModel cm){
		super(dm,cm);
	}
	
	public JTableMod(TableModel dm, TableColumnModel cm, ListSelectionModel sm){
		super(dm,cm,sm);
	}
	
	protected void setToolTipTime(){
		ToolTipManager.sharedInstance().setInitialDelay(0);
        ToolTipManager.sharedInstance().setDismissDelay(80000);
	}
	
	public void setAditionalColumnsRenders(LinkedHashMap<Integer, TableCellRenderer> renders){
		this.auxColumnRenders=renders;
	}
	
	public void setRenderToColumn(int colum,TableCellRenderer render){
		if(auxColumnRenders==null)
			auxColumnRenders=new LinkedHashMap<Integer, TableCellRenderer>();
		
		auxColumnRenders.put(colum, getColumnRenderIfPresent(render));
	}
	
	public void setRenderToRowColumnPair(int row, int column, TableCellRenderer render){
		if(auxRowColumnRenders==null)
			auxRowColumnRenders=new LinkedHashMap<>();
		    Pair<Integer, Integer> pair = new Pair<Integer, Integer>(row, column);
			auxRowColumnRenders.put(pair, render);
	}
	
	
	public void setPopupMenu(ITablePopupMenu popup){
		addMouseListener(popup.getTableMouseListener());
		setComponentPopupMenu(popup.getPopupMenu());
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
	
	
	
	
	@Override
    public TableCellRenderer getCellRenderer(int row, int column) {	
        
		if(auxRowColumnRenders!=null){
			for (Pair<Integer, Integer> pair : auxRowColumnRenders.keySet()) {
				if(MTUCollectionsUtils.tupleContainPair(row, column, pair)){
					return auxRowColumnRenders.get(pair);
				}
			}
		}
		
		else if(auxColumnRenders!=null){
			if(auxColumnRenders.containsKey(column))
				return auxColumnRenders.get(column);
		   }

		return super.getCellRenderer(row, column);

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
	
	
	
	private TableCellRenderer getColumnRenderIfPresent(TableCellRenderer inputrender){
		if(auxColumnRenders!=null){
		  LinkedHashSet<TableCellRenderer> renders = new LinkedHashSet<>(auxColumnRenders.values());
		  for (TableCellRenderer render : renders) {
			  if(render.getClass().equals(inputrender.getClass()))
				  return render;
		   }
		  
		}
		return inputrender;
		
	}

	

}
