package com.vlad;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.vlad.model.Account;
import com.vlad.repository.AccountRepository;
import com.vlad.service.AccountService;

@SpringBootTest
public class NoAcidTest {

    @Autowired
    private AccountService accountService;
    
    @Autowired
    private AccountRepository accountRepository;
    
    
    @BeforeEach
    void setUp() {
        // Reset accounts before each test
        accountRepository.deleteAll();
        accountRepository.save(new Account("ALICE-123", 100L)); // 1000.00
        accountRepository.save(new Account("BOB-456", 0L));  // 500.00
    }
    
//    @Test
    void testSingleTransferWorksCorrectly() {
        accountService.transfer("ALICE-123", "BOB-456", 30L);
        
        Long fromBalance = accountService.getBalance("ALICE-123");
        Long toBalance = accountService.getBalance("BOB-456");
        
        assertEquals(70L, fromBalance); // 100 - 30 = 70
        assertEquals(30L, toBalance);   // 0 + 30 = 30
    }
	
//    @Test
    void testSingleTransferInvalid() {
//        accountService.transfer("ALICE-123", "BOB-456", 300L);
        
        Long fromBalance = accountService.getBalance("ALICE-123");
        Long toBalance = accountService.getBalance("BOB-456");
        
     // Test that exception is thrown for non-existent accounts
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.transfer("ALICE-123", "BOB-456", 300L);
        });
//        assertEquals(70L, fromBalance);
//        assertEquals(30L, toBalance);
     // Verify the exception message
        assertTrue(exception.getMessage().contains("Insufficient balance in account:"));
    }
 
    
    
/**
 * Initial: Alice = 100, Bob = 0<p>
 * 
 * Time   Thread1              Thread2              Thread3              Thread4 <p>
 * T1     Read Alice: 100      Read Alice: 100      Read Alice: 100      Read Alice: 100 <p>
 * T2     Check: 100≥50 ✓      Check: 100≥50 ✓      Check: 100≥50 ✓      Check: 100≥50 ✓ <p>
 * T3     UPDATE Alice         UPDATE Alice         UPDATE Alice         UPDATE Alice <p>
       SET balance=100-50   SET balance=100-50   SET balance=100-50   SET balance=100-50  <p>
       (Alice now = 50)     (Alice now = 0)      (Alice now = -50)    (Alice now = -100) <p>
 * T4     UPDATE Bob           UPDATE Bob           UPDATE Bob           UPDATE Bob   <p>
       SET balance=0+50     SET balance=50+50    SET balance=100+50   SET balance=150+50  <p>
       (Bob now = 50)       (Bob now = 100)      (Bob now = 150)      (Bob now = 200)     <p>
 */
    
    // Faulty
//    @Test
    public void testParallelExecution() {
        // Setup initial balances
//        accountRepository.save(new Account("Alice-123", 10L));
//        accountRepository.save(new Account("Bob-456", 0L));
        
        assertEquals(100L, accountService.getBalance("ALICE-123"));
        assertEquals(0L, accountService.getBalance("BOB-456"));
        
        parallelExecution();
        
        System.out.println("Alice's balance " + accountService.getBalance("ALICE-123"));
        System.out.println("Bob's balance " + accountService.getBalance("BOB-456"));
    }
    
    int threadCount = 4;
    
    public void parallelExecution() {
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                awaitOnLatch(startLatch);
                
                try {
                	accountService.transfer("ALICE-123", "BOB-456", 50L);
                } catch (Exception e) {
                    System.out.println("Transfer failed: " + e.getMessage());
                }
                
                endLatch.countDown();
            }).start();
        }

        System.out.println("Starting threads");
        startLatch.countDown();
        awaitOnLatch(endLatch);
    }
    private void awaitOnLatch(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted", e);
        }
    }
    
    @Test
    public void testParallelExecutionWithPessimisticLocking() {
        // Setup initial balances
//        accountRepository.save(new Account("Alice-123", 10L));
//        accountRepository.save(new Account("Bob-456", 0L));
        
        assertEquals(100L, accountService.getBalance("ALICE-123"));
        assertEquals(0L, accountService.getBalance("BOB-456"));
        
        parallelExecutionWithPessimisticLocking();
        
        System.out.println("Alice's balance " + accountService.getBalance("ALICE-123"));
        System.out.println("Bob's balance " + accountService.getBalance("BOB-456"));
    }
    
    public void parallelExecutionWithPessimisticLocking() {
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                awaitOnLatch(startLatch);
                
                try {
                	accountService.transferWithPessimisticLocking("ALICE-123", "BOB-456", 50L);
                	
                } catch (Exception e) {
                    System.out.println("Transfer failed: " + e.getMessage());
                }
                
                endLatch.countDown();
            }).start();
        }

        System.out.println("Starting threads");
        startLatch.countDown();
        awaitOnLatch(endLatch);
    }
    
    
}
