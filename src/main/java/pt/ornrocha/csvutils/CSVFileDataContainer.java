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
package pt.ornrocha.csvutils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import pt.ornrocha.arrays.MTUMatrixUtils;

public class CSVFileDataContainer implements Serializable{
	

	private static final long serialVersionUID = 1L;
	private CSVParser csv;
	private List csvrecords;
	private ArrayList<String> columnheaderelements;
	private ArrayList<String> rowheaderselements;
	private LinkedHashMap<String, LinkedHashMap<String, String>> mapvalueswithrowandcolumnpositions;
	

	
	public CSVFileDataContainer(String filepath, char delimiter){
		File csvfile = new File(filepath);
		
		try {
			csv = new CSVParser(new FileReader(csvfile),CSVFormat.DEFAULT.withHeader().withDelimiter(delimiter));
			csvrecords=csv.getRecords();
			loadelements();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public CSVFileDataContainer(ArrayList<String> columnheader, ArrayList<String>rowheaderselements, LinkedHashMap<String, LinkedHashMap<String, String>> datamap){
		this.columnheaderelements=columnheader;
		this.rowheaderselements=rowheaderselements;
		this.mapvalueswithrowandcolumnpositions=datamap;	
	}
	
	
	
	private void loadelements(){
		setHeaderElements();
		setMapOfValuesConcerningRowAndColumn();
		setRowHeadersElements();
	}
	
	private void setHeaderElements(){
		this.columnheaderelements=new ArrayList<>(csv.getHeaderMap().keySet());
	}
	
	public ArrayList<String> getColumnHeaderElements(){
		return columnheaderelements;
	}
	
	
	public ArrayList<String> getColumnHeaderElementsIgnoreFirstNElements(int ignorefirst){
		ArrayList<String> res= new ArrayList<>();
		
		for (int i = ignorefirst; i < columnheaderelements.size(); i++) {
			res.add(columnheaderelements.get(i));
		}
	   return res;
	}
	
	public CSVParser getParsedFile(){
		return csv;
	}
	
	private void setRowHeadersElements(){
		this.rowheaderselements=getElementsOfColumn(getColumnHeaderElements().get(0));
	}
	
	public ArrayList<String> getRowheaders(){
		return rowheaderselements;
	}
	

	public ArrayList<String> getElementsOfColumn(String columnid){
		ArrayList<String> res =new ArrayList<>();
		
		int s=0;
		if(header!=null)
			s=1;
		
		for (int i = s; i < csvrecords.size(); i++) {
			CSVRecord record = (CSVRecord) csvrecords.get(i);
			res.add(record.get(columnid));
		}
		return res;
	}
	
	
	private void setMapOfValuesConcerningRowAndColumn(){
		
		this.mapvalueswithrowandcolumnpositions=new LinkedHashMap<>();
		ArrayList<String> header=getColumnHeaderElements();
		
		for (int i = 0; i < csvrecords.size(); i++) {
			CSVRecord record = (CSVRecord) csvrecords.get(i);
			HashMap<String, String> recordmap=new HashMap<>(record.toMap());
			String rowheader=null;
			LinkedHashMap<String, String> mapvaluetocolumn=new LinkedHashMap<>();
			for (int j = 0; j < header.size(); j++) {
				if(j==0)
					rowheader=recordmap.get(header.get(j));
				else{
					String columnname=header.get(j);
					mapvaluetocolumn.put(columnname, recordmap.get(columnname));
				}
			}
			mapvalueswithrowandcolumnpositions.put(rowheader, mapvaluetocolumn);
		}
	}
	
	
	public String getValue(String rowname, String columnname){
		LinkedHashMap<String, String> row=mapvalueswithrowandcolumnpositions.get(rowname);
		return row.get(columnname);
	}
	
	public String getValue(int rowpos, int columnpos){
		if(rowpos<=mapvalueswithrowandcolumnpositions.size()){
			
			String rowheader=rowheaderselements.get(rowpos);
			LinkedHashMap<String, String> row = mapvalueswithrowandcolumnpositions.get(rowheader);
			
			if(columnpos<=row.size()){
				String columnheader=columnheaderelements.get(columnpos+1);
				return row.get(columnheader);	
			}
			else
				throw new IndexOutOfBoundsException();	
			
		}
		else
			throw new IndexOutOfBoundsException();
	}
	
	
	
	public String[][] getCSVStringMatrixWithNoColumnHeaderAndRowHeader(){
		int nrows=getRowheaders().size();
		int ncolums=getColumnHeaderElements().size();
		
		String[][] matrix=new String[nrows][ncolums-1];
		ArrayList<String> header=getColumnHeaderElements();
		
		for (int i = 1; i <ncolums; i++) {
			ArrayList<String> elems=getElementsOfColumn(header.get(i));
			String[] array=elems.toArray(new String[elems.size()]);
			matrix=MTUMatrixUtils.addColumnToMatrix(array, matrix, (i-1));
		}
		
		return matrix;
	}
	
	
	public double[][] getCSVDoubleDataMatrix(){
		
		String[][] strmatrix=getCSVStringMatrixWithNoColumnHeaderAndRowHeader();
		double[][] doublematrix=new double[strmatrix.length][strmatrix[0].length];
		
		try{
		   for (int i = 0; i < doublematrix.length; i++) {
			
			   for (int j = 0; j < doublematrix[0].length; j++) {
				   double n=Double.valueOf(strmatrix[i][j].replace(",", "."));
				  doublematrix[i][j]=n;
			   }
		     }
		}catch(Exception e){
           e.printStackTrace();
        }
		return doublematrix;
	}
	
	public RealMatrix getCSVRealMatrix(){
		return MatrixUtils.createRealMatrix(getCSVDoubleDataMatrix());
	}
	
	
	public void writeToFile(String dirpath, String filename, ArrayList<String> rowids){
		try {
			String name=FilenameUtils.concat(dirpath, filename+".csv");
			FileWriter writer=new FileWriter(name);
	
			String[] fileheader=null;
			if(header!=null)
				fileheader=header;
			else
				fileheader=getColumnHeaderElements().toArray(new String[getColumnHeaderElements().size()]);
			
			CSVPrinter csvprinter=new CSVPrinter(writer,format.withHeader(fileheader));

			ArrayList<String> header=getColumnHeaderElements();
			ArrayList<String> rows=getRowheaders();

			for (int i =0; i < rows.size(); i++) {
				String rowid=rows.get(i);
				if(rowids!=null){
					if(rowids.contains(rowid))
						printRecord(csvprinter, header, rowid);
				}
				else{
					printRecord(csvprinter, header, rowid);
				}
			}
			
			writer.flush();
			writer.close();
			csvprinter.close();
			
			
		} catch (Exception e) {
		    e.printStackTrace();
	    }
	}
	
	private void printRecord(CSVPrinter printer, ArrayList<String> header, String rowid) throws IOException{
		  List data = new ArrayList<>();
		  data.add(rowid);
		  for (int j = 1; j < header.size(); j++) {
	            String headerid=header.get(j);
				data.add(getValue(rowid, headerid));
		   }
		 printer.printRecord(data);
	}
	
	

	
	
	/*
	 * ######################################  Static fields ##############################################
	 * 
	 */
	
	private CSVFileDataContainer container;
	private char delimiter;
	private FileReader filereader;
	private String[] header=null;
	private CSVFormat format=CSVFormat.DEFAULT.withHeader().withDelimiter(';');
	
	
	private CSVFileDataContainer(String filepath){
		File csvfile = new File(filepath);
		try {
			filereader=new FileReader(csvfile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
		
	
	public CSVFileDataContainer withDelimiter(char delimiter){
		this.delimiter=delimiter;
		return this;
	}
	
	public CSVFileDataContainer withHeader(String[] header){
		this.header=header;
		return this;
	}
	
	
	public static CSVFileDataContainer openFile(String file){
		return new CSVFileDataContainer(file);
	}
	
	public CSVFileDataContainer read() throws IOException{
		configureCSVFormat();
		csv = new CSVParser(filereader,format);
		csvrecords=csv.getRecords();
		loadelements();
		return this;
	}
	
	private void configureCSVFormat(){
		if(delimiter!=0x0)
			format=format.withDelimiter(delimiter);
		if(header!=null)
			format=format.withHeader(header);
	}
	
	
	
	public static CSVFileDataContainer createCSVFileDataContainerFromDoubleMatrix(ArrayList<String> columnheader, ArrayList<String>rowheaderselements, double[][] data){

		
		boolean validdata=true;
		LinkedHashMap<String, LinkedHashMap<String, String>> mapdata=new LinkedHashMap<>();
		DecimalFormat df = new DecimalFormat("#.##");

		if(data.length!=rowheaderselements.size()){
			validdata=false;
			throw new IndexOutOfBoundsException("Data matrix and row headers vector have a diferent number of rows");
		}
		if(data[0].length!=(columnheader.size()-1)){
			validdata=false;
			throw new IndexOutOfBoundsException("Data matrix and column headers vector have a diferent number of columns");
		}
		
		if(validdata)		
		for (int i = 0; i < rowheaderselements.size(); i++) {
		    String rowid=rowheaderselements.get(i);
			LinkedHashMap<String, String> mapcolumnvalue=new LinkedHashMap<>();
			
			for (int j = 0; j < columnheader.size()-1; j++) {
				String columnid=columnheader.get(j+1);
				//mapcolumnvalue.put(columnid, String.valueOf(data[i][j]));
				mapcolumnvalue.put(columnid, df.format(data[i][j]));
			}
			mapdata.put(rowid, mapcolumnvalue);
		}
		
		return new CSVFileDataContainer(columnheader, rowheaderselements, mapdata);
		
	}
	
	

	public static void main(String[] args) {
		String file="/home/orocha/MEOCloud/TRABALHO/Organizacao_phenotype_exper/Geral/MBD1/FilteredData/PM1/filtered_PM1.csv";
		
		try {
			//CSVFileDataContainer csv=CSVFileDataContainer.openFile(file).withHeader(new String[]{"b3066","b3072"}).withDelimiter(';').read();
			CSVFileDataContainer csv=CSVFileDataContainer.openFile(file).withDelimiter(';').read();
			System.out.println(csv.getColumnHeaderElementsIgnoreFirstNElements(1));
			System.out.println(csv.getRowheaders());
			//csv.writeToFile("/home/orocha/MEOCloud/TRABALHO/Organizacao_phenotype_exper/Geral/MBD1/FilteredData/PM1", "Test",null);
			//System.out.println(csv.getHeaderElements());
			//System.out.println(csv.getValue("a-Keto-Butyric Acid", "b3066"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		CSVFileDataContainer csv = new CSVFileDataContainer("/home/orocha/MEOCloud/TRABALHO/Organizacao_phenotype_exper/Geral/MBD1/ALLDATA/PM1/all_data_PM1_teste.csv",';');
		System.out.println(csv.getValue("a-Keto-Butyric Acid", "b3161"));
		*/
        //System.out.println(csv.getHeaderElements());
		//MTUPrintUtils.printStringMatrix(csv.getCSVStringMatrixWithNoColumnHeaderAndRowHeader());
		//double[][] m=csv.getCSVDoubleDataMatrix();
		//RealMatrix m=csv.getCSVRealMatrix();
		//MTUPrintUtils.printDoubleMatrix(m.getData());
	}

}
