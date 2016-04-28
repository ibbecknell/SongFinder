package project2_multithreading;
import java.util.ArrayList;

/**
 * Class that implements a threadpool using a work queue
 * 
 * @author ibbecknell
 *
 */
public class WorkQueue {
	private final int nThreads;
	private final PoolWorker[] threads;
	private final ArrayList<Runnable> queue;
	private volatile boolean shutdown = false;

	/**
	 * Constructor for work queue to begin a threadpool of a given number of
	 * threads
	 * 
	 * @param nThreads
	 */
	public WorkQueue(int nThreads) {
		this.nThreads = nThreads;
		queue = new ArrayList<Runnable>();
		threads = new PoolWorker[this.nThreads];

		for (int i = 0; i < this.nThreads; i++) {
			threads[i] = new PoolWorker();
			threads[i].start();
		}

	}

	/**
	 * Helper method that prints the size of the job queue
	 */
	public void queueSize() {
		System.out.println("queue size: " + queue.size());
	}

	/**
	 * Method to add jobs to the queue and notify the waiting thread to begin
	 * working
	 * 
	 * @param r
	 */
	public void execute(Runnable r) {
		if (!shutdown) {
			synchronized (queue) {
				queue.add(r);
				queue.notify();
			}
		}
	}

	/**
	 * Method to shutdown the queue and notify all waiting threads
	 */
	public void shutdown() {
		shutdown = true;

		synchronized (queue) {
			queue.notifyAll();
		}
	}

	/**
	 * Method to wait until all threads finish working
	 */
	public void awaitTermination() {
		// wait until all threads finish
		for (Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				System.err.println("interrupted join..");
			}
		}
	}

	/**
	 * Private inner class that runs executes a thread
	 * 
	 * @author ibbecknell
	 *
	 */
	private class PoolWorker extends Thread {
		/**
		 * Method to run a thread and check if the shutdown flag has been
		 * called. executes the work of parsing a file.
		 */
		public void run() {
			Runnable r;
			while (true) {
				synchronized (queue) {

					while (queue.isEmpty() && !shutdown) {
						try {
							queue.wait();
						} catch (InterruptedException ignored) {
							System.err.println("interrupted wait..");
						}
					}

					if (queue.isEmpty() && shutdown) {
						break;
					}
					r = (Runnable) queue.remove(0);
				}

				// If we don't catch RuntimeException,
				// the pool could leak threads
				try {
					r.run();
				} catch (RuntimeException e) {
					// You might want to log something here
					System.err.println("runtime exception on Thread " + Thread.currentThread().getId());
				}
			}
		}
	}
}
