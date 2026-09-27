package mt2_threads_creation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultiExecutor {

	private List<Runnable> tasks;
	private ExecutorService executorService;
	
	/* 
	 * @param tasks to executed concurrently
	 */
	public MultiExecutor(List<Runnable> tasks) {
		this.tasks = tasks;
		// Create a thread pool with size equal to number of tasks for maximum parallelism
		// or use available processors if tasks.size() is very large
		int availableProcessors = Runtime.getRuntime().availableProcessors();
		System.err.println("availableProcessors: " + availableProcessors);
		int threadPoolSize = Math.min(tasks.size(), availableProcessors);
		this.executorService = Executors.newFixedThreadPool(threadPoolSize);
	}

	/**
	 * Starts and executes all the tasks concurrently
	 */
	public void executeAll() {
		try {
			// Submit all tasks to the executor
			for (Runnable task : tasks) {
				executorService.submit(task);
			}
			
			// Shutdown the executor and wait for all tasks to complete
			executorService.shutdown();
			
//			List<Runnable> shutdownNow2 = executorService.shutdownNow();
//			System.out.println("*******shutdownNow(); number of tasks left: " + shutdownNow2.size());
			
//			if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
			if (!executorService.awaitTermination(60, TimeUnit.MILLISECONDS)) {
				System.out.println("*******awaitTermination");
				List<Runnable> shutdownNow = executorService.shutdownNow();
				System.out.println("*******shutdownNow(); number of tasks left: " + shutdownNow.size());
//				if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
				if (!executorService.awaitTermination(6, TimeUnit.MILLISECONDS)) {
					System.err.println("Executor did not terminate");
				}
			}
		} catch (InterruptedException e) {
			// Re-interrupt the thread and shutdown the executor
			Thread.currentThread().interrupt();
			executorService.shutdownNow();
			System.err.println("Execution was interrupted: " + e.getMessage());
		}
	}
	
	/**
	 * Shutdown the executor service
	 */
	public void shutdown() {
		if (executorService != null && !executorService.isShutdown()) {
			executorService.shutdown();
		}
	}

	public static void main(String[] args) {
		Long timestamp1 = System.currentTimeMillis();
		List<Runnable> tasks = new ArrayList<>();
		
		for (int i = 0; i < 20; i++) {
			final int taskId = i;
			tasks.add(new Runnable() {
				@Override
				public void run() {
					System.out.println("Task " + taskId + " executed by thread " + Thread.currentThread().getName());
					try {
						Thread.sleep(1000);
						// just added to observe interruption
//						Thread.sleep(10);
//						int j=0;
//						while (j<10_000) {
//							if (j%10 == 0) {
//								System.out.print(".");
//							}
//							j++;
//						}
//						System.out.println();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						System.err.println("Task " + taskId + " was interrupted");
					}
				}
			});
		}
		
		MultiExecutor multiExecutor = new MultiExecutor(tasks);
		multiExecutor.executeAll();
		
		Long timestamp2 = System.currentTimeMillis();
		System.out.println("Time taken: " + (timestamp2 - timestamp1) + " ms");
		
		System.out.println("\n\n");
		
		shutdownDemo(tasks);
		
		System.out.println("main done");
	}
	
	
	/**
	 * Demonstrates {@link ExecutorService#shutdown()} behavior with a fixed thread pool of 2.
	 *
	 * <p><b>Setup:</b> 4 tasks submitted to a 2-thread pool, then shutdown, then 1 more submit.</p>
	 *
	 * <p><b>Key behaviors:</b></p>
	 * <ul>
	 *   <li>Tasks 0,1 run immediately; Tasks 2,3 are <i>queued</i></li>
	 *   <li>{@code shutdown()} returns immediately — does NOT block main thread</li>
	 *   <li>Queued tasks (2,3) still complete after {@code shutdown()}</li>
	 *   <li>Task 4 submitted after {@code shutdown()} throws {@link java.util.concurrent.RejectedExecutionException}</li>
	 *   <li>Queued task execution order is <i>not guaranteed</i></li>
	 * </ul>
	 *
	 * <p><b>To block until all tasks finish:</b></p>
	 * <pre>
	 * es.shutdown();
	 * es.awaitTermination(10, TimeUnit.SECONDS);
	 * </pre>
	 *
	 * @param runnables list of at least 5 tasks; index 4 is intentionally rejected
	 * @throws java.util.concurrent.RejectedExecutionException (REE) when task submitted after shutdown
	 */
	private static void shutdownDemo(List<Runnable> runnables) {
		ExecutorService es = Executors.newFixedThreadPool(2);

		es.submit(runnables.get(0)); // running
		es.submit(runnables.get(1)); // running  
		es.submit(runnables.get(2)); // queued
		es.submit(runnables.get(3)); // queued

		es.shutdown();    // ← returns IMMEDIATELY
		                  // main thread moves on right here
		                  // but T1, T2, T3, T4 are still executing in background!

		es.submit(runnables.get(4)); // ❌ RejectedExecutionException

		System.out.println("shutdownDemo done"); // prints before tasks finish!
	}
}