package multiutils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import pt.ornrocha.webutils.connectionutils.WEBConnection;
import pt.ornrocha.webutils.connectionutils.WebConnectionException;
import pt.ornrocha.webutils.htmlutils.MTUHtmlUtils;

public class Testes {

	public static void main(String[] args) {
		
		 String url = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=gds&term=GSE37780[ACCN]&retmax=5000&retmode=json&usehistory=y";
		 
		 WEBConnection conn = new WEBConnection(url, "12.218.150.209",8080, "json");
		 StringBuilder answer = new StringBuilder(100000);
		
		 try {
		 BufferedReader in = new BufferedReader(new InputStreamReader(conn.doConnection()));
	        String inputLine;

	        
				while ((inputLine = in.readLine()) != null) {
					System.out.println(inputLine);
					System.out.println();
				    //answer.append(inputLine);
				   // answer.append("\n");
				}
			
	        in.close();
          } catch (IOException | WebConnectionException e) {
				//System.out.println("Can connect");
				e.printStackTrace();
			}
	}

}
