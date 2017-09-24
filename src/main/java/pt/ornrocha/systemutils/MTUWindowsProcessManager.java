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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.jvnet.winp.WinProcess;

public class MTUWindowsProcessManager {
	
	
	
	private LinkedHashMap<String, Integer> openedpidprocesses;
	
	static MTUWindowsProcessManager manager;
	
	
	synchronized public static MTUWindowsProcessManager getIntance(){
		if(manager==null)
			manager=new MTUWindowsProcessManager();
		return manager;
	}
	
 
	private MTUWindowsProcessManager(){
		openedpidprocesses=new LinkedHashMap<>();
	}
	
	
	public String followProcess(Process p){
		int pid=new WinProcess(p).getPid();
		openedpidprocesses.put(String.valueOf(pid), pid);
		return String.valueOf(pid);
	}
	
	public void followProcess(Process p, String id) {
		int pid=new WinProcess(p).getPid();
		openedpidprocesses.put(id, pid);
	}
	
	public void killProcessRecursively(String id){
		if(openedpidprocesses.containsKey(id)) {
			new WinProcess(openedpidprocesses.get(id)).killRecursively();
			openedpidprocesses.remove(id);
		}
	}
	
	public void killProcess(String id){
		if(openedpidprocesses.containsKey(id)) {
			new WinProcess(openedpidprocesses.get(id)).kill();
			openedpidprocesses.remove(id);
		}
	}
	
	
	
	public void shutdownOpenedProcesses() {
		
		if(openedpidprocesses.size()>0) {
			for (Integer pid: openedpidprocesses.values()) {
				new WinProcess(pid).killRecursively();
			}
		}
	}
	
	
	
	public static boolean killProcessByName(String processname) throws IOException, InterruptedException {
		
		ArrayList<String> processes=getListWindowsRunningProcesses();
		for (String proc : processes) {
			if(proc.contains(processname)) {
				Process p=Runtime.getRuntime().exec("taskkill /F /IM "+processname);
				int code=p.waitFor();
				if(code==0)
					return true;
				else
					return false;
			}
		}
		return false;
	}
	
	
	public static ArrayList<String> getListWindowsRunningProcesses() throws IOException{

		Process p=Runtime.getRuntime().exec("tasklist");

		ArrayList<String> res=new ArrayList<>();

		BufferedReader inputFile = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String currentline = null;
		try {
			while((currentline = inputFile.readLine()) != null) {
				if(currentline!=null && !currentline.isEmpty())
					res.add(currentline);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return res;
	}
	
}
