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
package pt.ornrocha.logutils;

public enum MTULogLevel {
	
	OFF,
	ERROR,
	WARNING,
	INFO,
	DEBUG,
	TRACE;

	public static MTULogLevel getLevelFromStringName(String name){
		
		switch (name.toLowerCase()) {
		case "off":
			return MTULogLevel.OFF;
		case "warn":
			return MTULogLevel.WARNING;
		case "info":
			return MTULogLevel.INFO;
		case "debug":
			return MTULogLevel.DEBUG;
		case "trace":
			return MTULogLevel.TRACE;

		default:
			return MTULogLevel.ERROR;
		}
	}

}

