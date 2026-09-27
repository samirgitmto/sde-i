package mt8_inter_thread_communication;

public class SimpleCountDownLatch {

	private int count;
	private final Object lock = new Object();

    public SimpleCountDownLatch(int count) {
        this.count = count;
        if (count < 0) {
            throw new IllegalArgumentException("count cannot be negative");
        }
    }

    /**
     * Causes the current thread to wait until the latch has counted down to zero.
     * If the current count is already zero then this method returns immediately.
    */
    public void await() throws InterruptedException {
        synchronized (lock) {
            while (count > 0) {
                lock.wait();
            }
        }
    }

    /**
     *  Decrements the count of the latch, releasing all waiting threads when the count reaches zero. 
     *  If the current count already equals zero then nothing happens.
     */
    public void countDown() {
        synchronized (lock) {
            if (count > 0) {
                count--;
                if (count == 0) {
                    lock.notifyAll();
                }
            }
        }
    }

    /**
     * Returns the current count.
    */
    public int getCount() {
        synchronized (lock) {
            return count;
        }
    }
	
}
