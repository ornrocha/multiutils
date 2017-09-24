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
package pt.ornrocha.timeutils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class MTUTimeUtils {
	
	
	
	public static void printTimeElapsed(long elapsed){
		   DateFormat df = new SimpleDateFormat("HH 'hours', mm 'mins,' ss 'seconds'");
		   df.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		   System.out.println(df.format(new Date(elapsed)));
		   
	   }
	
	public static String getTimeElapsed(long elapsed){
		
		   double seconds=((double)elapsed / 1000);
		
		
		   if(seconds<60){
			   return String.valueOf(seconds)+" seconds";
		   }
		   else{
			   DateFormat df = new SimpleDateFormat("HH 'hours', mm 'mins,' ss 'seconds'");
			   df.setTimeZone(TimeZone.getTimeZone("GMT+0"));
			   return df.format(new Date(elapsed));
		   }
		   
	   }
	
	
	
	public static String getCurrentDateAndTime(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	public static String getCurrentDateAndTime(String outputformat){
		DateFormat dateFormat = new SimpleDateFormat(outputformat);
		Date date = new Date();
		return dateFormat.format(date);
	}

}
