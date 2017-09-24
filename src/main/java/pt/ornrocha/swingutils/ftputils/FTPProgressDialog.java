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
package pt.ornrocha.swingutils.ftputils;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import pt.ornrocha.webutils.connectionutils.gui.DownloadProgressPanel;
import pt.ornrocha.webutils.ftputils.MTUFtpUtils;

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
public class FTPProgressDialog extends JDialog implements PropertyChangeListener{


	private static final long serialVersionUID = 1L;
	private JProgressBar jProgressBarpartial;
	private JPanel jPanelcurentfile;
	private JLabel jLabelfilesizevalue;
	private JLabel jLabelfilesize;
	private JLabel jLabelfilenamelabel;
	private JLabel jLabelfilename;
	private JProgressBar jProgressBartotal;

	
	public FTPProgressDialog(){
		initGUI();	
	}
	
	
	
	
	
	
	private void initGUI() {
	try {
		{   GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
			thisLayout.rowHeights = new int[] {7, 7, 7, 7, 7, 7};
			thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
			
			thisLayout.columnWidths = new int[] {7, 7, 7, 7, 7, 7};
			getContentPane().setLayout(thisLayout);
			{
				jProgressBartotal = new JProgressBar(0, 100);
				getContentPane().add(jProgressBartotal, new GridBagConstraints(0, 1, 6, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jProgressBartotal.setBorder(BorderFactory.createTitledBorder("Total Progress"));
			}
			{
				jPanelcurentfile = new JPanel();
				GridBagLayout jPanelcurentfileLayout = new GridBagLayout();
				getContentPane().add(jPanelcurentfile, new GridBagConstraints(0, 3, 6, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelcurentfileLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
				jPanelcurentfileLayout.rowHeights = new int[] {7, 7, 7};
				jPanelcurentfileLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
				jPanelcurentfileLayout.columnWidths = new int[] {7, 7, 7, 7, 7, 7};
				jPanelcurentfile.setLayout(jPanelcurentfileLayout);
				jPanelcurentfile.setBorder(BorderFactory.createTitledBorder("Current file progress"));
				{
					jProgressBarpartial = new JProgressBar(0, 100);
					jPanelcurentfile.add(jProgressBarpartial, new GridBagConstraints(0, 2, 6, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jProgressBarpartial.setStringPainted(true);
				}
				{
					jLabelfilename = new JLabel();
					jPanelcurentfile.add(jLabelfilename, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabelfilename.setText("Filename: ");
				}
				{
					jLabelfilenamelabel = new JLabel();
					jPanelcurentfile.add(jLabelfilenamelabel, new GridBagConstraints(1, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					jLabelfilenamelabel.setText("name ");
				}
				{
					jLabelfilesize = new JLabel();
					jPanelcurentfile.add(jLabelfilesize, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabelfilesize.setText("File size:  ");
				}
				{
					jLabelfilesizevalue = new JLabel();
					jPanelcurentfile.add(jLabelfilesizevalue, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					jLabelfilesizevalue.setText("value");
				}
			}
		}
		{
			this.setSize(568, 246);
		}
	} catch(Exception e) {
		e.printStackTrace();
	}
}







	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		    String property = evt.getPropertyName();

		    if (property.equals(DownloadProgressPanel.CURRENTEFILEPROGRESS)) {
		    	int progress = (Integer) evt.getNewValue();
		    	jProgressBarpartial.setValue(progress);
		    }
		    else if(property.equals(DownloadProgressPanel.CURRENTEFILENAME)){
		    	String currentfilename=(String) evt.getNewValue();
		    	jLabelfilenamelabel.setText(currentfilename); 
		    }
		
	}
	
	

}
