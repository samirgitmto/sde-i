# Circular Dependency Resolution: Field Injection with @Lazy

## Scenario Overview
Demonstrates how to resolve circular dependency issues using `@Lazy` annotation with field injection in Spring Boot.

## Problem Statement
Two services have a circular dependency:
- `OrderService` depends on `InventoryService` to check availability and update inventory
- `InventoryService` depends on `OrderService` to log order events after inventory updates

## Code Implementation

### OrderService.java
```java
@Service
public class OrderService {
    
    @Autowired
    private InventoryService inventoryService;
    
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

### InventoryService.java (with @Lazy)
```java
@Service
public class InventoryService {

    @Autowired
    @Lazy
    private OrderService orderService;
    
    private static int LAPTOP = 10;
    
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

## How @Lazy Resolves Circular Dependency

### Without @Lazy (Problem):
```
1. Spring tries to create InventoryService
2. InventoryService needs OrderService (immediate)
3. Spring tries to create OrderService
4. OrderService needs InventoryService (immediate)
5. DEADLOCK - Circular dependency error
```

### With @Lazy (Solution):
```
1. Spring starts creating InventoryService
2. Sees @Lazy OrderService → Creates a proxy instead of real bean
3. InventoryService instantiation completes ✅
4. Spring creates OrderService (which needs InventoryService)
5. OrderService instantiation completes ✅
6. When orderService.logOrderEvents() is called, proxy fetches real OrderService ✅
```

## Startup Log Output
```
InventoryService instantiated
OrderService instantiated
RazorPaymentService created
```

## Key Benefits of @Lazy Approach

### Advantages:
- **Simple to implement**: Just add `@Lazy` annotation
- **Minimal code changes**: No need to refactor existing architecture
- **Maintains business logic**: Preserves the circular relationship if it's legitimate
- **Works with field injection**: Compatible with existing field injection patterns

### Considerations:
- **Performance overhead**: First call to lazy bean has slight delay
- **Memory usage**: Proxy objects consume additional memory
- **Not always best practice**: May mask architectural issues
- **Debugging complexity**: Stack traces may show proxy classes

## When to Use @Lazy

### Good Use Cases:
- **Legitimate circular dependencies**: When business logic genuinely requires it
- **Quick fixes**: Temporary solution while refactoring
- **Optional dependencies**: When the dependency isn't always needed
- **Performance optimization**: When you want to delay expensive bean creation

### Better Alternatives:
- **Setter injection**: More explicit and testable
- **Constructor injection with @Lazy**: Better for required dependencies
- **Refactoring**: Split responsibilities to remove circular dependency
- **Event-driven architecture**: Use events instead of direct method calls

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

## Summary

The `@Lazy` annotation provides an elegant solution to circular dependency issues by:
1. **Delaying bean initialization** until actually needed
2. **Creating proxy objects** that resolve to real beans on first access
3. **Breaking the instantiation cycle** that causes circular dependency errors

This approach is particularly useful when you have legitimate business reasons for circular dependencies and need a quick, non-invasive solution. 