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
package pt.ornrocha.webutils.connectionutils.gui;

import java.util.concurrent.ThreadPoolExecutor;

public class DownloadFutureProcesskiller extends Thread{

	
	private ThreadPoolExecutor executor=null;
	
	public DownloadFutureProcesskiller(){
		
	}
	
	public DownloadFutureProcesskiller(ThreadPoolExecutor executor){
		this.executor=executor;
	}
	
	public void setThreadPoolExecutortoKill(ThreadPoolExecutor executor){
		this.executor=executor;
	}
	
	
	@Override
	public void run() {
		
		executor.shutdownNow();
	}
	
}
