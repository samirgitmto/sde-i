/*
 * Custom Multithreading Examples
 * Created for learning and practice purposes
 * Based on Michael Pogrebinsky's Top Developer Academy course
 */
package mt2_threads_creation;

public class UncaughtExceptionHandler {

    public static void main(String [] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //Code that will run in a new thread
                throw new RuntimeException("Intentional Exception");
//                throw new Exception("Intentional Exception");  Since `run()` declares **no checked exceptions**, you **cannot throw a checked exception** from it.
            }
        });

        thread.setName("Misbehaving-thread");

        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("A critical error happened in thread " + t.getName()
                        + "; the error is " + e.getMessage());
            }
        });
        thread.start();

    }

}
