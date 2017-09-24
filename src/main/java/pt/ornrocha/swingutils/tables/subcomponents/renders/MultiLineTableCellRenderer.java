package pt.ornrocha.swingutils.tables.subcomponents.renders;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;

import org.javatuples.Pair;

// Author: http://blog.botunge.dk/post/2009/10/09/JTable-multiline-cell-renderer.aspx

public class MultiLineTableCellRenderer extends JTextArea implements TableCellRenderer {

   private static final long serialVersionUID = 1L;
   protected List<List<Integer>> rowColHeight = new ArrayList<List<Integer>>();
   protected String textforsearch=null;
   protected Color backgroundcolor;

   public MultiLineTableCellRenderer() {
       setLineWrap(true);
       setWrapStyleWord(true);
       setOpaque(true);
   }
   
  public void setBackgroundColor(Color color){
	  this.backgroundcolor=color;
  }
  
  public void resetColors(){
	  this.backgroundcolor=null;
  }

 public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,int row, int column) {


	 if (isSelected) {
          setForeground(table.getSelectionForeground());
          if(backgroundcolor!=null)
        	  setBackground(backgroundcolor);
          else
             setBackground(table.getSelectionBackground());
      } else {
          setForeground(table.getForeground());
          setBackground(table.getBackground());
      }
	 
	
    setFont(table.getFont());
  
     if (hasFocus) {
         setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
         if (table.isCellEditable(row, column)) {
            setForeground(UIManager.getColor("Table.focusCellForeground"));
            setBackground(UIManager.getColor("Table.focusCellBackground"));
         }
     } else {
        setBorder(new EmptyBorder(1, 2, 1, 2));
       }
    if (value != null) {
   
    	if(value instanceof List<?> || value instanceof Set<?>)
    		setText(getString(value));
    		
    	else
            setText(value.toString());
      
      
    } else {
       setText("");
    }

    
    adjustRowHeight(table, row, column);
    setTextHighlight();
    
    return this;
  }

 
 
 
  public void setTextToHighlight(String str){
	  if(!str.isEmpty())
	   this.textforsearch=str;
  }
  
  public void removeTextHighlight(){
	  this.textforsearch=null;
  }
 
  private void setTextHighlight(){
	 String text = this.getText();
	 

	 if(textforsearch!=null){
		 
	   ArrayList<Pair<Integer, Integer>> pos =new ArrayList<>();	 
	   Pattern p = Pattern.compile(textforsearch);
	   Matcher m = p.matcher(text);

	   while (m.find()) {

	      int start=m.start();
		  int end =m.end();
		  pos.add(new Pair<Integer, Integer>(start, end));
	   }
	   
	   if(pos.size()>0){
		   
		   for (int i = 0; i < pos.size(); i++) {
			  Pair<Integer, Integer> pairpos = pos.get(i);
			  try {
				  
				  this.getHighlighter().addHighlight(pairpos.getValue0(), pairpos.getValue1(), new DefaultHighlighter.DefaultHighlightPainter(Color.RED));
			      } catch (BadLocationException e) {
				e.printStackTrace();
			   }
		   }   
	   }
	 }
	 else{
		 this.getHighlighter().removeAllHighlights();
	 }
  }

/**
 * Calculate the new preferred height for a given row, and sets the height on the table.
 */
private void adjustRowHeight(JTable table, int row, int column) {
  //The trick to get this to work properly is to set the width of the column to the
  //textarea. The reason for this is that getPreferredSize(), without a width tries
  //to place all the text in one line. By setting the size with the with of the column,
  //getPreferredSize() returnes the proper height which the row should have in
  //order to make room for the text.
    int cWidth = table.getTableHeader().getColumnModel().getColumn(column).getWidth();
    setSize(new Dimension(cWidth, 1000));
  
    int prefH = getPreferredSize().height;
  
    
    while (rowColHeight.size() <= row) {
        rowColHeight.add(new ArrayList<Integer>(column));
    }
    List<Integer> colHeights = rowColHeight.get(row);
    while (colHeights.size() <= column) {
      colHeights.add(0);
     }
   colHeights.set(column, prefH);
   
   int maxH = prefH;
  
   for (Integer colHeight : colHeights) {
    if (colHeight > maxH) {
      maxH = colHeight;
    }
  }
    if (table.getRowHeight(row) != maxH) {
      table.setRowHeight(row, maxH);
    }

    
  }

  private String getString(Object value){
	 // System.out.println("MultilineRender input value: "+value);
	  String res="";
	  int n=0;
	  if(value instanceof List<?>){
		  int size =((List<?>)value).size();
		  for (Object v : ((List<?>)value)) {
			res+=String.valueOf(v);
			if(n<size-1)
				res+="\n";
			n++;
		  }
	  }
	  else if(value instanceof Set<?>){
		  int size =((Set<?>)value).size();
		  for (Object v : ((Set<?>)value)) {
			res+=String.valueOf(v);
			if(n<size-1)
				res+="\n";
			n++;
		  }
	  }
	//  System.out.println("MultilineRender output value: "+res);
	  return res;
  }



}
