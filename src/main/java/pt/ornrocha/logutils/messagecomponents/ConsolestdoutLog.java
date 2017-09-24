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

public class ConsolestdoutLog implements ILogProgressListener{

	   
	    private Class<?> currentclass;
	    private static String CLASSMSG="In class [";
	    private boolean usestacktrace=false;
	    
	    @Override
		public void propertyChange(PropertyChangeEvent evt) {
			String cmd =evt.getPropertyName();
			if(cmd.equals(LogMessageCenter.INFO_MESSSAGE) || cmd.equals(LogMessageCenter.DEBUG_MESSAGE)){
				if(currentclass!=null)
					System.out.println(CLASSMSG+currentclass.getSimpleName()+"], "+evt.getNewValue());
				else
					System.out.println(evt.getNewValue());
			}
			else if(cmd.equals(LogMessageCenter.TRACE_MESSAGE))
				if(currentclass!=null)
					System.out.println(CLASSMSG+currentclass.getSimpleName()+"], "+evt.getNewValue());
				else
					System.out.println(evt.getNewValue());
			else if(cmd.equals(LogMessageCenter.ERROR_MESSAGE)|| cmd.equals(LogMessageCenter.CRITICAL_ERROR_MESSAGE))
				if(currentclass!=null)
					System.err.println(CLASSMSG+currentclass.getSimpleName()+"] An error has occurred, "+evt.getNewValue());
				else
					System.err.println("An error has occurred: "+evt.getNewValue());
			
			else if(cmd.equals(LogMessageCenter.THROWABLE_MESSAGE)){
				Throwable e =(Throwable) evt.getNewValue();
				if(currentclass!=null)
					System.err.println(CLASSMSG+currentclass.getSimpleName()+"] An error has occurred: ");
				if(usestacktrace)
					e.printStackTrace();
				else
					System.err.println(e.getMessage());
			}
			else if(cmd.equals(LogMessageCenter.WARN_MESSAGE)){
				System.out.println(evt.getNewValue());
			}
				 
		}


		@Override
		public MTULogLevel getmaxLogLevel() {
			return MTULogLevel.TRACE;
		}

		@Override
		public MTULogLevel getDefaultLevel() {
			return MTULogLevel.INFO;
		}


		@Override
		public synchronized void setCurrentClass(Class<?> currentclass) {
			this.currentclass=currentclass;
		}


		@Override
		public void enableStackTrace(boolean enable) {
             usestacktrace=true;
		}
	 

}
