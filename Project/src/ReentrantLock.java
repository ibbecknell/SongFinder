import java.util.HashMap;

/**
 * A read/write lock that allows multiple readers, disallows multiple writers,
 * and allows a writer to acquire a read lock while holding the write lock.
 * 
 */
public class ReentrantLock {
	// map id -> number of read locks held
	private HashMap<Long, Integer> readLocks;
	// map id -> number of write locks held
	private HashMap<Long, Integer> writeLocks;

	/**
	 * Construct a new ReentrantLock.
	 */
	public ReentrantLock() {
		readLocks = new HashMap<Long, Integer>();
		writeLocks = new HashMap<Long, Integer>();
	}

	/**
	 * Returns true if the invoking thread holds a read lock.
	 * 
	 * @return
	 */
	public synchronized boolean hasRead() {
		return readLocks.containsKey(Thread.currentThread().getId());
	}

	/**
	 * Returns true if the invoking thread holds a write lock.
	 * 
	 * @return
	 */
	public synchronized boolean hasWrite() {
		return writeLocks.containsKey(Thread.currentThread().getId());
	}

	/**
	 * Non-blocking method that attempts to acquire the read lock. Returns true
	 * if successful.
	 * 
	 * @return
	 */
	public synchronized boolean tryLockRead() {
		if (writeLocks.size() == 0
				|| (writeLocks.size() >= 1 && writeLocks.containsKey(Thread.currentThread().getId()))) {
			if (readLocks.get(Thread.currentThread().getId()) == null) {
				readLocks.put(Thread.currentThread().getId(), 1);
			} else {
				Integer count = readLocks.get(Thread.currentThread().getId());
				count++;
				readLocks.put(Thread.currentThread().getId(), count);
			}
			return true;
		}
		return false;
	}

	/**
	 * Non-blocking method that attempts to acquire the write lock. Returns true
	 * if successful.
	 * 
	 * @return
	 */
	public synchronized boolean tryLockWrite() {
		if ((readLocks.size() == 0 && writeLocks.size() == 0)
				|| (writeLocks.size() >= 1 && writeLocks.containsKey(Thread.currentThread().getId()))) {
			if (writeLocks.get(Thread.currentThread().getId()) == null) {
				writeLocks.put(Thread.currentThread().getId(), 1);
			} else {
				Integer count = writeLocks.get(Thread.currentThread().getId());
				count++;
				writeLocks.put(Thread.currentThread().getId(), count);
			}
			return true;
		}

		return false;
	}

	/**
	 * Blocking method that will return only when the read lock has been
	 * acquired.
	 */
	public synchronized void lockRead() {
		while (!tryLockRead()) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

		}
	}

	/**
	 * Releases the read lock held by the calling thread. Other threads may
	 * continue to hold a read lock.
	 */
	// http://tutorials.jenkov.com/java-concurrency/read-write-locks.html
	public synchronized void unlockRead() {
		Integer count = readLocks.get(Thread.currentThread().getId());
		if (readLocks.containsKey(Thread.currentThread().getId())) {
			if (readLocks.get(Thread.currentThread().getId()) == 1) {
				readLocks.remove(Thread.currentThread().getId());
			} else {
				readLocks.put(Thread.currentThread().getId(), count - 1);
			}

		}
		notifyAll();
	}

	/**
	 * Blocking method that will return only when the write lock has been
	 * acquired.
	 */
	public synchronized void lockWrite() {
		while (!tryLockWrite()) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	/**
	 * Releases the write lock held by the calling thread. The calling thread
	 * may continue to hold a read lock.
	 */
	// http://tutorials.jenkov.com/java-concurrency/read-write-locks.html
	public synchronized void unlockWrite() {
		Integer count = writeLocks.get(Thread.currentThread().getId());
		if (writeLocks.containsKey(Thread.currentThread().getId())) {
			if (writeLocks.get(Thread.currentThread().getId()) == 1) {
				writeLocks.remove(Thread.currentThread().getId());
			} else {
				writeLocks.put(Thread.currentThread().getId(), count - 1);
			}
		}
		notifyAll();
	}
}
