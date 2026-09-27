package mt3_thread_coordination;

/*
The answer you selected is incorrect because calling thread.interrupt(); only sends a signal to the thread that it should stop—it's not an immediate
 termination command. The thread, in this case, is designed to sleep for an extended period and will remain in that state unless it checks for interruption.
  When the thread detects that it has been interrupted (when it catches the InterruptedException), it does not stop automatically; you'll need to include
   logic in the catch block to handle this signal appropriately.

Understanding how thread interruption works is key. It involves cooperative thread management, where the thread must explicitly check for interruption signals
 and decide to stop accordingly. This concept is vital for effective thread handling in Java, ensuring that your applications can respond gracefully to
  interruptions rather than halting abruptly.
 */

public class Quiz2ThreadTermination {

    public static void main(String [] args) {
        Thread thread = new Thread(new SleepingThread());
        thread.start();
        thread.interrupt();
    }
 
    private static class SleepingThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                	if (!Thread.currentThread().isInterrupted()) {
                		Thread.currentThread().interrupt(); // restore interrupt status
                	}
                    Thread.sleep(1000000);
                } catch (InterruptedException e) {
                	// here Interrupted flag is cleared
                	System.out.println(Thread.currentThread().isInterrupted());
                	System.err.println("InterruptedException");
//                	return;  // without this, the thread will run indefinitely
                }
            }
        }
    }
	
}
