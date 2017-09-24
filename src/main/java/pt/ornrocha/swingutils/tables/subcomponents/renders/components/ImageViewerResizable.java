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
package pt.ornrocha.swingutils.tables.subcomponents.renders.components;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class ImageViewerResizable extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private java.awt.Image image;
    private int xCoordinate;
    private boolean stretched = true;
    private int yCoordinate;

    public ImageViewerResizable() {
    }

    public ImageViewerResizable(Image image) {
        this.image = image;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image != null) {
            if (isStretched()) {
                g.drawImage(image, xCoordinate, yCoordinate,getSize().width, getSize().height, this);
            } else {
                g.drawImage(image, xCoordinate, yCoordinate, this);
            }
        }
    }

    public java.awt.Image getImage() {
        return image;
    }

    public void setImage(java.awt.Image image) {
        this.image = image;
        repaint();
    }

    public boolean isStretched() {
        return stretched;
    }

    public void setStretched(boolean stretched) {
        this.stretched = stretched;
        repaint();
    }

    public int getXCoordinate() {
        return xCoordinate;
    }

    public void setXCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
        repaint();
    }

    public int getYCoordinate() {
        return yCoordinate;
    }

    public void setYCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
        repaint();
    }
}
