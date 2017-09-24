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
package pt.ornrocha.webutils.htmlutils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.javatuples.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MTUHtmlUtils {
	
	
	
	public static Document getHTMLDocument(String url) throws IOException{
		return Jsoup.connect(url).get();
	}
	
	
	public static Element getHTMLElementById(Document doc,String id){
		return doc.getElementById(id);
	}
	
	public static Elements getHTMLElementsbyTag(Element node, String tag){
		return node.getElementsByTag(tag);
	}
	
	public static String getInnerHTMLElementsbyTag(Element node,String tag){
		return node.getElementsByTag(tag).text();
	}
	
	public static Elements getHTMLElementsbyClass(Element node, String classname){
		return node.getElementsByClass( classname);
	}
	
	public static ArrayList<String> getHTMLStringElementsbyClass(Element node, String classname, String innertag){
		ArrayList<String> res = new ArrayList<>();
		
		Elements innerelms = node.getElementsByClass(classname);
		
		for (Element el: innerelms) {
			res.add(el.getElementsByTag(innertag).text());
		}
		return res;
	}
	
	
	public static ArrayList<String> getHTMLStringElementsOfInnerTagElemByClass(Element node,String outertag,String classname, String innertag){
		  ArrayList<String> res = new ArrayList<>();
		  Elements forms = node.getElementsByTag(outertag);
		  
		  for (Element element : forms) {
			  res.addAll(getHTMLStringElementsbyClass(element,classname,innertag));
		  }
		  return res;
	}
	
	
	public static LinkedHashMap<String, String> getInnerElementsByClassOfOuterElementBytag(Document doc, String rootelemid,String outertag,Pair<String, String> classnames,String innertag){
		LinkedHashMap<String, String> res = new LinkedHashMap<>();
		
		Element root = doc.getElementById(rootelemid);
		
		ArrayList<String> keys=getHTMLStringElementsOfInnerTagElemByClass(root,outertag,classnames.getValue0(), innertag);
		ArrayList<String> values=getHTMLStringElementsOfInnerTagElemByClass(root,outertag,classnames.getValue1(), innertag);
		
		if(keys.size()==values.size()){
			for (int i = 0; i < keys.size(); i++) {
				res.put(keys.get(i), values.get(i));
			}
		}
	
		return res;
	}
	
	public static LinkedHashMap<String, String> getTwoTagValuesMapOfHTMLpage(String url, String rootelemid,String outertag,String classnamekey,String classnamevalue,String innertag) throws IOException{
		Pair<String, String> mapping =new Pair<String, String>(classnamekey, classnamevalue);
		Document doc = getHTMLDocument(url);
		if(doc!=null){
			return getInnerElementsByClassOfOuterElementBytag(doc, rootelemid,outertag,mapping,innertag);
		}
		return null;
	}

}
