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
package pt.ornrocha.webutils.connectionutils.gui;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedHashSet;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class DownloadsProgressGUI extends JDialog implements PropertyChangeListener,ActionListener,IDownloadsProgressInterface{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JScrollPane jScrollPanedownloads;
	private JLabel jLabelprogress;
	private JProgressBar jProgressBartotal;
	private JPanel jPaneltotal;
    private JPanel showprogress;
    private PropertyChangeSupport changelst =null;
    private JButton jButtoncancelnow;
    private JButton jButtoncancel;
    private int ntotaldownloads=0;
    private int ndownloaded=0;
    private DownloadFutureProcesskiller processkiller=null;
    
   public static String FINISHEDPROGRESS="finishedprogress";
   public static String STARTPROGRESS="startprogress";
   public static String CANCELNEXTPROCESSES="cancelnextprocesses";
   public static String CANCELPROCESSESNOW="cancelprocessesnow";
	
	public DownloadsProgressGUI(){
		initGUI();	
		this.changelst=new PropertyChangeSupport(this);
		this.changelst.addPropertyChangeListener(this);
	}

	
	public PropertyChangeSupport getPropertyListenerSupport(){
		return this.changelst;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener l){
		this.changelst.addPropertyChangeListener(l);
	}
	
	public void setFutureProcesskiller(DownloadFutureProcesskiller killer){
		this.processkiller=killer;
	}
	
	
	private void initGUI() {
		try {
			{   GridBagLayout thisLayout = new GridBagLayout();
				thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
				thisLayout.rowHeights = new int[] {7, 7, 7, 7, 7, 7, 7};
				thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
				
				thisLayout.columnWidths = new int[] {7, 7, 7, 7, 7, 7};
				getContentPane().setLayout(thisLayout);
				{
					jScrollPanedownloads = new JScrollPane();
					getContentPane().add(jScrollPanedownloads, new GridBagConstraints(0, 1, 6, 5, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jScrollPanedownloads.setBorder(BorderFactory.createTitledBorder("Current Downloads"));
					jScrollPanedownloads.setPreferredSize(new Dimension(640,200));
				
					{
						showprogress=new JPanel();
						showprogress.setLayout(new BoxLayout(showprogress, BoxLayout.Y_AXIS));
						jScrollPanedownloads.setViewportView(showprogress);
						//showprogress.setPreferredSize(new Dimension(1000,500)
					}
				}
				{
					jPaneltotal = new JPanel();
					GridBagLayout jPaneltotalLayout = new GridBagLayout();
					getContentPane().add(jPaneltotal, new GridBagConstraints(0, 0, 6, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPaneltotal.setBorder(BorderFactory.createTitledBorder("Total Progress"));
					jPaneltotalLayout.rowWeights = new double[] {0.1, 0.1};
					jPaneltotalLayout.rowHeights = new int[] {7, 7};
					jPaneltotalLayout.columnWeights = new double[] {0.1};
					jPaneltotalLayout.columnWidths = new int[] {7};
					jPaneltotal.setLayout(jPaneltotalLayout);
					{
						jProgressBartotal = new JProgressBar(0,100);
						jPaneltotal.add(jProgressBartotal, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jProgressBartotal.setStringPainted(true);
					}
					{
						jLabelprogress = new JLabel();
						jPaneltotal.add(jLabelprogress, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jLabelprogress.setText("0 of 0");
					}

				}
				{
					jButtoncancel = new JButton();
					getContentPane().add(jButtoncancel, new GridBagConstraints(1, 6, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jButtoncancel.setText("Cancel all next");
					jButtoncancel.setActionCommand(CANCELNEXTPROCESSES);
					jButtoncancel.addActionListener(this);
				}
				{
					jButtoncancelnow = new JButton();
					getContentPane().add(jButtoncancelnow, new GridBagConstraints(3, 6, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jButtoncancelnow.setText("Cancel all now");
					jButtoncancelnow.setActionCommand(CANCELPROCESSESNOW);
					jButtoncancelnow.addActionListener(this);
				}
			}
			{
				this.setSize(580, 325);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	


	public void addProgressViewers(LinkedHashSet<DownloadProgressPanel> viewers){
		
		for (DownloadProgressPanel panel : viewers) {
			showprogress.add(panel);
			ntotaldownloads++;
			updateDownloadInfo();
		}
	}
	
	public void addProgressViewer(DownloadProgressPanel view){
		showprogress.add(view);
		ntotaldownloads++;
		updateDownloadInfo();
	}
	
	
   private void refreshActivePanels(){
    	int comp =showprogress.getComponentCount();
    	int xdim=0;
    	int ydim=0;
    	
    	for (int i = 0; i < comp; i++) {
			boolean visible = showprogress.getComponent(i).isVisible();
			if(visible){
				xdim+=showprogress.getComponent(i).getWidth();
				ydim+=showprogress.getComponent(i).getHeight();
			}
		}
    
    	jScrollPanedownloads.setPreferredSize(new Dimension((xdim+20),ydim+20));
    	jScrollPanedownloads.revalidate();
    	jScrollPanedownloads.repaint();

    }



	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String property = evt.getPropertyName();
		
		 if (property.equals(FINISHEDPROGRESS)) {
             ndownloaded++;
             updateDownloadInfo();
             updateProgressBar();
             refreshActivePanels();
            
		 }
		 else if(property.equals(STARTPROGRESS)){
	
			 refreshActivePanels();
		 }
		
	}
	
	private void updateDownloadInfo(){
		jLabelprogress.setText(ndownloaded+" of "+ntotaldownloads);
	}
	
	private void updateProgressBar(){
		int value = (int)(ndownloaded*100/ntotaldownloads);
		jProgressBartotal.setValue(value);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd =e.getActionCommand();
		
		if(cmd.equals(CANCELNEXTPROCESSES)){
			if(processkiller!=null)
				processkiller.run();
			jLabelprogress.setText("Cancelling...");
			changelst.firePropertyChange(CANCELNEXTPROCESSES, false, true);
		}
		else if(cmd.equals(CANCELPROCESSESNOW)){
			if(processkiller!=null)
				processkiller.run();
			changelst.firePropertyChange(CANCELPROCESSESNOW, false, true);
		}
		
	}
	

}
