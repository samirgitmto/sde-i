# Data Races Quiz: Interleaved Execution Analysis

## Code Under Analysis

```java
public static void main(String[] args) {
    SharedClass sharedClass = new SharedClass();
    
    Thread thread1 = new Thread(() -> sharedClass.method1());
    Thread thread2 = new Thread(() -> sharedClass.method2());

    thread1.start();
    thread2.start();
}

private static class SharedClass {
    int a = 0;
    int b = 0;

    public void method1() {
        int local1 = a;  // Step 1
        this.b = 1;      // Step 2
    }

    public void method2() {
        int local2 = b;  // Step 3
        this.a = 2;      // Step 4
    }       
}
```

## Question: Can we get `local1 = 2` and `local2 = 1`?

## Answer: **YES, this is possible through interleaved execution**

---

## Initial State
```
a = 0
b = 0
```

## Target Result
```
local1 = 2
local2 = 1
```

## Execution Scenario Analysis

### **Step-by-Step Interleaved Execution**

| Thread 1 | Thread 2 | Shared Variables | Local Variables |
|----------|----------|------------------|-----------------|
|          |          | a=0, b=0         |                 |
| local1 = a |         | a=0, b=0         | local1=0        |
|          | local2 = b | a=0, b=0         | local1=0, local2=0 |
|          | a = 2    | a=2, b=0         | local1=0, local2=0 |
| b = 1    |          | a=2, b=1         | local1=0, local2=0 |

**❌ This doesn't give us the target result!**

### **Correct Execution Order for Target Result**

| Thread 1 | Thread 2 | Shared Variables | Local Variables |
|----------|----------|------------------|-----------------|
|          |          | a=0, b=0         |                 |
|          | a = 2    | a=2, b=0         |                 |
| local1 = a |         | a=2, b=0         | local1=2        |
| b = 1    |          | a=2, b=1         | local1=2        |
|          | local2 = b | a=2, b=1         | local1=2, local2=1 |

**✅ This gives us the target result!**

## Detailed Explanation

### **Why This Scenario is Possible**

1. **No Synchronization**: The methods have no synchronization mechanisms
2. **Race Conditions**: Both threads can access shared variables concurrently
3. **Interleaved Execution**: CPU can switch between threads at any point
4. **Memory Visibility**: Changes are not guaranteed to be immediately visible

### **Execution Flow for Target Result**

```
Initial State: a=0, b=0

Thread 2 executes first:
  Step 4: a = 2  →  a becomes 2, b remains 0

Thread 1 executes:
  Step 1: local1 = a  →  local1 gets 2 (from Thread 2's update)
  Step 2: b = 1  →  b becomes 1, a remains 2

Thread 2 continues:
  Step 3: local2 = b  →  local2 gets 1 (from Thread 1's update)

Final Result:
  local1 = 2
  local2 = 1
  a = 2
  b = 1
```

## All Possible Execution Orders

### **Order 1: Thread 1 completes first**
```
Thread 1: local1 = a (0), b = 1
Thread 2: local2 = b (1), a = 2
Result: local1 = 0, local2 = 1
```

### **Order 2: Thread 2 completes first**
```
Thread 2: local2 = b (0), a = 2
Thread 1: local1 = a (2), b = 1
Result: local1 = 2, local2 = 0
```

### **Order 3: Interleaved (Target Scenario)**
```
Thread 2: a = 2
Thread 1: local1 = a (2), b = 1
Thread 2: local2 = b (1)
Result: local1 = 2, local2 = 1  ← TARGET RESULT
```

### **Order 4: Other interleavings**
```
Thread 1: local1 = a (0)
Thread 2: local2 = b (0), a = 2
Thread 1: b = 1
Result: local1 = 0, local2 = 0
```

## Key Concepts Demonstrated

### **1. Data Races**
- Multiple threads accessing shared variables without synchronization
- Unpredictable results due to interleaved execution
- No guarantee of execution order

### **2. Memory Visibility**
- Changes made by one thread may not be immediately visible to others
- CPU cache and memory barriers affect visibility
- No happens-before relationship between threads

### **3. Race Conditions**
- The outcome depends on the timing of thread execution
- Same code can produce different results on different runs
- Non-deterministic behavior

### **4. Interleaved Execution**
- CPU can switch between threads at any instruction boundary
- Threads don't execute atomically
- Partial execution of one thread can be interrupted by another

## Thread Safety Issues

### **Problems with Current Code**
1. **No synchronization**: Methods can execute concurrently
2. **Shared mutable state**: Variables `a` and `b` are shared
3. **No atomicity**: Operations are not atomic
4. **No visibility guarantees**: Changes may not be visible across threads

### **Potential Issues**
- **Inconsistent results**: Different outcomes on different runs
- **Hard to debug**: Race conditions are timing-dependent
- **Unpredictable behavior**: Can't rely on specific execution order

## Solutions to Make It Thread-Safe

### **Solution 1: Synchronized Methods**
```java
private static class SharedClass {
    int a = 0;
    int b = 0;

    public synchronized void method1() {
        int local1 = a;
        this.b = 1;
    }

    public synchronized void method2() {
        int local2 = b;
        this.a = 2;
    }       
}
```

### **Solution 2: Atomic Variables**
```java
private static class SharedClass {
    private AtomicInteger a = new AtomicInteger(0);
    private AtomicInteger b = new AtomicInteger(0);

    public void method1() {
        int local1 = a.get();
        b.set(1);
    }

    public void method2() {
        int local2 = b.get();
        a.set(2);
    }       
}
```

### **Solution 3: Volatile Variables**
```java
private static class SharedClass {
    volatile int a = 0;
    volatile int b = 0;

    public void method1() {
        int local1 = a;
        this.b = 1;
    }

    public void method2() {
        int local2 = b;
        this.a = 2;
    }       
}
```

## Testing the Scenario

### **Code to Demonstrate the Race Condition**
```java
public static void main(String[] args) {
    for (int i = 0; i < 1000; i++) {
        SharedClass sharedClass = new SharedClass();
        
        Thread thread1 = new Thread(() -> sharedClass.method1());
        Thread thread2 = new Thread(() -> sharedClass.method2());
        
        thread1.start();
        thread2.start();
        
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Check if we got the target result
        if (sharedClass.getLocal1() == 2 && sharedClass.getLocal2() == 1) {
            System.out.println("Target result achieved on iteration " + i);
            break;
        }
    }
}
```

## Summary

### **Key Points:**
1. **Yes, `local1 = 2` and `local2 = 1` is possible** through interleaved execution
2. **Thread 2 must execute `a = 2` before Thread 1 reads `a`**
3. **Thread 1 must execute `b = 1` before Thread 2 reads `b`**
4. **This demonstrates a classic data race condition**
5. **The result is unpredictable and depends on execution timing**

### **Why This Matters:**
- **Real-world impact**: Similar issues occur in production systems
- **Debugging difficulty**: Race conditions are hard to reproduce
- **Importance of synchronization**: Proper thread safety is crucial
- **Testing challenges**: Concurrent code requires special testing approaches

This example perfectly illustrates why understanding data races and thread safety is essential for writing reliable concurrent applications. 