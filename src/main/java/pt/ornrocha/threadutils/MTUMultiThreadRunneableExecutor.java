package pt.ornrocha.threadutils;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MTUMultiThreadRunneableExecutor implements ProcessShutdowner{
	
	
	
	 private  ThreadPoolExecutor executor = null;
	 private ArrayList<Runnable> lisprocesses=null;

	 public MTUMultiThreadRunneableExecutor(ArrayList<Runnable> listprocesses, Integer nproc){
		 createThreadPool(nproc);
		 this.lisprocesses=listprocesses;
	 }
	 
	 
	

	 private void createThreadPool(Integer numberprocesses){
		 
		 int nsimproc=1;
		 if(numberprocesses==null)
		    nsimproc=Runtime.getRuntime().availableProcessors();
		 else if(numberprocesses!=null && numberprocesses<=0)
			 nsimproc=Runtime.getRuntime().availableProcessors();
		 else
			 nsimproc=numberprocesses;
		 executor=(ThreadPoolExecutor) Executors.newFixedThreadPool(nsimproc);
	 }
	 
	 
	 public void run()  throws InterruptedException{
		 
			 for (int i = 0; i < lisprocesses.size(); i++) {
				 executor.execute(lisprocesses.get(i));
			 }
		 
			 executor.shutdown();
		
			 executor.awaitTermination(2, TimeUnit.MINUTES);
		/* while (!executor.isTerminated()) {
			Thread.sleep(1000);
		 }*/
		 
	 }
	 
	 public void shutdownProcessesNow(){
		 executor.shutdownNow();
	 }
	 
	 
	 public static void execute(ArrayList<Runnable> listprocesses) throws InterruptedException{
		 MTUMultiThreadRunneableExecutor executor=new MTUMultiThreadRunneableExecutor(listprocesses, null);
		 executor.run();
	 }
	 
	 public static void execute(ArrayList<Runnable> listprocesses, Integer nprocs) throws InterruptedException{
		 MTUMultiThreadRunneableExecutor executor=new MTUMultiThreadRunneableExecutor(listprocesses, nprocs);
		 executor.run();
	 }



	@Override
	public void closeProcess() {
		shutdownProcessesNow();
		
	}

}
