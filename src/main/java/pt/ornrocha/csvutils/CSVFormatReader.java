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
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import pt.ornrocha.ioutils.readers.MTUReadUtils;

public class CSVFormatReader {
	
	protected int ncolumns=0;
	protected ArrayList<String> lines;
	protected boolean ignorefirstline=false;
	protected String delimiter=";";
	protected int[] readcolumnposi;

	
	public CSVFormatReader(String filepath, String delimiter, boolean ignoreheader) throws IOException{
		this.lines=(ArrayList<String>) MTUReadUtils.readFileLines(filepath);
		if(delimiter!=null)
		    this.delimiter=delimiter;
		this.ignorefirstline=ignoreheader;
	}
	
	public CSVFormatReader(String filepath, int ncolumns, String delimiter, boolean ignoreheader) throws IOException{
		this.lines=(ArrayList<String>) MTUReadUtils.readFileLines(filepath);
		this.ncolumns=ncolumns;
		if(delimiter!=null)
		    this.delimiter=delimiter;
		this.ignorefirstline=ignoreheader;
	}
	
	public CSVFormatReader(String filepath, int [] colunmpos, String delimiter, boolean ignoreheader) throws IOException{
		this.lines=(ArrayList<String>) MTUReadUtils.readFileLines(filepath);
		this.readcolumnposi=colunmpos;
		if(delimiter!=null)
		    this.delimiter=delimiter;
		this.ignorefirstline=ignoreheader;
	}
	
	
	public ArrayList<String[]> getAllLineElements(){
		ArrayList<String[]> elems = new ArrayList<>();
		
		for (int i = 0; i < lines.size(); i++) {
			String[]linelems=lines.get(i).split(delimiter);
			elems.add(linelems);
		}
		
		return elems;
	}
	
	public static void readCSV(String filepath) throws IOException{
		File csvfile = new File(filepath);
		CSVParser csv = new CSVParser(new FileReader(csvfile),CSVFormat.DEFAULT.withHeader().withDelimiter((char)';'));
		System.out.println(csv.getHeaderMap());
		List csvRecords = csv.getRecords(); 
		for (int i = 0; i < csvRecords.size(); i++) {
			CSVRecord record = (CSVRecord) csvRecords.get(i);
			System.out.println(record.get("b3072"));
		}
	}
	
	
	public static void main(String[] args){
		try {
			
			readCSV("/home/orocha/MEOCloud/TRABALHO/Organizacao_phenotype_exper/Geral/MBD1/ALLDATA/PM1/all_data_PM1_teste.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
