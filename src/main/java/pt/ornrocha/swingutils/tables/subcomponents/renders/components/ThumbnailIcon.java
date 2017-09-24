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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.Icon;

public class ThumbnailIcon implements Icon {  
    int width, height;  
    Image image;  

    public ThumbnailIcon(Image image, int width) {  
        this.image = image;  
        this.width = width;  
        this.height = (width * image.getHeight(null)) / image.getWidth(null);  
    }
     
    public int getIconHeight () {
        return height;
    }
     
    public int getIconWidth () {
        return width;
    }

    public void paintIcon (Component c, Graphics g, int x, int y) {  
        g.drawImage(image, x, y, width, height, c);  
    }  
}  



