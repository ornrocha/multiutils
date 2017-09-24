package pt.ornrocha.threadutils;

public class AbortProcessExecuter implements Runnable{

	
	private ProcessShutdowner process;
	
	
	public AbortProcessExecuter(ProcessShutdowner process){
		this.process=process;
	}

	@Override
	public void run() {
		process.closeProcess();
		
	}
	  
	
}
