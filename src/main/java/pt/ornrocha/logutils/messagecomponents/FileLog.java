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
package pt.ornrocha.logutils.messagecomponents;

import java.beans.PropertyChangeEvent;


import pt.ornrocha.logutils.MTULogLevel;
import pt.ornrocha.logutils.MTULogUtils;

public class FileLog implements ILogProgressListener{
	
	
	private MTULogLevel defaultlevel=MTULogLevel.DEBUG;
	private Class<?> currentclass;
	 private boolean usestacktrace=false;
	
	 
	 public FileLog(String filename){
		String path = System.getProperty("user.dir");
		MTULogUtils.saveLogToFolder(path, filename, ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME, defaultlevel, true);
	}
	
	public FileLog(String filename, MTULogLevel loglevel){
		String path = System.getProperty("user.dir");
		MTULogUtils.saveLogToFolder(path, filename, ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME, loglevel, true);
		defaultlevel=loglevel;	
	}
	
	
	public FileLog(String savetofolder, String filename, MTULogLevel loglevel) {
		MTULogUtils.saveLogToFolder(savetofolder, filename, ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME, loglevel, true);
		defaultlevel=loglevel;		
	}
	

	public FileLog(String savetofolder, String filename, MTULogLevel loglevel, boolean stdoutoutput) {
		MTULogUtils.saveLogToFolder(savetofolder, filename, ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME, loglevel, stdoutoutput);
		defaultlevel=loglevel;
	}
	
	
	
	    @Override
		public void propertyChange(PropertyChangeEvent evt) {
			String cmd =evt.getPropertyName();
			if(cmd.equals(LogMessageCenter.INFO_MESSSAGE)){
				if(currentclass!=null)
					MTULogUtils.addInfoMsgToClass(currentclass, String.valueOf(evt.getNewValue()));
				else
					MTULogUtils.addInfoMsg(String.valueOf(evt.getNewValue()));	
			}
			else if(cmd.equals(LogMessageCenter.DEBUG_MESSAGE)){
				if(currentclass!=null)
					MTULogUtils.addDebugMsgToClass(currentclass, String.valueOf(evt.getNewValue()));
				else
					MTULogUtils.addDebugMsg(String.valueOf(evt.getNewValue()));
				
			}
			else if(cmd.equals(LogMessageCenter.TRACE_MESSAGE)){
				if(currentclass!=null)
					MTULogUtils.addTraceMsgToClass(currentclass, String.valueOf(evt.getNewValue()));
				else
					MTULogUtils.addTraceMsg(String.valueOf(evt.getNewValue()));
				
			}
			else if(cmd.equals(LogMessageCenter.ERROR_MESSAGE) || cmd.equals(LogMessageCenter.CRITICAL_ERROR_MESSAGE))
				if(currentclass!=null)
					MTULogUtils.addErrorMsgToClass(currentclass, String.valueOf(evt.getNewValue()));
				else
					MTULogUtils.addErrorMsg(String.valueOf(evt.getNewValue()));
			else if(cmd.equals(LogMessageCenter.THROWABLE_MESSAGE)){
				Throwable e =(Throwable) evt.getNewValue();
				if(usestacktrace)
					e.printStackTrace();
				else{
					if(currentclass!=null)
						MTULogUtils.addErrorMsgToClass(currentclass, e.getMessage());
					else
						MTULogUtils.addErrorMsg(e.getMessage());
				}
			}
				 
		}


		@Override
		public MTULogLevel getmaxLogLevel() {
			return MTULogLevel.TRACE;
		}

		@Override
		public MTULogLevel getDefaultLevel() {
			return defaultlevel;
		}

		@Override
		public synchronized void setCurrentClass(Class<?> currentclass) {
			this.currentclass=currentclass;
		}

		@Override
		public void enableStackTrace(boolean enable) {
			usestacktrace=enable;
			
		}

}
