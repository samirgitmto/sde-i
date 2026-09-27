package xtracapfintech;

import java.util.concurrent.CountDownLatch;

public class MoneyTransferTests {

	
	
	public static void transferMoneyTest1(Account account) throws InterruptedException {
		CountDownLatch startLatch = new CountDownLatch(1);

		Runnable transfer = () -> {
		    try {
		        startLatch.await(); // all threads wait here

		        // Read balance
		        int balance = account.getBalance();

		        // Simulate some processing
		        Thread.sleep(100);

		        // Transfer 500
		        account.setBalance(balance - 500);

		        System.out.println(
		            Thread.currentThread().getName()
		            + " completed transfer"
		        );

		    } catch (InterruptedException e) {
		        Thread.currentThread().interrupt();
		    }
		};

		Thread t1 = new Thread(transfer, "T1");
		Thread t2 = new Thread(transfer, "T2");
		Thread t3 = new Thread(transfer, "T3");

		t1.start();
		t2.start();
		t3.start();

		
//		this will cause a DEADLOCK
//		t1.join();
//		t2.join();
//		t3.join();
		
		// Release all three
		startLatch.countDown();
		
		t1.join();
		t2.join();
		t3.join();
	}
	
	public static void main(String[] args) {
		Account account = new Account(1000);
		try {
			transferMoneyTest1(account);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.err.println(account.getBalance());
	}
	
	public static class Account {
		private int balance;
		public Account(int bal) {
			this.balance = bal;
		}
		
		public void setBalance(int bal) {
			this.balance = bal;
		}
		public int getBalance() {
			return this.balance;
		}
	}
}
