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
package pt.ornrocha.swingutils.tables.models;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

public class GenericTableModel extends AbstractTableModel{



private static final long serialVersionUID = 1L;

protected String[] columnNames = null;
protected ArrayList<Color> rowColors=null;
protected int[] editablecolumns=null;
protected ArrayList<Class<?>> editableclasselements;
protected ArrayList<Integer> rowsinitialized=new ArrayList<>();

/** The is value cells editable. */
protected boolean isValueCellsEditable=false;

/** The data. */
protected List<Object[]> data = null;

/** The editfromcolumn. */
private int editfromcolumn =0;

/**
 * Instantiates a new generic table viewer.
 *
 * @param colNames the col names
 * @param isValueCellsEditable the is value cells editable
 */
public GenericTableModel(){
	this.columnNames =new String[]{};
	
}

public GenericTableModel(String[] colNames){
	this.columnNames =colNames;
	
}

public GenericTableModel(String[] colNames,boolean isValueCellsEditable){
	this.columnNames = colNames;
	this.isValueCellsEditable = isValueCellsEditable;
}

public GenericTableModel(String[] colNames,boolean isValueCellsEditable, ArrayList<Class<?>> editobjelem){
	this.columnNames = colNames;
	this.isValueCellsEditable = isValueCellsEditable;
	this.editableclasselements=editobjelem;
}

public GenericTableModel(String[] colNames,int[] editablecolumns){
	this.columnNames = colNames;
	isValueCellsEditable=true;
	this.editablecolumns=editablecolumns;
}

public GenericTableModel(String[] colNames,int[] editablecolumns,ArrayList<Class<?>> editobjelem){
	this.columnNames = colNames;
	isValueCellsEditable=true;
	this.editablecolumns=editablecolumns;
	this.editableclasselements=editobjelem;
}

/**
 * Instantiates a new generic table viewer.
 *
 * @param listobj the listobj
 * @param colNames the col names
 * @param isValueCellsEditable the is value cells editable
 */
public GenericTableModel (List<Object[]> listobj,String[] colNames,boolean isValueCellsEditable){
    this.data=listobj;
	this.columnNames = colNames;
	this.isValueCellsEditable = isValueCellsEditable;
	
}


public void initializeEmptyTable(int nrows) {
	
	for (int i = 0; i < nrows; i++) {
		Object [] objlist=new Object [getColumnCount()];
		addRow(objlist);
	}
}

public void setColumnName(String[] columnnames){
	this.columnNames=columnnames;
}

/* (non-Javadoc)
 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
 */
@Override
public String getColumnName(int column) {
    return columnNames[column];
}

public String[] getColumnNames() {
	return columnNames;
}

/* (non-Javadoc)
 * @see javax.swing.table.TableModel#getRowCount()
 */
@Override
public int getRowCount() {
	if(data !=null)
	     return data.size();
	else return 0;
}

/* (non-Javadoc)
 * @see javax.swing.table.TableModel#getColumnCount()
 */
@Override
public int getColumnCount() {
	return columnNames.length;
}

/* (non-Javadoc)
 * @see javax.swing.table.TableModel#getValueAt(int, int)
 */
@Override
public Object getValueAt(int rowIndex, int columnIndex) {
	if(data!=null){
	  if(rowIndex>-1 && columnIndex>-1){
	    Object [] objlist= data.get(rowIndex);
	    Object obj = objlist[columnIndex];
	    return obj;
	  }
	}
	
	return null;
}

/**
 * Gets the row at.
 *
 * @param rowIndex the row index
 * @return the row at
 */
public Object[] getRowAt(int rowIndex){
		return data.get(rowIndex);
}


@Deprecated
public Color getRowColorAt(int rowIndex){
	if(rowColors!=null)
		return rowColors.get(rowIndex);
	else
		return Color.WHITE;
}




/* (non-Javadoc)
 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
 */
public void setValueAt(Object value, int rowIndex, int columnIndex){
	
	if(data!=null) {
		Object [] objlist= data.get(rowIndex);
	    objlist[columnIndex] = value;
	   // fireTableRowsUpdated(rowIndex,rowIndex);
	    fireTableCellUpdated(rowIndex, columnIndex);
		//fireTableDataChanged();
	}
}


public void setValueAt(Object value, int rowIndex, int columnIndex, Color color){
	if(data!=null){
	Object [] objlist= data.get(rowIndex);
	objlist[columnIndex] = value;
	checkRowColorArray();
	rowColors.set(rowIndex, color);
	fireTableDataChanged();
	}
}




/**
 * Initializetable.
 *
 * @param listobj the listobj
 */
public void initializetable(List<Object[]> listobj){
	this.data = listobj;
	fireTableDataChanged();		
}

 /**
	 * Insert data at row.
	 *
	 * @param values the values
	 * @param row the row
	 */
	public void insertDataAtRow(Object[] values, int row){ 
	 if(data.size()>0){
	 data.remove(row-1);
	 data.add(row-1, values);
	 fireTableDataChanged();
	 }
    }
	
	public void insertDataAtRow(Object[] values, int row, Color color){ 
		 if(data.size()>0){
		 data.remove(row-1);
		 data.add(row-1, values);
		 fireTableDataChanged();
		 }
	 }
 
 /**
	 * Insert list of data.
	 *
	 * @param listobj the listobj
	 */
	public void insertListOfData(List<Object[]> listobj){
	 if(data==null)
		 this.data= new ArrayList<Object[]>();
	 for(Object[] objects : listobj){
		 this.data.add(objects); 
	 }
	 fireTableDataChanged();
 }
 
 /**
	 * Insert single object data.
	 *
	 * @param obj the obj
	 */
	/*public void insertSingleObjectData(Object[] obj){
	 if(data==null)
		 this.data= new ArrayList<Object[]>();
	 this.data.add(obj);	 
	 fireTableDataChanged();
    }*/
 
 /**
	 * Adds the row.
	 *
	 * @param rowData the row data
	 */
	public void addRow(Object[] rowData) {
		 if(data==null)
			 this.data= new ArrayList<Object[]>();
		 this.data.add(rowData);	 
		 fireTableDataChanged();
     }
	
	public void addRow(Object[] rowData, Color color) {
		checkRowColorArray();
        addRow(rowData);
        rowColors.add(color);
    }
 
 
 /**
	 * Put all data.
	 *
	 * @param listobj the listobj
	 */
	public void putalldata(List<Object[]> listobj){
	 int n = 0;
	 for (Object[] objects : listobj) {
		 insertDataAtRow(objects, n);
		n++;
	}
	 
 }
 
/**
* Add rows.
*
* @param nrows the nrows
*/
public void addrows(int nrows){
	  
	 for (int i = 0; i < nrows; i++) {
	  Object[] valuesarray = new Object [columnNames.length];
	  for (int j = 0; j < valuesarray.length; j++) {
		  valuesarray[j]="";
	  }
	  data.add(valuesarray); 
	 }
	 isValueCellsEditable=true;
	 fireTableDataChanged();  
  }

/**
* Adds the row at pos.
*
* @param row the row
*/
public void addRowAtPos(int row){ 
	 if(data.size()>0){
      Object[] valuesarray = new Object [columnNames.length];
      for (int j = 0; j < valuesarray.length; j++) {
			  valuesarray[j]="";
		  }
	 data.add(row-1, valuesarray);
	 fireTableDataChanged();
	 }
}



/**
* Removes the rows at positions (x).
*
* @param indices the indices
*/
public void removeRowsAtPos(int[] indices) {

 int a=0;
  for (int j = 0; j < indices.length; j++) {
	   int b =indices[j]-a; 
	   data.remove(b);
	   if(rowColors!=null)
		   rowColors.remove(b);
	   a++;
	   fireTableRowsDeleted(b, b);
}

  }
  


/**
* Removes the row at position (x).
*
* @param n the n
*/
public void removeRowAtPos(int n) {
       if(data.size()>0){
    	data.remove(n);
    	 if(rowColors!=null)
  		   rowColors.remove(n);
		fireTableRowsDeleted(n, n);
       }
	} 


/**
* Removes the row.
*/
public void removeRow(){
  if (data.size()>0){
	  removeRowAtPos(data.size()-1);
  }
}


/**
* Sets the column names.
*
* @param names the new column names
*/
public void setColumnNames(String[] names){
   this.columnNames = names;
  }

/**
* Gets the column names size.
*
* @return the column names size
*/
public int getColumnNamesSize(){
   return this.columnNames.length;
  }

/**
* Reset table.
*/
public void resetTable(){
    if(data!=null){
    	int nrows = data.size();
    	if(nrows>0){
    		for (int i = 0; i < nrows; i++) {
    			removeRow();
    		}
    	}
    }
   this.rowColors=null;
}

/* (non-Javadoc)
* @see javax.swing.table.AbstractTableModel#getColumnClass(int)
*/
@Override
public Class<?> getColumnClass(int column)
{
   for (int row = 0; row < getRowCount(); row++)
   {
       Object o = getValueAt(row, column);

       if (o != null)
           return o.getClass();
   }

   return Object.class;
}

/* (non-Javadoc)
* @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
*/
@Override
public boolean isCellEditable(int row, int col) {
	  Object val=getValueAt(row, col);
	  
	  if(editableclasselements!=null){
	    if(editableclasselements.contains(val.getClass())){
		  return true;
	    }
	  }
	
	  if(isValueCellsEditable){
		 if(editablecolumns!=null){
			 return editableColumn(col);
		 }
		 else if (col > editfromcolumn) 
            	return true;
		 else   
            return false;
	 }
	 return false;
    }

 private boolean editableColumn(int n){
	 if(editablecolumns!=null)
		 for (int i = 0; i < editablecolumns.length; i++) {
			 if(editablecolumns[i]==n)
				 return true;
		 }
	 return false;
 }

/**
* Sets the editfromcolumn.
*
* @param n the new editfromcolumn
*/
public void seteditfromcolumn(int n){
	this.editfromcolumn = n;
	isValueCellsEditable=true;
}


public void setEditableColumnPositions(int[] columns){
	this.editablecolumns=columns;
	isValueCellsEditable=true;
}

public void blockEditable(){
	isValueCellsEditable=false;
	this.editablecolumns=null;
	this.editfromcolumn =0;
}

/**
* Move element up.
*
* @param index the index
*/
public void moveElementUp(int index){
   if(index>0 && data.size()>1)
		 Collections.swap(data, index, index-1);
   fireTableDataChanged();
}

/**
* Move element down.
*
* @param index the index
*/
public void moveElementDown(int index){
   if(index>=0 && index+1<data.size() && data.size()>1)
		 Collections.swap(data, index, index+1);
   fireTableDataChanged();
}

private void checkRowColorArray(){
	if(rowColors==null)
		rowColors=new ArrayList<>();
}



public static class TableColorCellRenderer extends DefaultTableCellRenderer {


	private static final long serialVersionUID = 1L;

	@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

    	GenericTableModel model = (GenericTableModel) table.getModel();
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        c.setBackground(model.getRowColorAt(row));

        return c;
    }
}






}
