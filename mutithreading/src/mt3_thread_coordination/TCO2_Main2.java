/*
 * Custom Multithreading Examples
 * Created for learning and practice purposes
 * Based on Michael Pogrebinsky's Top Developer Academy course
 */
package mt3_thread_coordination;

import java.math.BigInteger;

/**
 * ThreadInterruptionDemo and the interrupted FLAG
 */
public class TCO2_Main2 {

    public static void main(String[] args) throws InterruptedException {

		demonstrateCpuBoundInterruption();
    	demonstrateBlockingInterruption();
	
    	demonstrateThreadInterrupted();
    	demonstrateFlagClearedBeforeCheck();
    }

    /**
     * Demonstrates cooperative interruption for a CPU-bound (non-blocking) task.
     *
     * Key concept: thread.interrupt() only SETS the interrupt flag — it does NOT forcefully stop the thread.
     * The thread must voluntarily check the flag via Thread.currentThread().isInterrupted() and decide to stop itself.
     *
     * This is different from blocking methods (e.g. Thread.sleep, Object.wait), which automatically throw InterruptedException when the flag is set.
     * CPU-bound loops have no such mechanism, so cooperative checking is the only way to make them interruptible.
     * @throws InterruptedException only because of join() call
     */
	private static void demonstrateCpuBoundInterruption() throws InterruptedException {
		Thread thread = new Thread(new LongComputationTask(new BigInteger("200000"), new BigInteger("100000000")));
        thread.start();
        thread.interrupt();
        System.err.println(thread.isInterrupted());
        thread.join();
	}

    private static class LongComputationTask implements Runnable {
        private BigInteger base;
        private BigInteger power;

        public LongComputationTask(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            System.out.println(base + "^" + power + " = " + pow(base, power));
        }

        /**
         * Computes base^power iteratively, with cooperative interruption support.
         *
         * On each iteration, isInterrupted() is polled. If the interrupt flag has been set, the computation is abandoned early
         * and BigInteger.ZERO is returned as a sentinel value.
         *
         * Note: isInterrupted() reads the flag WITHOUT clearing it. 
         * If you need to clear the flag (e.g. to propagate it or re-raise it), use the static
         *   Thread.interrupted() instead — it reads AND clears the flag.
         *
         * @param base  the base value
         * @param power the exponent (must be non-negative)
         * @return the result of base^power, or BigInteger.ZERO if interrupted early
         */
        private BigInteger pow(BigInteger base, BigInteger power) {
            BigInteger result = BigInteger.ONE;

            for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i = i.add(BigInteger.ONE)) {
                if (Thread.currentThread().isInterrupted()) {
//                if (Thread.interrupted()) {
                    System.out.println("Prematurely interrupted computation");
                    // true for instance method isInterrupted(), but false for static method interrupted() as the flag gets cleared automatically
                    System.err.println("After call inside the method block : " + Thread.currentThread().isInterrupted());
                    return BigInteger.ZERO;
                }
                result = result.multiply(base);
            }

            return result;
        }
    }
    
    /**
     * Demonstrates that blocking methods (e.g. Thread.sleep) respond to
     * the interrupt flag by throwing InterruptedException AND clearing the flag.
     *
     * Two things happen atomically when sleep detects the interrupt:
     *   1. InterruptedException is thrown  — giving you a chance to react
     *   2. The flag is cleared             — so it won't auto-throw again
     *
     * This is fundamentally different from a CPU-bound loop, where nothing is thrown automatically
     *  — the flag just sits there until you poll it.
     * @throws InterruptedException because of sleep() call.
     */
    private static void demonstrateBlockingInterruption() throws InterruptedException {
        Thread t = new Thread(() -> {
            System.out.println("\n****** Blocking method interruption demo");
            try {
                System.out.println("About to sleep... flag before: " + Thread.currentThread().isInterrupted()); // false
                Thread.sleep(5000);
                System.out.println("This line never runs");
            } catch (InterruptedException e) {
                // We are here because sleep THREW — not because we polled.
                // sleep already cleared the FLAG before throwing.
                System.out.println("InterruptedException thrown by sleep");
                System.out.println("Flag after throw: " + Thread.currentThread().isInterrupted()); // false — already cleared
            }
        });

        t.start();
        Thread.sleep(100); // let the thread reach sleep()
        t.interrupt();     // causes sleep() to throw InterruptedException
        t.join();
    }
    
    /**
     * Thread.interrupted() - static method, CLEARING.
     *
     * Reads the interrupt flag AND resets it to false.
     * Must be called from INSIDE the thread to be meaningful —
     * it always checks the CURRENT thread, not a specific one.
     */
    private static void demonstrateThreadInterrupted() throws InterruptedException {
        Thread t = new Thread(() -> {
            // self-interrupt to simulate receiving an interrupt signal
            Thread.currentThread().interrupt();

            System.out.println("\n****** Thread.interrupted() demo");
            System.out.println("First call:  " + Thread.interrupted());  // true  — flag was set
            System.out.println("Second call: " + Thread.interrupted());  // false — flag was CLEARED by first call
        });

        t.start();
        t.join();
    }

    /**
     * The subtle bug: flag cleared BEFORE your code checks it.
     *
     * If a blocking call (e.g. sleep, wait) throws InterruptedException,
     * it clears the flag as part of throwing. If you catch and swallow
     * the exception, the interrupt is silently lost.
     *
     * Fix: always re-interrupt in the catch block to restore the flag.
     */
    private static void demonstrateFlagClearedBeforeCheck() throws InterruptedException {
        Thread t = new Thread(() -> {
            System.err.println("\n*******Swallowed interrupt bug");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // BAD: silently swallowed — flag is now false
                System.out.println("Caught InterruptedException, but swallowed it");
            }

            // flag is lost — this prints false
            System.out.println("isInterrupted() after swallowed catch: " + Thread.currentThread().isInterrupted());

            // --- correct pattern ---
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // GOOD to restore the flag so callers can observe it
                Thread.currentThread().interrupt();
                System.out.println("Re-interrupted after catch");
            }

            // flag is preserved — this prints true
            System.out.println("isInterrupted() after re-interrupt: " + Thread.currentThread().isInterrupted());
        });

        t.start();
        Thread.sleep(100);
        t.interrupt(); // triggers first sleep
        Thread.sleep(200);
        t.interrupt(); // triggers second sleep
        t.join();
    }
}
