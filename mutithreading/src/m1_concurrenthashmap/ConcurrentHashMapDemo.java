package m1_concurrenthashmap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConcurrentHashMapDemo {

	/**
	 * CME demo
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		Map<String, Integer> hashMap = new HashMap<>();

		// Single-threaded put (safe)
		hashMap.put("A", 1);
		hashMap.put("B", 2);
		System.out.println(hashMap); // {A=1, B=2}

		// Multi-threaded unsafe example (May cause issues)
		Runnable task = () -> {
			String threadPrefix = Thread.currentThread().getName(); // Unique prefix per thread
		    for (int i = 0; i < 1000; i++) {
		    	hashMap.put(threadPrefix + "-Key" + i, i); // Unique keys
		    }
		};

		Thread t1 = new Thread(task);
		Thread t2 = new Thread(task);

		t1.start();
		t2.start();

		t1.join();
		t2.join();
		// May throw `ConcurrentModificationException` or corrupt data
		// won't because keys are different

		System.out.println("hashMap.size(): " + hashMap.size());

		// Thread modifying while iterating → Exception!
        new Thread(() -> {
            try {
                Thread.sleep(50); // Simulate delay
                hashMap.put("C", 3); // Modify while iterating
                System.out.println("put done: " + hashMap.get("C"));
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.err.println(e.getClass());
            }
        }).start();
		
		Iterator<Map.Entry<String, Integer>> it = hashMap.entrySet().iterator();
        while (it.hasNext()) {
            // Throws ConcurrentModificationException
        	try {
//        		System.out.println(it.next());
        		it.next();    // this may not cause CME due to speed difference
        	} catch (Exception e) {
        		e.printStackTrace();
        		System.err.println(e.getClass());
//        		System.exit(1);
			}
        }
		

		ConcurrentMap<String, Integer> concurrentMap = new ConcurrentHashMap<>();

		// Multi-threaded safe operations
		Runnable task2 = () -> {
			String threadPrefix = Thread.currentThread().getName(); // Unique prefix per thread
		    for (int i = 0; i < 1000; i++) {
		        concurrentMap.put(threadPrefix + "-Key" + i, i); // Unique keys
		    }
		};

		Thread t11 = new Thread(task2);
		Thread t12 = new Thread(task2);

		t11.start();
		t12.start();

		try {
			t11.join();
			t12.join();
		} catch (InterruptedException e) {
			System.err.println(e.getMessage());
		}

		System.out.println("concurrentHashMap.size(): " + concurrentMap.size()); // ~2000 (Thread-safe)
		
		Iterator<Map.Entry<String, Integer>> it2 = concurrentMap.entrySet().iterator();
		while (it2.hasNext()) {
			it2.next();
		}
		
		
		// runAsync uses ForkJoinPool.commonPool() by default
		// Those threads are DAEMON threads
		// JVM won't wait for these to finish
		CompletableFuture.runAsync(() -> {
			try {
				Thread.sleep(100);
				System.out.println("woken");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
/**
// Option 1: block explicitly
CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
    ...
});
future.join(); // main thread blocks here until done

// Option 2: chain and join
CompletableFuture.runAsync(() -> { ... }).join();

// Option 3: add artificial delay (hacky, never do in prod)
Thread.sleep(200); // Hope 100ms task finishes — unreliable!		
 */
		
	}

}