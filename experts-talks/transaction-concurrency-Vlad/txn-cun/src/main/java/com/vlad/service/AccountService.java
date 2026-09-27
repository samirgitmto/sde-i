package com.vlad.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vlad.model.Account;
import com.vlad.repository.AccountRepository;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
//import jakarta.transaction.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;


@Service
@Transactional
public class AccountService {
    
    private final AccountRepository accountRepository;
    
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    
//    read → check → update operation
//    @Transactional
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)   // best case for ACID violation
//    @Transactional(isolation = Isolation.READ_COMMITTED)   // READ_COMMITTED fixes certain dirty-read problems, but it doesn't automatically make your read → check → update operation atomic.
    public void transfer(String fromIban, String toIban, Long transferCents) {
        
    	String txInfo = getCurrentTransactionInfo();
        String threadInfo = Thread.currentThread().getName();
        
        System.out.println("[" + txInfo + "-" + threadInfo + "] Starting transfer...");
    	
    	// Check if accounts exist
        Account fromAccount = accountRepository.findByIban(fromIban)
                .orElseThrow(() -> new IllegalArgumentException("Can't find account with IBAN: " + fromIban));
        
        System.out.println("[" + txInfo + "-" + threadInfo + "] Read balance: " + fromAccount.getBalance());
        
        Account toAccount = accountRepository.findByIban(toIban)
                .orElseThrow(() -> new IllegalArgumentException("Can't find account with IBAN: " + toIban));
        
        // Check sufficient balance
        if (fromAccount.getBalance() < transferCents) {
            System.err.println("[" + txInfo + "-" + threadInfo + "] INSUFFICIENT BALANCE");
            throw new IllegalArgumentException("Insufficient balance in account: " + fromIban);
        }
        
        // Perform transfer
        accountRepository.updateBalance(fromIban, -transferCents);
        accountRepository.updateBalance(toIban, transferCents);
        
        System.out.println("[" + txInfo + "-" + threadInfo + "] Transfer completed");

    }
    
    
    // fixed version
    public void transferWithPessimisticLocking(String fromIban, String toIban, Long transferCents) {
        
    	String txInfo = getCurrentTransactionInfo();
        String threadInfo = Thread.currentThread().getName();
        
        System.out.println("[" + txInfo + "-" + threadInfo + "] Starting transfer...");
    	
    	// Check if accounts exist
        Account fromAccount = accountRepository.findByIbanForUpdate(fromIban)
                .orElseThrow(() -> new IllegalArgumentException("Can't find account with IBAN: " + fromIban));
        
        System.out.println("[" + txInfo + "-" + threadInfo + "] Read balance: " + fromAccount.getBalance());
        
        Account toAccount = accountRepository.findByIbanForUpdate(toIban)
                .orElseThrow(() -> new IllegalArgumentException("Can't find account with IBAN: " + toIban));
        
        // Check sufficient balance
        if (fromAccount.getBalance() < transferCents) {
            System.err.println("[" + txInfo + "-" + threadInfo + "] INSUFFICIENT BALANCE");
            throw new IllegalArgumentException("Insufficient balance in account: " + fromIban);
        }
        
        // Perform transfer
        accountRepository.updateBalance(fromIban, -transferCents);
        accountRepository.updateBalance(toIban, transferCents);
        
        System.out.println("[" + txInfo + "-" + threadInfo + "] Transfer completed");

    }
    
    
    // Alternative implementation using entity manipulation
    public void transferWithEntities(String fromIban, String toIban, Long transferCents) {
        Account fromAccount = accountRepository.findByIban(fromIban)
                .orElseThrow(() -> new IllegalArgumentException("Can't find account with IBAN: " + fromIban));
        
        Account toAccount = accountRepository.findByIban(toIban)
                .orElseThrow(() -> new IllegalArgumentException("Can't find account with IBAN: " + toIban));
        
        if (fromAccount.getBalance() < transferCents) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        
        fromAccount.setBalance(fromAccount.getBalance() - transferCents);
        toAccount.setBalance(toAccount.getBalance() + transferCents);
        
        accountRepository.saveAll(List.of(fromAccount, toAccount));
    }
    
    public Long getBalance(String iban) {
        return accountRepository.findBalanceByIban(iban)
                .orElseThrow(() -> new IllegalArgumentException("Can't find account with IBAN: " + iban));
    }
    
    
    private String getCurrentTransactionInfo() {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            return "NO-ACTIVE-TRANSACTION";
        }
        
        String transactionName = TransactionSynchronizationManager.getCurrentTransactionName();
        boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        String isolationLevel = String.valueOf(TransactionSynchronizationManager.getCurrentTransactionIsolationLevel());
        
        return String.format("TX[%s-readOnly:%s-isolation:%s]", 
            transactionName, readOnly, isolationLevel);
    }
    
}