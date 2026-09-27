/*
 * Custom Multithreading Examples
 * Created for learning and practice purposes
 * Based on Michael Pogrebinsky's Top Developer Academy course
 */
package mt3_thread_coordination;

public class TCO1_Main1 {
    public static void main(String [] args) {
        Thread thread = new Thread(new BlockingTask());

//        thread.setDaemon(true);
        thread.start();
    }

    private static class BlockingTask implements Runnable {

        @Override
        public void run() {
            //do things
            try {
                Thread.sleep(500000);
            } catch (InterruptedException e) {
                System.out.println("Existing blocking thread");
            }
        }
    }
}
