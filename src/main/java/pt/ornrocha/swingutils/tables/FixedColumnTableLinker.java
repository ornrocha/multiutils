package pt.ornrocha.swingutils.tables;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class FixedColumnTableLinker implements ChangeListener, PropertyChangeListener{

	private JTable maintable;
	private JTable fixedtable;
	private JScrollPane mainscrollPane;
	private int fixedcolsize=100;
	
	public FixedColumnTableLinker(JTable maintable, JTable fixedtable, JScrollPane mainscrollPane) {
		
		this.maintable=maintable;
		this.fixedtable=fixedtable;
		this.mainscrollPane=mainscrollPane;
		doConnections();
	}
	
	
	private void doConnections() {
		
		fixedtable.setPreferredScrollableViewportSize(new Dimension(fixedcolsize, fixedcolsize));
		mainscrollPane.setRowHeaderView(fixedtable);
		mainscrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, fixedtable.getTableHeader());

		mainscrollPane.getRowHeader().addChangeListener( this );

	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
	
		if ("selectionModel".equals(evt.getPropertyName())){
			fixedtable.setSelectionModel( maintable.getSelectionModel() );
		}

		if ("model".equals(evt.getPropertyName())){
			fixedtable.setModel( maintable.getModel() );
		}
		
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JViewport viewport = (JViewport) e.getSource();
		mainscrollPane.getVerticalScrollBar().setValue(viewport.getViewPosition().y);
		
	}
	
	public static FixedColumnTableLinker link(JTable maintable, JTable fixedtable, JScrollPane mainscrollPane) {
		return new FixedColumnTableLinker(maintable, fixedtable, mainscrollPane);
	}

}
