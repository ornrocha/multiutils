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

import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.util.StatusPrinter;

public class MTULogUtils {
	
	
	 public synchronized static void addDebugMsg(String msg, Object ... in){
		   Logger logger=getRootLogger();
		   if(logger.isDebugEnabled()){
			   if(in!=null)
				   logger.debug(msg, in);
			   else
				   logger.debug(msg);
		   }
	   }
	 
	 public synchronized static void addDebugMsg(String msg){
		   Object[] args=null;
		   addDebugMsg(msg, args);
	    }
	
	
	public synchronized static void addDebugMsg(Logger logger, String msg, Object ... in){
		   if(logger.isDebugEnabled()){
			   logger.debug(msg, in);
		   }
	   }

	   public synchronized static void addDebugMsg(Logger logger, String msg){
		   if(logger.isDebugEnabled()){
			   logger.debug(msg);
		   }
	   }
	   
	   
	   public synchronized static void addDebugMsgToClass(Class<?> klass, String msg, Object ... in){
		   final Logger logger = LoggerFactory.getLogger(klass.getSimpleName());
		   addDebugMsg(logger, msg, in);
	   }
	   
	   public synchronized static void addDebugMsgToClass(Class<?> klass, String msg){
		   final Logger logger = LoggerFactory.getLogger(klass.getSimpleName());
		   addDebugMsg(logger, msg);  
	   }
	   
	   public synchronized static void addInfoMsg(String msg, Object ... in){
		   Logger logger=getRootLogger();
		   if(logger.isDebugEnabled() || logger.isInfoEnabled()){
			   if(in!=null)
				   logger.info(msg, in);
			   else
				   logger.info(msg);
		   }
	   }
	   
	   
	   public synchronized static void addInfoMsg(String msg){
		   Object[] args=null;
		   addInfoMsg(msg, args);
	    }
	   
	   
	   public synchronized static void addInfoMsg(Logger logger, String msg, Object ... in){
		   if(logger.isDebugEnabled() || logger.isInfoEnabled()){
			   logger.info(msg, in);
		   }
	   }

	   public synchronized static void addInfoMsg(Logger logger, String msg){
		   if(logger.isDebugEnabled() || logger.isInfoEnabled()){
			   logger.info(msg);
		   }
	   }
	   
	   public synchronized static void addInfoMsgToClass(Class<?> klass, String msg, Object ... in){
		   final Logger logger = LoggerFactory.getLogger(klass.getSimpleName());
		   addInfoMsg(logger, msg, in);
	   }
	   
	   public synchronized static void addInfoMsgToClass(Class<?> klass, String msg){
		   if(klass!=null){
			  final Logger logger = LoggerFactory.getLogger(klass.getSimpleName());
		      addInfoMsg(logger, msg);  
		   }
	   }
	   
	   
	   public synchronized static void addErrorMsg(String msg, Object ... in){
		   Logger logger=getRootLogger();
		   if(logger.isErrorEnabled()){
			   if(in!=null)
				   logger.error(msg, in);
			   else
				   logger.error(msg);
		   }
	   }
	   
	   public synchronized static void addErrorMsg(String msg){
		   Object[] args=null;
		   addErrorMsg(msg, args);
	    }
	   
	   public synchronized static void addErrorMsg(Logger logger, String msg, Object ... in){
		   if(logger.isErrorEnabled()){
			   logger.error(msg, in);
		   }
	   }

	   public synchronized static void addErrorMsg(Logger logger, String msg){
		   if(logger.isErrorEnabled()){
			   logger.error(msg);
		   }
	   }
	   
	   
	   
	   public synchronized  static void addErrorMsgToClass(Class<?> klass, String msg, Object ... in){
		   final Logger logger = LoggerFactory.getLogger(klass.getSimpleName());
		   addErrorMsg(logger, msg, in);
	   }
	   
	   public synchronized static void addErrorMsgToClass(Class<?> klass, String msg){
		   final Logger logger = LoggerFactory.getLogger(klass.getSimpleName());
		   addErrorMsg(logger, msg);  
	   }
	   
	   public synchronized  static void addTraceMsg(String msg, Object ... in){
		   Logger logger=getRootLogger();
		   if(logger.isTraceEnabled()){
			   if(in!=null)
				   logger.trace(msg, in);
			   else
				   logger.trace(msg);
		   }
	   }
	 
	 public synchronized static void addTraceMsg(String msg){
		   Object[] args=null;
		   addDebugMsg(msg, args);
	    }
	
	
	public synchronized static void addTraceMsg(Logger logger, String msg, Object ... in){
		   if(logger.isTraceEnabled()){
			   logger.trace(msg, in);
		   }
	   }

	   public synchronized static void addTraceMsg(Logger logger, String msg){
		   if(logger.isTraceEnabled()){
			   logger.trace(msg);
		   }
	   }
	   
	   
	   public synchronized static void addTraceMsgToClass(Class<?> klass, String msg, Object ... in){
		   final Logger logger = LoggerFactory.getLogger(klass.getSimpleName());
		   addTraceMsg(logger, msg, in);
	   }
	   
	   public synchronized static void addTraceMsgToClass(Class<?> klass, String msg){
		   final Logger logger = LoggerFactory.getLogger(klass.getSimpleName());
		   addTraceMsg(logger, msg);  
	   }
	   
	   
	   
	   
	   
	   
	   public synchronized static boolean isLogDebug(Class<?> klass){
		   final Logger logger = LoggerFactory.getLogger(klass.getSimpleName());
		   return logger.isDebugEnabled();
	   }
	   
	   public synchronized static boolean isLogInfo(Class<?> klass){
		   final Logger logger = LoggerFactory.getLogger(klass.getSimpleName());
		   return logger.isInfoEnabled();
	   }
	   
	   public synchronized static void setLogLevel(MTULogLevel level){
		   Logger logger = LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
		   ((ch.qos.logback.classic.Logger) logger).setLevel(Level.toLevel(level.toString().toLowerCase()));
	   }
	   
	   public synchronized static boolean isLogEnabled(){
		   Logger logger = LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
		   if(logger.isErrorEnabled() || logger.isWarnEnabled() || logger.isTraceEnabled() || logger.isInfoEnabled() || logger.isDebugEnabled())
			   return true;
		   return false;
	   }
	   
	   public synchronized static Logger getRootLogger(){
		   return LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
	   }
	   
	   public synchronized static void setLogLevelForLogger(String loggername, MTULogLevel level){
		   Logger logger = LoggerFactory.getLogger(loggername);
		   ((ch.qos.logback.classic.Logger) logger).setLevel(Level.toLevel(level.toString().toLowerCase()));
	   }
	   
	   
	   public static void showLoggerInfoInConsole(String loggername, MTULogLevel level){
	
		   LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		   FileAppender fileAppender = new FileAppender();
		   ConsoleAppender console=new ConsoleAppender<>();
		   console.setContext(loggerContext);
		  // console.setName(logname);
		  // fileAppender.setFile(filename);
		   PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		    encoder.setContext(loggerContext);
		    encoder.setPattern("%r %thread %level - %msg%n");
		    encoder.start();

		    console.setEncoder(encoder);
		    console.start();
		    Logger logbackLogger =null;
		    if(loggername!=null){
		    	setLogLevelForLogger(loggername, level);
		        logbackLogger = loggerContext.getLogger(loggername);
		    }
		    else
		    	logbackLogger = loggerContext.getLogger("ROOT");
		    ((ch.qos.logback.classic.Logger) logbackLogger).addAppender(fileAppender);
		    StatusPrinter.print(loggerContext);
	   }
	   
	   
	   public static void saveLogToFolder(String folderpath, String logname, String loggername,MTULogLevel level,boolean showinconsole){
		   String filename=FilenameUtils.concat(folderpath, logname+".log");
		   LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		   FileAppender<ILoggingEvent> fileAppender = new FileAppender();
		   
	       if(!showinconsole){
		      Appender<ILoggingEvent> console=loggerContext.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME).getAppender("console");
		      if(console!=null)
		         loggerContext.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME).detachAppender("console");
	       }

		   
		     fileAppender.setContext(loggerContext);
		     fileAppender.setName(logname);
		     fileAppender.setFile(filename);
		     fileAppender.setAppend(false);
		    PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		    encoder.setContext(loggerContext);
		    encoder.setPattern("%r %thread %level - %msg%n");
		    encoder.start();

		    fileAppender.setEncoder(encoder);
		    fileAppender.setPrudent(true);
		    fileAppender.start();
		  

		    Logger logbackLogger =null;
		    if(loggername!=null){
		    	setLogLevelForLogger(loggername, level);
		        logbackLogger = loggerContext.getLogger(loggername);
		    }
		    else
		    	logbackLogger = loggerContext.getLogger("ROOT");
		    
		   ((ch.qos.logback.classic.Logger) logbackLogger).addAppender(fileAppender);
		   StatusPrinter.print(loggerContext);
		   
	   }
	   
	   public static void saveClassLogToFolder(String folderpath, Class<?> klass,MTULogLevel level, boolean showinconsole){
		   String name =klass.getSimpleName();
		   saveLogToFolder(folderpath, name, name, level,showinconsole);
	   }
	   
	   public static void saveLogOfClasses(String folderpath, ArrayList<Class<?>> klasses, MTULogLevel level, boolean showinconsole){
		   
		   for (int i = 0; i < klasses.size(); i++) {
			  Class<?> klass=klasses.get(i);
			  String name =klass.getSimpleName();
		      saveLogToFolder(folderpath, name, name, level,showinconsole);
		   } 
	   }
	   
     public static void saveLogOfLoggersNames(String folderpath, ArrayList<String> names, MTULogLevel level,boolean showinconsole){
		 
    	 if(names!=null)
		   for (int i = 0; i < names.size(); i++) {
			  String name =names.get(i);
		      saveLogToFolder(folderpath, name, name, level,showinconsole);
		   } 
	   }
     
    public static void ShowLogOfLoggersNames(ArrayList<String> names, MTULogLevel level){
		 
    	 if(names!=null)
		   for (int i = 0; i < names.size(); i++) {
			  String name =names.get(i);
			  showLoggerInfoInConsole(name, level);
		   } 
	   }
     
     
    public static MTULogLevel getLogLevel(String level){
    	
        if(level!=null){
 		  
        	if(level.toLowerCase().equals(MTULogLevel.ERROR.toString().toLowerCase()))
 			   return MTULogLevel.ERROR;
        	else if(level.toLowerCase().equals(MTULogLevel.INFO.toString().toLowerCase()))
 			   return MTULogLevel.INFO;
        	else if(level.toLowerCase().equals(MTULogLevel.DEBUG.toString().toLowerCase()))
 			   return MTULogLevel.DEBUG;
        	else if(level.toLowerCase().equals(MTULogLevel.TRACE.toString().toLowerCase()))
  			   return MTULogLevel.TRACE;
         }
 		return MTULogLevel.OFF;
 			
 	}

}
