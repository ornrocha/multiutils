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
package pt.ornrocha.excelutils;

public enum ExcelVersion {
	
	
	XLS{
		public String toString(){
			return "xls";
		}
		
		public String getFileNameWithExtension(String path){
			return path+".xls";
		}
	},
	
	XLSX{
		public String toString(){
			return "xlsx";
		}
		
		public String getFileNameWithExtension(String path){
			return path+".xlsx";
		}
	};
    
	
	public  String getFileNameWithExtension(String path){
		return getFileNameWithExtension(path);
	}
	
	public static ExcelVersion getVersionFromExtension(String extension){
		if(extension.toLowerCase().equals("xls"))
			return ExcelVersion.XLS;
		else if(extension.toLowerCase().equals("xlsx"))
			return ExcelVersion.XLSX;
		else
			return null;
			
		
	}
}
