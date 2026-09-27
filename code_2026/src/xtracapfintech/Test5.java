package xtracapfintech;

public class Test5 {

	static void printByThreads0(int x) throws InterruptedException {
		
		int[] i = {1};
		Object lock = new Object();
		
		Runnable runnable1 = new Runnable() {
			@Override
			public void run() {
				synchronized (lock) {
					while (i[0] < x) {
						if (i[0] < x && i[0] % 2 == 0) {
							System.out.println(i[0]);
							i[0]++;
							Thread.currentThread();
							if (Thread.holdsLock(lock)) {
								Thread.currentThread().notify();
							}
						}
						
						try {
							if (Thread.currentThread().holdsLock(lock))
								Thread.currentThread().wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
		Runnable runnable2 = new Runnable() {
			@Override
			public void run() {
				synchronized (lock) {
					while (i[0] < x) {
						if (i[0] < x && i[0] % 2 != 0) {
							System.out.println(i[0]);
							i[0]++;
							Thread.currentThread();
							if (Thread.holdsLock(lock))
								Thread.currentThread().notify();
						}
						
						try {
							if (Thread.currentThread().holdsLock(lock))
								Thread.currentThread().wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
		
		Thread t1 = new Thread(runnable1);
		Thread t2 = new Thread(runnable2);
		
		t1.start();
		t2.start();
	}
	
	static void printByThreads1(int x) throws InterruptedException {
		
		int[] i = {1};
		Object lock = new Object();
		
		Runnable runnable1 = new Runnable() {
			@Override
			public void run() {
				synchronized (lock) {
					while (i[0] < x) {
						if (i[0] < x && i[0] % 2 == 0) {
							System.out.println(i[0]);
							i[0]++;
//							Thread.currentThread();
//							if (Thread.holdsLock(lock)) {
//								Thread.currentThread().notify();
//							}
							
							try {
								wait();
							} catch (Exception e) {
							}
						}
						
						if (Thread.currentThread().holdsLock(lock))
//								Thread.currentThread().wait();
						Thread.currentThread().notify();
					}
				}
			}
		};
		Runnable runnable2 = new Runnable() {
			@Override
			public void run() {
				synchronized (lock) {
					while (i[0] < x) {
						if (i[0] < x && i[0] % 2 != 0) {
							System.out.println(i[0]);
							i[0]++;
//							Thread.currentThread();
//							if (Thread.holdsLock(lock))
//								Thread.currentThread().notify();
							try {
								wait();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						
						if (Thread.currentThread().holdsLock(lock))
//								Thread.currentThread().wait();
						Thread.currentThread().notify();
					}
				}
			}
		};
		
		Thread t1 = new Thread(runnable1);
		Thread t2 = new Thread(runnable2);
		
		t1.start();
		t2.start();
	}
	
	static void printByThreads(int x) throws InterruptedException {
	    
	    int[] i = {1};
	    Object lock = new Object();
	    
	    Runnable runnable1 = new Runnable() {
	        @Override
	        public void run() {
	            synchronized (lock) {
	                while (i[0] <= x) {
	                    if (i[0] <= x && i[0] % 2 == 0) {
	                        System.out.println(Thread.currentThread().getName() + ": " + i[0]);
	                        i[0]++;
	                        lock.notify();  // Notify on lock, not on Thread.currentThread()
	                    }
	                    
	                    if (i[0] <= x) {
	                        try {
	                            lock.wait();  // Wait on lock, not on Thread.currentThread()
	                        } catch (InterruptedException e) {
	                            Thread.currentThread().interrupt();
	                            e.printStackTrace();
	                        }
	                    }
	                }
	                lock.notify(); // Ensure the other thread can exit if needed
	            }
	        }
	    };
	    
	    Runnable runnable2 = new Runnable() {
	        @Override
	        public void run() {
	            synchronized (lock) {
	                while (i[0] <= x) {
	                    if (i[0] <= x && i[0] % 2 != 0) {
	                        System.out.println(Thread.currentThread().getName() + ": " + i[0]);
	                        i[0]++;
	                        lock.notify();  // Notify on lock
	                    }
	                    
	                    if (i[0] <= x) {
	                        try {
	                            lock.wait();  // Wait on lock
	                        } catch (InterruptedException e) {
	                            Thread.currentThread().interrupt();
	                            e.printStackTrace();
	                        }
	                    }
	                }
	                lock.notify(); // Ensure the other thread can exit if needed
	            }
	        }
	    };
	    
	    Thread t1 = new Thread(runnable1, "EvenThread");
	    Thread t2 = new Thread(runnable2, "OddThread");
	    
	    t1.start();
	    t2.start();
	    
//	    t1.join(); // Wait for threads to finish
//	    t2.join();
	}
	
	public static void main(String[] args) throws InterruptedException {
		
//		printByThreads(10);
		
		printByThreadsV1(10);
		
		System.err.println("DONE...................");
	}

	private static void printByThreadsV1(int i) throws InterruptedException {
		
		int[] counter = {1};
		
		Object lock = new Object();
		
		Thread t1 = new Thread(() -> {
			synchronized (lock) {
				while (counter[0] <= i) {
					if (counter[0] % 2 == 0) {
						System.out.println(Thread.currentThread().getName() + " - " + counter[0]);
						counter[0]++;
						lock.notify();
					}
					else {
						try {
							lock.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}, "Even Thread");
		
		Thread t2 = new Thread(() -> {
			synchronized (lock) {
				while (counter[0] <= i) {
					if (counter[0] % 2 != 0) {
						System.out.println(Thread.currentThread().getName() + " - " + counter[0]);
						counter[0]++;
						lock.notify();
					}
					else {
						try {
							lock.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}, "Odd Thread");
		
		t1.start();
		t2.start();
		
		t1.join();
		t2.join();
		
	}
	
	
}
