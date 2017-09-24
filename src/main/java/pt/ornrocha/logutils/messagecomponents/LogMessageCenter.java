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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.math.R.Logger;

import pt.ornrocha.logutils.MTULogLevel;
import pt.ornrocha.printutils.MTUPrintUtils;

public class LogMessageCenter implements Logger{
	
	public static final String CRITICAL_ERROR_MESSAGE="critical_error_message";
	public static final String ERROR_MESSAGE="error_message";
	public static final String WARN_MESSAGE="warn_message";
	public static final String INFO_MESSSAGE="info_message";
	public static final String DEBUG_MESSAGE="debug_message";
	public static final String TRACE_MESSAGE="trace_message";
	public static final String THROWABLE_MESSAGE="throwable_message";
	
	public static final String TASK_STATUS_MESSSAGE="task_status_message";
	public static final String SUBTASK_STATUS_MESSSAGE="subtask_status_message";
	public static final String TASK_PROGRESS_MESSSAGE="task_progress_message";
	
	static LogMessageCenter center=null;
	
	//protected boolean debug=false;
	private boolean enabled=true;
	private MTULogLevel currentLevel=MTULogLevel.INFO;
	private MTULogLevel defaultlevel;
	private MTULogLevel maxLevel;
	private final LinkedHashMap<MTULogLevel, Integer> loglevelscale=getLogLevelScale();
	private Class<?> currentlogclass=null;
	private HashSet<Class<?>> classzs=new HashSet<>();
	private HashSet<Class<?>> allowedlogclass;
	private ILogProgressListener currentloglistener;
	private boolean ignoreclass=false;
	private ArrayList<PropertyChangeListener> listeners=new ArrayList<>();

	protected PropertyChangeSupport propertyChangeSupport;
	
	synchronized public static LogMessageCenter getLogger(){
		if(center==null)
			center=new LogMessageCenter();
		
		return center;
	}
	
	
	protected LogMessageCenter(){
		this.propertyChangeSupport=new PropertyChangeSupport(this);
		currentloglistener=new ConsolestdoutLog();
		setLogProgressListener(currentloglistener);
	}
	
	private final LinkedHashMap<MTULogLevel, Integer> getLogLevelScale(){
		LinkedHashMap<MTULogLevel, Integer> loglevelscale=new LinkedHashMap<>();
		loglevelscale.put(MTULogLevel.ERROR, 1);
		loglevelscale.put(MTULogLevel.WARNING, 2);
		loglevelscale.put(MTULogLevel.INFO, 3);
		loglevelscale.put(MTULogLevel.DEBUG, 4);
		loglevelscale.put(MTULogLevel.TRACE, 5);
		
		return loglevelscale;
	}
	
	
	public synchronized void setLogProgressListener(ILogProgressListener plistener){
		
		removeListener(plistener);
		this.currentloglistener=plistener;
		addListener(currentloglistener);
		
		this.defaultlevel=plistener.getDefaultLevel();
		this.currentLevel=plistener.getDefaultLevel();
		this.maxLevel=plistener.getmaxLogLevel();
	}
	
	public void addStatusLogProgressListener(IStatusProgressListener statlistener) {
		addListener(statlistener);
	}
	
	public void removeStatusLogProgressListener(IStatusProgressListener statlistener) {
		removeListener(statlistener);
	}
	
	
	protected void addListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
		listeners.add(listener);
	}
	
	
	protected void removeListener(PropertyChangeListener listener) {
		if(listeners.size()>0 && listeners.contains(listener)) {
			propertyChangeSupport.removePropertyChangeListener(listener);
			listeners.remove(listener);
		}
	}
	
	
	
	private boolean msgAllowedByLevel(MTULogLevel necessaryLevel){
		int neclev=loglevelscale.get(necessaryLevel);
		int curlev=loglevelscale.get(currentLevel);
		
		if(neclev<=curlev)
			return true;
		return false;
	}
	
	public void setLogLevel(MTULogLevel level){
		
		if(level.equals(MTULogLevel.OFF))
			disableLog();
		else{
			enableLog();
			if(loglevelscale.get(level)<=loglevelscale.get(maxLevel))
				this.currentLevel=level;
			else{
				System.out.println("This level is not supported by the current log listener, using default level ["+currentLevel+"]");
				this.currentLevel=defaultlevel;
			}
		}
			
	}
	
	private void resetLoggerClasses(){
		this.currentlogclass=null;
		if(currentloglistener!=null)
			currentloglistener.setCurrentClass(null);
	}
  
	public void enableStackTrace(){
		currentloglistener.enableStackTrace(true);
	}
	
	public void disableStackTrace(){
		currentloglistener.enableStackTrace(false);
	}
	
	public void disableLog(){
		this.enabled=false;
	}
	
	public void enableLog(){
		this.enabled=true;
	}
	
	public boolean isEnabled(){
		return enabled;
	}
	
	public MTULogLevel getLogLevel(){
		return currentLevel;
	}
	
	public LogMessageCenter toClass(Class<?> classz){
		classzs.add(classz);
		if(classz!=null){
			this.currentlogclass=classz;
			this.ignoreclass=false;
			if(currentloglistener!=null)
				currentloglistener.setCurrentClass(classz);
		}
		return this;
	}
	
	public final PropertyChangeSupport getPropertyChangeSupport() {
		return propertyChangeSupport;
	}
	
	protected synchronized final void firePropertyChange(String propertyName, Object oldValue,Object newValue) {
		    if(ignoreclass)
		    	currentloglistener.setCurrentClass(null);
		
			getPropertyChangeSupport().firePropertyChange(propertyName,oldValue, newValue);
			resetLoggerClasses();
    }
	
	protected synchronized final void firePropertyChange(String propertyName, Object newValue) {
		    if(ignoreclass)
		    	currentloglistener.setCurrentClass(null);
			firePropertyChange(propertyName, null, newValue);
			resetLoggerClasses();
	}
	
	public synchronized final void viewMessage(String msg){
		if(!msg.isEmpty()){
			firePropertyChange(INFO_MESSSAGE, msg);
		}
	}
	
	public synchronized final void addTaskStatusMessage(String msg){
		if(msg!=null && !msg.isEmpty() && listeners.size()>1)
			firePropertyChange(TASK_STATUS_MESSSAGE, null,msg);
	}
	
	public synchronized final void addSubTaskStatusMessage(String msg){
		if(msg!=null && !msg.isEmpty() && listeners.size()>1)
			firePropertyChange(SUBTASK_STATUS_MESSSAGE, null,msg);
	}
	
	public synchronized final void addTaskProgressMessage(Float progress){
		if(progress!=null && listeners.size()>1)
			firePropertyChange(TASK_PROGRESS_MESSSAGE, null,progress);
	}
	
	public synchronized final void addInfoMessage(String msg){
		if(enabled && !msg.isEmpty() && msgAllowedByLevel(MTULogLevel.INFO) && msgCanBeDisplayed())
		  firePropertyChange(INFO_MESSSAGE, msg);
	}
	
	public synchronized final void addInfoMessage(String msg, Object object, String delimiter){
		if(enabled && !msg.isEmpty() && msgAllowedByLevel(MTULogLevel.INFO) && msgCanBeDisplayed()){
			String objectstring=MTUPrintUtils.getObjectToString(object, delimiter);
			msg=checkMsgObjectSpace(msg,object)+objectstring;
			firePropertyChange(INFO_MESSSAGE, msg);
		}
	}
	
	public synchronized final void addInfoMessage(String msg, Object object){
		addInfoMessage(msg, object, null);
	}
	
	public synchronized final void addInfoMessage(String msg, Object key, Object value,String delimiter){
		if(enabled && !msg.isEmpty() && msgAllowedByLevel(MTULogLevel.INFO) && msgCanBeDisplayed()){
			String keystring=MTUPrintUtils.getObjectToString(key,delimiter);
			String valuestring=MTUPrintUtils.getObjectToString(value,delimiter);
			
			msg=msg+" key: "+keystring+" value: "+valuestring;
			firePropertyChange(INFO_MESSSAGE, msg);
		}
	}
	
	public synchronized final void addWarnMessage(String msg){
		if(enabled && !msg.isEmpty() && msgAllowedByLevel(MTULogLevel.WARNING) && msgCanBeDisplayed())
		  firePropertyChange(WARN_MESSAGE, msg);
	}
	
	public synchronized final void addWarnMessage(String msg, Object object, String delimiter){
		if(enabled && !msg.isEmpty() && msgAllowedByLevel(MTULogLevel.WARNING) && msgCanBeDisplayed()){
			String objectstring=MTUPrintUtils.getObjectToString(object, delimiter);
			msg=checkMsgObjectSpace(msg,object)+objectstring;
			firePropertyChange(WARN_MESSAGE, msg);
		}
	}
	
	public synchronized final void addWarnMessage(String msg, Object object){
		addWarnMessage(msg, object, null);
	}

	
	/*public synchronized final void addInfoMessage(String msg, Object key, Object value){
		addInfoMessage(msg, key, value, null);
	}*/
	
	
	public synchronized final void addDebugMessage(String msg){
		if(!msg.isEmpty() && msgAllowedByLevel(MTULogLevel.DEBUG) && msgCanBeDisplayed())
		  firePropertyChange(DEBUG_MESSAGE, msg);
	}
	
	public synchronized final void addDebugMessage(String msg, Object object, String delimiter){
		if(enabled && !msg.isEmpty() && msgAllowedByLevel(MTULogLevel.DEBUG) && msgCanBeDisplayed()){
			String objectstring=MTUPrintUtils.getObjectToString(object, delimiter);
			msg=checkMsgObjectSpace(msg,object)+objectstring;
			firePropertyChange(DEBUG_MESSAGE, msg);
		}
	}
	
	public synchronized final void addDebugMessage(String msg, Object object){
		addDebugMessage(msg, object, "\n");
	}
	
	
	public synchronized final void addDebugMessage(String msg, Object key, Object value,String delimiter){
		if(enabled && !msg.isEmpty() && msgAllowedByLevel(MTULogLevel.DEBUG) && msgCanBeDisplayed()){
			String keystring=MTUPrintUtils.getObjectToString(key,delimiter);
			String valuestring=MTUPrintUtils.getObjectToString(value,delimiter);
			
			msg=msg+" key: "+keystring+" value: "+valuestring;
			firePropertyChange(DEBUG_MESSAGE, msg);
		}
	}
	
/*	public synchronized final void addDebugMessage(String msg, Object key, Object value){
		addDebugMessage(msg, key, value, null);
	}*/
	
	public synchronized final void addTraceMessage(String msg){
		if(!msg.isEmpty() && msgAllowedByLevel(MTULogLevel.TRACE) && msgCanBeDisplayed())
		  firePropertyChange(TRACE_MESSAGE, msg);
	}
	
	public synchronized final void addTraceMessage(String msg, Object object, String delimiter){
		if(enabled && !msg.isEmpty() && msgAllowedByLevel(MTULogLevel.TRACE) && msgCanBeDisplayed()){
			String objectstring=MTUPrintUtils.getObjectToString(object, delimiter);
			msg=checkMsgObjectSpace(msg,object)+objectstring;
			firePropertyChange(TRACE_MESSAGE, msg);
		}
	}
	
	public synchronized final void addTraceMessage(String msg, Object object){
		addTraceMessage(msg, object, null);
	}
	
	public synchronized final void addTraceMessage(String msg, Object key, Object value,String delimiter){
		if(enabled && !msg.isEmpty() && msgAllowedByLevel(MTULogLevel.TRACE) && msgCanBeDisplayed()){
			String keystring=MTUPrintUtils.getObjectToString(key,delimiter);
			String valuestring=MTUPrintUtils.getObjectToString(value,delimiter);
			
			msg=msg+" key: "+keystring+" value: "+valuestring;
			firePropertyChange(TRACE_MESSAGE, msg);
		}
	}
	
	/*public synchronized final void addTraceMessage(String msg, Object key, Object value){
		addTraceMessage(msg, key, value, null);
	}*/
	
	public synchronized final void addErrorMessage(String msg){
		if(enabled && msg!=null && !msg.isEmpty() && msgAllowedByLevel(MTULogLevel.ERROR) && msgCanBeDisplayed())
		   firePropertyChange(ERROR_MESSAGE, msg);
	}
	
	public synchronized final void addErrorMessage(String msg, Throwable e){
		if(enabled && !msg.isEmpty() && msgAllowedByLevel(MTULogLevel.ERROR) && msgCanBeDisplayed()){
			firePropertyChange(ERROR_MESSAGE, msg);
			firePropertyChange(THROWABLE_MESSAGE, e);
		}
	}
	
	public synchronized final void addErrorMessage(Throwable e){
		if(e!=null && enabled)
		   firePropertyChange(THROWABLE_MESSAGE, e);
	}
	
	public synchronized final void addCriticalErrorMessage(String msg, Throwable e){
		firePropertyChange(CRITICAL_ERROR_MESSAGE, msg);
		firePropertyChange(THROWABLE_MESSAGE, e);
	}
	
	
	public synchronized final void addCriticalErrorMessage(String msg){
		firePropertyChange(CRITICAL_ERROR_MESSAGE, msg);
	}
	
	
	
	public synchronized final void addCriticalErrorMessage(Throwable e){
		if(e!=null && enabled)
			   firePropertyChange(THROWABLE_MESSAGE, e);
	}
	
	public synchronized final void addMessageToPropertyName(String propertyname, Object value){
		if(enabled)
			firePropertyChange(propertyname, null, value);
	}
	
	
	public synchronized final void addInfoSeparator(String name,Integer numberemptylinesbefore, Integer numberemptylinesafter){
		addInfoMessage(configureSeparator(name,numberemptylinesbefore,numberemptylinesafter));
	}
	
	public synchronized final void addInfoSeparator(String name){
		addInfoMessage(configureSeparator(name,null,null));
	}
	
	public synchronized final void addDebugSeparator(String name,Integer numberemptylinesbefore, Integer numberemptylinesafter){
		addDebugMessage(configureSeparator(name,numberemptylinesbefore,numberemptylinesafter));
	}
	
	public synchronized final void addDebugSeparator(String name){
		addDebugMessage(configureSeparator(name,null,null));
	}
	
	public synchronized final void addTraceSeparator(String name,Integer numberemptylinesbefore, Integer numberemptylinesafter){
		addTraceMessage(configureSeparator(name,numberemptylinesbefore,numberemptylinesafter));
	}
	
	public synchronized final void addTraceSeparator(String name){
		addTraceMessage(configureSeparator(name,null,null));
	}
	
	protected String configureSeparator(String name, Integer newlinebefore, Integer newlineafter){
		String separator=null;
		String spacebefore="\n\n";
		String spaceafter="\n";
		
		if(newlinebefore!=null)
			spacebefore=StringUtils.repeat("\n", newlinebefore);
		if(newlineafter!=null)
			spaceafter=StringUtils.repeat("\n", newlineafter);
		
		if(name!=null){
			separator=spacebefore+"############### "+name+" ###############"+spaceafter;
			if(currentlogclass!=null){
				separator=spacebefore+"############### "+name+" [in class: "+currentlogclass.getSimpleName()+"] ###############"+spaceafter;
				ignoreclass=true;
			}
		}
		else{
			separator=spacebefore+"########################################"+spaceafter;
			
			if(currentlogclass!=null)
			   ignoreclass=true;
			
		}
		
		
		return separator;
	}
	
	
	private String checkMsgObjectSpace(String msg, Object object){
		if(object==null)
			return msg+" ";
		else{
			if((object instanceof Map) ||
					(object instanceof List) ||
						(object instanceof Set))
				return msg+"\n";
			else
				return msg+" ";
				
		}
	}
	
	
/*	private void clearCurrentLogClass(){
		this.currentlogclass=null;
	}*/
	
	

	
	public void restrictLogToClasses(Class<?>...classes){
		this.allowedlogclass=new HashSet<>();
		for (Class<?> inclass : classes) {
			allowedlogclass.add(inclass);
		}
	}
	
	
	private boolean msgCanBeDisplayed(){
		if(currentlogclass!=null && allowedlogclass!=null){
			if(allowedlogclass.contains(currentlogclass))
				return true;
			else
				return false;
		}
		return true;
	}
	

	@Override
	public void println(String message, Level level) {
		if (level == Level.ERROR) {
           addErrorMessage(message);
        }
		/*else if(msgAllowedByLevel(MTULogLevel.INFO) && (level ==Level.INFO || level==Level.WARNING))
			addInfoMessage(message);*/
		else if(msgAllowedByLevel(MTULogLevel.DEBUG))
			addDebugMessage(message);
       // p.println(string);
		
	}

	@Override
	public void close() {
		
		
	}

}
