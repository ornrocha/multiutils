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
package pt.ornrocha.webutils.connectionutils;


public class WebConnectionException extends Exception{


	private static final long serialVersionUID = 1L;
	
	public WebConnectionException(String msg){
		super(msg);
	}
	
	public WebConnectionException(String msg, int codeerror){
		super(msg+" "+getCodeMSG(codeerror));
	}
	
	public WebConnectionException(int codeerror){
		super(getCodeMSG(codeerror));
	}
	
	
	public static String getCodeMSG(int code){
		
		switch (code) {
		case -2:
			return "Connection Timeout";
		case -1:
		    return "Invalid Document type";
		case 203:
			return "Non-Authoritative Information";
		case 204:
			return "No Response";
		case 205:
			return "The server successfully processed the request, but is not returning any content.";
		case 400:
			return "Bad Request";
		case 401:
			return "Unauthorized";
		case 402:
			return "Payment Required";
		case 403:
			return "Forbidden";
		case 404:
			return "Not Found";
		case 405:
			return "Method Not Allowed";
		case 406:
			return "Not Acceptable";
		case 407:
			return "Proxy Authentication Required";
		case 408:
			return "Request Timeout";
		case 500:
			return "Internal Error";
		case 501:
			return "Not implemented";
		case 502:
			return "Bad Gateway";
		case 503:
			return "Service Unavailable";
		case 504:
			return "Gateway Timeout";
		case 505:
			return "HTTP Version Not Supported";
		default:
			break;
		}
		
		return "Unknown Error";
	}
	

}
