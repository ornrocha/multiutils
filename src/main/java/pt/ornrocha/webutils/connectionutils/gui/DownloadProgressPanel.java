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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

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
public class DownloadProgressPanel extends JPanel implements PropertyChangeListener{


	private static final long serialVersionUID = 1L;
	private JProgressBar jProgressBardata;
	private JLabel jLabelsizevalue;
	private JLabel jLabelsize;
	private PropertyChangeSupport changelst = new PropertyChangeSupport(this);
	private static String BASEBORDERTXT="Downloading: ";
	
	
	
	public static String CURRENTEFILEPROGRESS="currentfileprogress";
	public static String CURRENTEFILENAME="currentfilename";
	public static String CURRENTEFILESIZE="currentfilesize";
	
	public DownloadProgressPanel(){
		initGUI();
		setBorderText(null);
		changelst.addPropertyChangeListener(this);
	}
	
	
	public PropertyChangeSupport getListenerSupport(){
		return this.changelst;
	}
	


	private void initGUI() {
	try {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			this.setPreferredSize(new java.awt.Dimension(526, 45));
			thisLayout.rowWeights = new double[] {0.1};
			thisLayout.rowHeights = new int[] {7};
			thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
			thisLayout.columnWidths = new int[] {7, 7, 7, 7, 7, 7};
			this.setLayout(thisLayout);
			
			{
				jProgressBardata = new JProgressBar(0, 100);
				this.add(jProgressBardata, new GridBagConstraints(2, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jProgressBardata.setStringPainted(true);
			}
			{
				jLabelsize = new JLabel();
				this.add(jLabelsize, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelsize.setText("Size (KB)");
			}
			{
				jLabelsizevalue = new JLabel();
				this.add(jLabelsizevalue, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jLabelsizevalue.setText("getting size");
			}
		}
	} catch(Exception e) {
		e.printStackTrace();
	}
}
	
	
	
	private void setBorderText(String str){
		if(str!=null)
			str=BASEBORDERTXT+str;
		else
			str=BASEBORDERTXT;
		
		
		this.setBorder(BorderFactory.createTitledBorder(str));
	}



	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		String property = evt.getPropertyName();

	    if (property.equals(CURRENTEFILEPROGRESS)) {
	    	int progress = (Integer) evt.getNewValue();
	    	jProgressBardata.setValue(progress);
	    }
	    else if(property.equals(CURRENTEFILENAME)){
	    	String currentfilename=(String) evt.getNewValue();
	    	setBorderText(currentfilename); 
	    }
	    else if(property.equals(CURRENTEFILESIZE)){
	    	long size = (long) evt.getNewValue();
	    	long fileSizeInKB = size/1024;
	    	
	    	long fileSizeInMB = fileSizeInKB / 1024;
	    	
	    	if(fileSizeInMB > 2){
	    		jLabelsize.setText("Size (MB)");
	    		jLabelsizevalue.setText(String.valueOf(fileSizeInMB));
	    	}
	    	else{
	    		jLabelsize.setText("Size (KB)");
	    	   jLabelsizevalue.setText(String.valueOf(fileSizeInKB));
	    	   
	    	}
	    	
	    }
		
	}

}
