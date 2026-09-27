Starting threads
[TX[com.vlad.service.AccountService.transfer-readOnly:false-isolation:null]-Thread-2] Starting transfer...
[TX[com.vlad.service.AccountService.transfer-readOnly:false-isolation:null]-Thread-3] Starting transfer...
[TX[com.vlad.service.AccountService.transfer-readOnly:false-isolation:null]-Thread-4] Starting transfer...
[TX[com.vlad.service.AccountService.transfer-readOnly:false-isolation:null]-Thread-2] Read balance: 100
[TX[com.vlad.service.AccountService.transfer-readOnly:false-isolation:null]-Thread-4] Read balance: 100
[TX[com.vlad.service.AccountService.transfer-readOnly:false-isolation:null]-Thread-3] Read balance: 100
[TX[com.vlad.service.AccountService.transfer-readOnly:false-isolation:null]-Thread-1] Starting transfer...
[TX[com.vlad.service.AccountService.transfer-readOnly:false-isolation:null]-Thread-1] Read balance: 100
[TX[com.vlad.service.AccountService.transfer-readOnly:false-isolation:null]-Thread-2] Transfer completed
[TX[com.vlad.service.AccountService.transfer-readOnly:false-isolation:null]-Thread-4] Transfer completed
[TX[com.vlad.service.AccountService.transfer-readOnly:false-isolation:null]-Thread-3] Transfer completed
[TX[com.vlad.service.AccountService.transfer-readOnly:false-isolation:null]-Thread-1] Transfer completed
Alice's balance -100
Bob's balance 200
[TX[com.vlad.service.AccountService.transfer-readOnly:false-isolation:null]-main] Starting transfer...
[TX[com.vlad.service.AccountService.transfer-readOnly:false-isolation:null]-main] Read balance: 100
[TX[com.vlad.service.AccountService.transfer-readOnly:false-isolation:null]-main] INSUFFICIENT BALANCE
[TX[com.vlad.service.AccountService.transfer-readOnly:false-isolation:null]-main] Starting transfer...
[TX[com.vlad.service.AccountService.transfer-readOnly:false-isolation:null]-main] Read balance: 100
[TX[com.vlad.service.AccountService.transfer-readOnly:false-isolation:null]-main] Transfer completed




Based on your output, here are the **exact problems** demonstrated:

## 1. **Stale Read Problem**
```
[TX...-Thread-2] Read balance: 100
[TX...-Thread-4] Read balance: 100  
[TX...-Thread-3] Read balance: 100
[TX...-Thread-1] Read balance: 100
```
**Problem:** All 4 threads read the same initial balance (100) because they don't see each other's uncommitted changes.

## 2. **Lost Update Problem**
```
[TX...-Thread-2] Transfer completed  // Alice: 100-50=50
[TX...-Thread-4] Transfer completed  // Alice: 50-50=0  
[TX...-Thread-3] Transfer completed  // Alice: 0-50=-50
[TX...-Thread-1] Transfer completed  // Alice: -50-50=-100
```
**Problem:** Each update overwrites the previous one. Only the last update (-100) persists, losing the intermediate updates.

## 3. **Non-Repeatable Read Problem**
The balance changes during each transaction's execution, but the threads don't re-read the balance after their initial read.

## 4. **Write Skew Problem**
```
Alice's balance -100
Bob's balance 200
```
**Problem:** The constraint "balance cannot be negative" is violated because each transaction only checks its local view of the balance.

## 5. **Isolation Level Inadequacy**
```
isolation:null
```
**Problem:** The default isolation level (READ_COMMITTED) only prevents dirty reads but allows:
- Non-repeatable reads ✓ (your case)
- Phantom reads ✓
- Lost updates ✓ (your case)

## 6. **Lack of Atomicity**
The transfer operation (check balance → debit → credit) is not atomic. Other transactions can interleave between these steps.

## 7. **No Concurrency Control**
No locking mechanism prevents multiple transactions from modifying the same data simultaneously.

## 8. **Business Rule Violation**
```
Alice's balance -100  // ❌ Negative balance not allowed
Bob's balance 200
```
**Problem:** The fundamental business rule "account balance cannot be negative" is violated.

## 9. **Transaction Interleaving**
The output shows transactions are interleaving arbitrarily:
```
Thread-2 starts → Thread-4 starts → Thread-3 starts → Thread-1 starts
Thread-2 completes → Thread-4 completes → Thread-3 completes → Thread-1 completes
```
This arbitrary interleaving leads to unpredictable results.

## 10. **No Serializability**
The final result (-100, 200) could never occur if the transactions executed sequentially (one after another). This violates serializability.

**Summary:** Your output demonstrates classic concurrency problems that occur when multiple transactions access shared data without proper isolation, locking, or consistency guarantees.