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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class MTUExcelWriterUtils {
	
	
  public static void WriteHeader(XSSFSheet sheet, String header, String delimiter){
		
		String[] elems = header.split(delimiter);
		if(elems.length>0){
			Row row = sheet.createRow(0);
			for (int i = 0; i < elems.length; i++) {
				Cell cell = row.createCell(i);
				cell.setCellValue(elems[i]);
			}
			
		}
		
	}
  
  
  
  private static void WriteDataToExcelSheet(Sheet sht, ArrayList<ArrayList<Object>> excelrows){
	  
	  for (int i = 0; i < excelrows.size(); i++) {
		   
		   Row row = sht.createRow(i);
		   ArrayList<Object> data = excelrows.get(i);
		   
		   for (int j = 0; j < data.size(); j++) {
			   
			   Object obj = data.get(j);
			   Cell cell =row.createCell(j);
			   
			   if(obj instanceof Integer)
				   cell.setCellValue((Integer)obj);
			   else if(obj instanceof Double)
				   cell.setCellValue((Double)obj);
			   else if(obj instanceof Boolean)
				   cell.setCellValue((Boolean)obj);
			   else if (obj instanceof Date)
                  cell.setCellValue((Date) obj);
			   else if(obj instanceof String)
				   cell.setCellValue((String) obj);
			   else
				   cell.setCellValue(String.valueOf(obj));
		   }

	   }
	  
  }
  
  
 
  public static void updateDataOfExcelFile(File file, ArrayList<ArrayList<Object>> excelrows,String sheetname) throws IOException{
	  
	  Workbook wb=null;
	  Sheet sht=null;
	  
	  
	  FileInputStream infile=new FileInputStream(file);
	  ExcelVersion version=ExcelVersion.getVersionFromExtension(FilenameUtils.getExtension(file.getAbsolutePath()));
	  
	  if(version.equals(ExcelVersion.XLS))
		  wb=new HSSFWorkbook(infile);
	  else
		  wb=new XSSFWorkbook(infile);

	  
	  if(sheetname!=null && wb.getSheet(sheetname)!=null){
		  sht=wb.getSheet(sheetname);
	  }
	  else{
		  if(sheetname!=null)
			  sht=wb.createSheet(sheetname);
		  else
			  sht=wb.createSheet();
	  }
		  
	  WriteDataToExcelSheet(sht, excelrows);
	  
	  infile.close();
	  
	  FileOutputStream fos = new FileOutputStream(file);
	  wb.write(fos);
	  fos.close();
	  wb.close();

   }
  
  public static void updateDataOfExcelFile(String filepath, ArrayList<ArrayList<Object>> excelrows,String sheetname) throws IOException{
	  updateDataOfExcelFile(new File(filepath), excelrows, sheetname);
  }

  
  public static void WriteDataToNewExcelFile(String filepath, ExcelVersion extension, ArrayList<ArrayList<Object>> excelrows, String sheetname) throws IOException{
	  
	  Workbook wb=null;
	  Sheet sht=null;
	  
	  if(extension.equals(ExcelVersion.XLS))
		  wb=new HSSFWorkbook();
	  else
		  wb=new XSSFWorkbook();

	  if(sheetname!=null)
		  sht=wb.createSheet(sheetname);
	  else
		  sht=wb.createSheet();
	  
	  String filepathout=extension.getFileNameWithExtension(filepath);
	  File f=new File(filepathout);
	  if(!f.exists())
		  f.createNewFile();
	  
	  WriteDataToExcelSheet(sht, excelrows);
	  
	   FileOutputStream fos = new FileOutputStream(filepathout);
	   wb.write(fos);
	   fos.close();
	   wb.close();

   }
  
  public static <T> ArrayList<ArrayList<Object>> convertArrayListToColumnExcelList(List<T> values){
	  ArrayList<ArrayList<Object>> res=new ArrayList<>();
	  
	  for (int i = 0; i < values.size(); i++) {
		  ArrayList<Object> row=new ArrayList<>();
		  row.add(values.get(i));
		  res.add(row);
	   }
	  
	  return res;
  }


}
