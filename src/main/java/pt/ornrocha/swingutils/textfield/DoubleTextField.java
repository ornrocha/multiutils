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
package pt.ornrocha.swingutils.textfield;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class DoubleTextField extends JTextField {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DoubleTextField() {
        super();
    }

    public DoubleTextField( int columns) {
        super(columns);
    }

    @Override
    protected Document createDefaultModel() {
        return new DocumentValidator(this);
    }

    static class DocumentValidator extends PlainDocument {

    	boolean inserteddot=false;
    	JTextField parentcomp;
    	
    	public DocumentValidator(JTextField parentcomp) {
    		this.parentcomp=parentcomp;
    	}
    	
    	
        @Override
        public void insertString( int offs, String str, AttributeSet a )
                throws BadLocationException {

            if ( str == null ) {
                return;
            }
            char[] chars = str.toCharArray();
            boolean ok = true;

            for ( int i = 0; i < chars.length; i++ ) {

                try {
                	if(chars[i]=='.' && !parentcomp.getText().contains(".")) {
                		ok=true;
                		continue;
                	}
                    Double.parseDouble(String.valueOf(chars[i]));
                } catch ( NumberFormatException exc ) {
                    ok = false;
                    break;
                }


            }

            if (ok)
                super.insertString( offs, new String(chars), a);

        }
    }
    
    public double getDoubleValue() {
    	if(getText().isEmpty())
    		return 0;
    	else {
    		return Double.parseDouble(getText());
    	}
    }
    
    public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		frame.getContentPane().add(new DoubleTextField());
		frame.setSize(100,100);
	    frame.setVisible(true);
    }

}
