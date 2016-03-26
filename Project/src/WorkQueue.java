import java.util.ArrayList;

public class WorkQueue
{
    private final int nThreads;
    private final PoolWorker[] threads;
    private final ArrayList<Runnable> queue;
    private volatile boolean shutdown = false;

    public WorkQueue(int nThreads){
        this.nThreads = nThreads;
        queue = new ArrayList<Runnable>();
        threads = new PoolWorker[nThreads];
        
        
        for (int i=0; i<nThreads; i++) {		
            threads[i] = new PoolWorker();
            threads[i].start();
        }

    }

    public void queueSize(){
    	System.out.println("queue size: " + queue.size());
    }
    
    public void execute(Runnable r) {
        synchronized(queue) {
            queue.add(r);
            queue.notify();
        }
    }

    public void shutdown(){
    	shutdown = true;
    	
    	synchronized(queue){
    		queue.notifyAll();
    	}
    }
    
    public void awaitTermination(){
//    	wait until all threads finish
    	for (Thread t : threads){
    		try{
    			t.join();
    		} catch(InterruptedException e){
    			System.err.println("interrupted join..");;
    		}
    	}
    }
    
    private class PoolWorker extends Thread {
    	int i=0;
        public void run() {
            Runnable r = null;
            while (true) {
                synchronized(queue) {
                	
                    while (queue.isEmpty() && !shutdown) {
                        try
                        {
//                        	System.out.println("waiting job #" + i++);
                        	queue.wait();
                        }
                        catch (InterruptedException ignored)
                        {
                        	System.err.println("interrupted wait..");
                        }
                    }

                    if(queue.isEmpty() && shutdown){
//                    	System.out.println("breaking");
                    	break;
                    }
//                    System.out.println(!queue.isEmpty() && shutdown || !queue.isEmpty() && !shutdown);
                    if(!queue.isEmpty() && shutdown || !queue.isEmpty() && !shutdown){
                    	r = (Runnable) queue.remove(0);
                    }
                }

                // If we don't catch RuntimeException, 
                // the pool could leak threads
                try {
//                	System.out.println("run thread...");
//                	System.out.println("running #" + i++);
                    r.run();
                }
                catch (RuntimeException e) {
                    // You might want to log something here
                	System.err.println("runtime exception on " + i);
                }
            }
        }
    }
}