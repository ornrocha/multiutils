package pt.ornrocha.threadutils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;


public class MTUMultiThreadCallableExecutor {
	
	/*
	 *  This class is based on MulticoreExecutor.class of smile library http://haifengl.github.io/smile/index.html
	 */
	
	private static int nprocs = 1;
    /**
     * Thread pool.
     */
    private static ThreadPoolExecutor executor = null;


    /**
     * number of simultaneous threads, if nprocesses=0 the number of threads will be equal to the number of available processors.  
     * @param nprocesses
     */
    private static void createThreadPool(int nprocesses) {
        if (nprocesses >= 0) {
           
            if(nprocesses==0){
                int nprocessors = Runtime.getRuntime().availableProcessors();
                nprocs=nprocessors;
            }
            else{
            	nprocs=nprocesses;
            }
          
            if (nprocs > 1) {
                executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(nprocs);
            }
        }
        
        	
    }

    


    /**
     * Executes the given tasks serially or parallel depending on the number
     * of cores of the system. Returns a list of result objects of each task.
     * The results of this method are undefined if the given collection is
     * modified while this operation is in progress.
     * @param tasks the collection of tasks.
     * @return a list of result objects in the same sequential order as
     * produced by the iterator for the given task list.
     * @throws Exception if unable to compute a result.
     */
    public static <T> List<T> run(Collection<? extends Callable<T>> tasks, int nprocesses) throws Exception {
        createThreadPool(nprocesses);
        
        List<T> results = new ArrayList<>();
        if (executor == null) {
            for (Callable<T> task : tasks) {
                results.add(task.call());
            }
        } else {
            if (executor.getActiveCount() < nprocs) {
                List<Future<T>> futures = executor.invokeAll(tasks);
                for (Future<T> future : futures) {
                    results.add(future.get());
                }
            } else {
                // Thread pool is busy. Just run in the caller's thread.
                for (Callable<T> task : tasks) {
                    results.add(task.call());
                }
            }
        }
        
        close();
       // shutdown();
        return results;
    }
    
    /**
     * Shutdown the thread pool.
     */
    public static void shutdown() {
        if (executor != null) {
            executor.shutdown();
        }
    }
	
	
    protected static void close(){
    	
    	if(executor!=null){
    		executor.shutdown();
    		boolean waitclose=true;
    		while (waitclose) {
    			try{
    				waitclose=!executor.awaitTermination(5, TimeUnit.SECONDS);
    				if(waitclose)
    					LogMessageCenter.getLogger().addTraceMessage("Waiting to finish "+executor.getActiveCount()+" processes");
    			}catch (InterruptedException e) {
    				LogMessageCenter.getLogger().addTraceMessage("Interruped while awaiting completion of callback threads - trying again...");
			
    			}
    		}
    	}
    }

	
	
	
	
	
	

}
