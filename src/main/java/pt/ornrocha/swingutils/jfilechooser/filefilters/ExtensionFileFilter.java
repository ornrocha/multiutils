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
package pt.ornrocha.swingutils.jfilechooser.filefilters;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.apache.commons.io.FilenameUtils;

public class ExtensionFileFilter extends FileFilter{

	
	private String ext;
	private String description;
	
	public ExtensionFileFilter(String allowedextension) {
		this.ext=allowedextension;
	}
	
	public ExtensionFileFilter(String description, String allowedextension) {
		this.ext=allowedextension;
		this.description=description;
	}
	
	
	@Override
	public boolean accept(File f) {
		String fileext=FilenameUtils.getExtension(f.getAbsolutePath());
		if(fileext.toLowerCase().equals(ext.toLowerCase()))
			return true;
		else if(f.isDirectory())
			return true;
		return false;
	}

	@Override
	public String getDescription() {
		if(description==null)
		  return "*."+ext;
		else
			return description+" (*."+ext+")";
	}
	
	public String getSingleDescription() {
		if(description==null)
			  return ext;
			else
				return description;
	}
	
	public String getExtension() {
		return ext;
	}

}
