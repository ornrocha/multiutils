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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import pt.ornrocha.logutils.MTULogUtils;

public class MTUExcelReaderUtils {
	
	
	
	  public static LinkedHashMap<Integer, ArrayList<Object>> readExcelFileSheet(int nsheet, String filepath, boolean ignoreemptycell){
		  
		  LinkedHashMap<Integer, ArrayList<Object>> res = new LinkedHashMap<>();
		  try {
			FileInputStream fis = new FileInputStream(filepath);
			
			Sheet sh = null;
			
			 Workbook wb = null;
	            if(filepath.toLowerCase().endsWith("xlsx")){
	                wb = new XSSFWorkbook(fis);
	            }else if(filepath.toLowerCase().endsWith("xls")){
	                wb = new HSSFWorkbook(fis);
	            }
			
			sh=wb.getSheetAt(nsheet);
			
			Iterator<Row> rowIterator = sh.iterator();
			
			int rownumber=0;
			while (rowIterator.hasNext()){
				
				Row row = rowIterator.next();

				Iterator<Cell> cellIterator = row.cellIterator();
				ArrayList<Object> rowvalues=new ArrayList<>();
				
				while (cellIterator.hasNext()){
					 Cell cell = cellIterator.next();
					// System.out.println("MTUExcelReaderUtils "+ cell.getCellType());
					 switch(cell.getCellType()){
					 case Cell.CELL_TYPE_STRING:
						  rowvalues.add(cell.getStringCellValue().trim());
						 break;
					 case Cell.CELL_TYPE_NUMERIC:
						 rowvalues.add(cell.getNumericCellValue());
						 break;
					 case Cell.CELL_TYPE_BOOLEAN:
						 rowvalues.add(cell.getBooleanCellValue());
						 break; 
					 case Cell.CELL_TYPE_BLANK:{
						if(!ignoreemptycell) 
						   rowvalues.add("");
						break; 
					 }
					default:	 
						break;
					 
					 }
					 
					
				}
				
				MTULogUtils.addDebugMsgToClass(MTUExcelReaderUtils.class,"Extracted values in row {}: {}", rownumber,rowvalues);
				
				res.put(rownumber, rowvalues);
				
				rownumber++;
			}
			
			
			fis.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		  
		  return res;
		  
	  }
	  
	  
	  public static ArrayList<String> readExcelFileasCSVFormat(int nsheet,String filepath, String delimiter,int ignorefirstNlines,boolean ignoreemptycell){
		  LinkedHashMap<Integer, ArrayList<Object>> excelres =readExcelFileSheet(nsheet, filepath,ignoreemptycell);
		  ArrayList<String> res = new ArrayList<>();
		  
		  int startreadfrom=0;
		  
		  if(ignorefirstNlines>0)
			  startreadfrom=ignorefirstNlines;
		  
		  
		  for (Map.Entry<Integer, ArrayList<Object>> rowvalues: excelres.entrySet()) {
			 if(rowvalues.getKey()>=startreadfrom){
				 ArrayList<Object> values = rowvalues.getValue();
				 StringBuilder str=new StringBuilder();
	           if(values.size()>0){
				 for (int i = 0; i <values.size(); i++) {
					 
					 str.append(String.valueOf(values.get(i)));
					 if(i<values.size()-1)
						 str.append(delimiter);
				 }
				 res.add(str.toString());
			   }
			 }
			  
		   }
		  return res;
	  }
	
	

}
