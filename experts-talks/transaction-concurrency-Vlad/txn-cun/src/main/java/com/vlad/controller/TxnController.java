package com.vlad.controller;

import java.util.concurrent.CountDownLatch;

public class TxnController {

	
	public static void main(String[] args) throws InterruptedException {
        int numWorkers = 3;
        CountDownLatch latch = new CountDownLatch(numWorkers);

        for (int i = 0; i < numWorkers; i++) {
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " is working...");
//                    Thread.sleep(1000); // simulate work
                    Thread.sleep(1); // simulate work
                    System.out.println(Thread.currentThread().getName() + " finished.");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown(); // signal task is done
                }
            }).start();
        }

        System.out.println("Main thread waiting for workers...");
        latch.await(); // wait until all workers finish
        System.out.println("All workers finished. Main thread continues.");
    }
	
}
