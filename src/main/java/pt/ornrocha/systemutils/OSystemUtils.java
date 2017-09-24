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
package pt.ornrocha.systemutils;

import java.io.File;

import org.apache.commons.lang3.SystemUtils;

public class OSystemUtils {
	
	public static String Bit64="b64";
	public static String Bit32="b32";
	public static String Bitundefined="bundifined";
	
	public enum OS {

		
			/** The LINUX32. */
			LINUX32,
		
			/** The LINUX64. */
			LINUX64,
		
			/** The WIN32. */
			WIN32,
		
			/** The WIN64. */
			WIN64,
		
			/** The MACOS */
			MACOS,
			
			UNDEFINED;


		}

	
	public static String getOsName(){
		return System.getProperty("os.name");
	}

	public static boolean isLinux(){
	     return SystemUtils.IS_OS_UNIX;
	}


  /**
   * Checks if is windows.
   *
   * @return true, if is windows
   */
	public static boolean isWindows(){
	   return SystemUtils.IS_OS_WINDOWS;
	}
  
  /**
   * Checks if is mac os.
   *
   * @return true, if is mac os
   */
	public static boolean isMacOS(){
		return (SystemUtils.IS_OS_MAC_OSX || SystemUtils.IS_OS_MAC);
	}
  
	public static OS getOperationSystem()  {
		String archversion=getOperationSystemBitFlag();
  	
		if(isLinux()){
			if(archversion.equals(Bit64))
				return OS.LINUX64;
			else if (archversion.equals(Bit32))
				return OS.LINUX32;
		}
		else if(isWindows()){
			if(archversion.equals(Bit64))
				return OS.WIN64;
			else if (archversion.equals(Bit32))
				return OS.WIN32;
  		
		}
		else if(isMacOS()){
  				return OS.MACOS;
		}

		return OS.UNDEFINED;
	}
  
	public static String getOperationSystemBitFlag(){
  	
		String arch = SystemUtils.OS_ARCH;

		if (arch.equals("amd64") || arch.equals("x86_64")) {
			return Bit64;
		}    
		else if (arch.equals("x86") || arch.equals("i386")) {
			return Bit32;
		}
		else 
			return Bitundefined;
	}
  
	
	public static String getSystemSeparator(){
		return File.separator;
	}
  
	public static String getSystemTemporaryFolder(){
		String tmpDir = SystemUtils.getJavaIoTmpDir().getAbsolutePath();
		return tmpDir;
	}
	
	public static String getUserWorkingDirectory(){
		return System.getProperty("user.dir");
	}
	
	public static String getUserHomeDirectory(){
		return System.getProperty("user.home");
	}

	public static String validatePath(String origpath){
		if(isWindows() && origpath!=null){
			if(origpath.startsWith("\"") || origpath.endsWith("\""))
				origpath=origpath.replace("\"", "");
			origpath=origpath.replace("\\", "/");
			return origpath;
		}
		else
			return origpath;
	}
	
	/*public static String convertPathToWindows(String origpath){
		return origpath.replace("\\", File.separator);
	}
	
	public static String convertWindowsPathToSlash(String origpath){
		return origpath.replace("\\", "/");
	}*/
	
	public static String convertWindowsPathtoDoubleSeparator(String origpath){
		return origpath.replace("\\", "\\\\");
	}

}
