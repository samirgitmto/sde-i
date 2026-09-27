# Circular Dependency Resolution: Constructor Injection with @Lazy

## Scenario Overview
Demonstrates how to resolve circular dependency issues using `@Lazy` annotation with constructor injection in Spring Boot, with `@Lazy` applied to the OrderService constructor.

## Problem Statement
Two services have a circular dependency:
- `OrderService` depends on `InventoryService` to check availability and update inventory
- `InventoryService` depends on `OrderService` to log order events after inventory updates

## Code Implementation

### OrderService.java (Constructor Injection with @Lazy)
```java
@Service
public class OrderService {
    
    private InventoryService inventoryService;
    
    public OrderService() {
        System.err.println("OrderService instantiated");
    }
    
    @Autowired
    public OrderService(@Lazy InventoryService inventoryService) {
        this.inventoryService = inventoryService;
        System.err.println("OrderService instantiated through parameterized constructor");
    }
    
    public String processOrder(String item, int qty) {
        int qtyAvailable = inventoryService.checkAvailability(item);
        if (qtyAvailable >= qty) {
            inventoryService.updateInventory(item, qty);
            return "order processed";
        } else {
            return "insufficient stock";
        }
    }
    
    public void logOrderEvents(String item, int qty) {
        System.err.println("order logged for item " + item + " with quantity " + qty);
    }
}
```

### InventoryService.java (Constructor Injection)
```java
@Service
public class InventoryService {

    private OrderService orderService;
    
    private static int LAPTOP = 10;
    
    public InventoryService() {
        System.err.println("InventoryService instantiated");
    }
    
    @Autowired  // ⚠️ CRITICAL: This annotation is required!
    public InventoryService(OrderService orderService) {
        this.orderService = orderService;
        System.err.println("InventoryService instantiated through parameterized constructor");
    }
    
    public int checkAvailability(String item) {
        if (item.equalsIgnoreCase("laptop"))
            return LAPTOP;
        else
            return 0;
    }
    
    public void updateInventory(String item, int qtySold) {
        LAPTOP -= qtySold;
        System.err.println("laptop quantity updated to " + LAPTOP);
        orderService.logOrderEvents(item, qtySold);
    }
}
```

## How Constructor Injection with @Lazy Resolves Circular Dependency

### Resolution Flow:
```
1. Spring starts creating OrderService
2. OrderService constructor needs @Lazy InventoryService → Creates proxy ✅
3. OrderService instantiation completes ✅
4. Spring creates InventoryService (which needs OrderService)
5. InventoryService instantiation completes ✅
6. When inventoryService.checkAvailability() is called, proxy fetches real InventoryService ✅
```

### Startup Log Output:
```
InventoryService instantiated through parameterized constructor
OrderService instantiated through parameterized constructor
RazorPaymentService created
```

## ⚠️ Critical Learning: @Autowired on Constructors

### The Problem:
When you have **multiple constructors** in a Spring bean, Spring needs to know which one to use for dependency injection.

### What Happens Without @Autowired:
```java
// ❌ WRONG - Spring will choose the default constructor
public InventoryService(OrderService orderService) {
    this.orderService = orderService;
}
```

**Result**: 
- Spring chooses the default constructor (no parameters)
- `orderService` field remains `null`
- **NullPointerException** when calling `orderService.logOrderEvents()`

### The Fix:
```java
// ✅ CORRECT - Spring will use this constructor for dependency injection
@Autowired
public InventoryService(OrderService orderService) {
    this.orderService = orderService;
}
```

**Result**:
- Spring uses the parameterized constructor
- `orderService` field gets properly injected
- No more NullPointerException

## Key Differences from Other Approaches

### This Approach vs Field Injection:
- **@Lazy location**: Applied to OrderService constructor parameter
- **Creation order**: OrderService → InventoryService
- **Control**: OrderService controls the lazy initialization

### This Approach vs Previous Constructor Injection:
- **@Lazy location**: OrderService constructor instead of InventoryService
- **Initialization pattern**: Different lazy loading strategy
- **Performance**: InventoryService created normally (no proxy overhead)

## Advantages of This Approach

### Benefits:
1. **OrderService controls lazy loading**: It decides when to initialize InventoryService
2. **InventoryService created normally**: No proxy overhead for its dependencies
3. **Clear separation**: One service is lazy, the other is eager
4. **Constructor injection benefits**: Immutability, testability, compile-time safety

### When to Use:
- **OrderService is the primary service**: When OrderService should control initialization
- **Performance critical**: When you want InventoryService without proxy overhead
- **Clear dependency hierarchy**: When you want to establish a clear "controller" service

## Testing the Resolution

### Controller Endpoint:
```java
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;
    
    @PostMapping("/order/{item}")
    public ResponseEntity<String> placeOrder(@PathVariable String item, @RequestParam int qty) {
        String res = orderService.processOrder(item, qty);
        return ResponseEntity.ok(res);
    }
}
```

### Test Request:
```
POST /order/laptop?qty=2
```

### Expected Output:
```
laptop quantity updated to 8
order logged for item laptop with quantity 2
```

## Common Pitfalls and Solutions

### Pitfall 1: Missing @Autowired on Constructor
**Problem**: NullPointerException due to uninitialized dependencies
**Solution**: Always add `@Autowired` to the constructor you want Spring to use

### Pitfall 2: Multiple Constructors Without @Autowired
**Problem**: Spring might choose the wrong constructor
**Solution**: Either add `@Autowired` or remove unused constructors

### Pitfall 3: @Lazy on Wrong Constructor
**Problem**: Circular dependency not resolved
**Solution**: Apply `@Lazy` to the constructor parameter that creates the circular dependency

## Best Practices

### When Using Constructor Injection with @Lazy:
1. **Always add @Autowired**: To the constructor you want Spring to use
2. **Document the circular dependency**: Explain why it exists
3. **Choose @Lazy location carefully**: Decide which service should control lazy loading
4. **Test thoroughly**: Ensure all dependencies are properly initialized
5. **Consider alternatives**: Evaluate if circular dependency can be eliminated

### Alternative Solutions:
1. **Event-driven architecture**: Use Spring events instead of direct method calls
2. **Mediator pattern**: Create a third service to coordinate between the two
3. **Interface segregation**: Split interfaces to reduce coupling
4. **Dependency inversion**: Use interfaces to weaken direct dependencies

## Summary

Constructor injection with `@Lazy` on the OrderService constructor provides an effective solution for circular dependencies by:

1. **OrderService controls lazy initialization** of InventoryService
2. **InventoryService gets created normally** without proxy overhead
3. **Clear dependency contracts** through constructor signatures
4. **Proper dependency injection** with `@Autowired` annotation

**Key Learning**: Always add `@Autowired` to the constructor you want Spring to use for dependency injection, especially when you have multiple constructors!

This approach demonstrates deep understanding of Spring's dependency injection mechanisms and the importance of proper constructor annotation. 