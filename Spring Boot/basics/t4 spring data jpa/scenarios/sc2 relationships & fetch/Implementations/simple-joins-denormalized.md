# Simple JOINs with Denormalized DTOs: Alternative to JOIN FETCH

## Overview

This approach uses simple JOINs with constructor expressions to create denormalized DTOs directly from the database query. This is an elegant alternative to JOIN FETCH that avoids the MultipleBagFetchException and entity serialization issues.

## Implementation

### **DTO Design**
```java
public record CustomerOrderItemDTO(
    Long customerId,
    String customerName,
    Long orderId,
    LocalDate orderDate,
    Long itemId,
    String productName,
    Integer quantity
) {}
```

### **Repository Query**
```java
@Query("""
    SELECT new com.example.CustomerOrderItemDTO(
        c.id, c.name,
        o.id, o.orderDate,
        i.id, i.productName, i.quantity
    )
    FROM Customer c
    JOIN c.orders o
    JOIN o.items i
    WHERE c.id = :customerId
    """)
List<CustomerOrderItemDTO> getCustomerDashboardData(@Param("customerId") Long customerId);
```

### **Service Layer**
```java
@Service
public class CustomerService {
    
    public List<CustomerOrderItemDTO> getCustomerDashboardData(Long customerId) {
        return customerRepo.getCustomerDashboardData(customerId);
    }
}
```

### **Controller Endpoint**
```java
@GetMapping("/customers/{customerId}/dashboard-data")
public ResponseEntity<List<CustomerOrderItemDTO>> getCustomerDashboardData(
        @PathVariable Long customerId) {
    List<CustomerOrderItemDTO> data = customerService.getCustomerDashboardData(customerId);
    return ResponseEntity.ok(data);
}
```

## How It Works

### **Query Execution**
The JPQL query translates to SQL like:
```sql
SELECT 
    c.id, c.name,
    o.id, o.order_date,
    i.id, i.product_name, i.quantity
FROM customer c
JOIN orders_table o ON c.id = o.customer_id
JOIN order_item i ON o.id = i.order_id
WHERE c.id = ?
```

### **Result Processing**
- Each row represents one item with its associated customer and order data
- Hibernate automatically calls the DTO constructor with the selected fields
- No entity loading or relationship management required

### **Sample Output**
```json
[
  {
    "customerId": 1,
    "customerName": "John Doe",
    "orderId": 1,
    "orderDate": "2024-01-15",
    "itemId": 1,
    "productName": "Laptop",
    "quantity": 1
  },
  {
    "customerId": 1,
    "customerName": "John Doe", 
    "orderId": 1,
    "orderDate": "2024-01-15",
    "itemId": 2,
    "productName": "Mouse",
    "quantity": 2
  },
  {
    "customerId": 1,
    "customerName": "John Doe",
    "orderId": 2,
    "orderDate": "2024-01-20",
    "itemId": 3,
    "productName": "Keyboard",
    "quantity": 1
  }
]
```

## Comparison with JOIN FETCH

### **JOIN FETCH Approach**
```java
@Query("Select c From Customer c " +
       "Left JOIN FETCH c.orders o " +
       "Left JOIN FETCH o.items i " +
       "Where c.id = :cId")
Customer findCustomerWithOrdersAndItems(@Param("cId") Long customerId);
```

**Characteristics:**
- Returns **entities** with relationships loaded
- Maintains object hierarchy (Customer → Orders → Items)
- Can cause MultipleBagFetchException with multiple List collections
- Requires manual DTO conversion
- More memory usage (full entity objects)
- Potential JSON serialization issues

### **Denormalized DTO Approach**
```java
@Query("""
    SELECT new com.example.CustomerOrderItemDTO(
        c.id, c.name, o.id, o.orderDate, i.id, i.productName, i.quantity
    )
    FROM Customer c JOIN c.orders o JOIN o.items i
    WHERE c.id = :customerId
    """)
List<CustomerOrderItemDTO> getCustomerDashboardData(@Param("customerId") Long customerId);
```

**Characteristics:**
- Returns **DTOs directly** from the query
- Flattens data into denormalized structure
- No MultipleBagFetchException (no entity relationships to manage)
- No manual conversion needed
- More efficient memory usage (only needed fields)
- Clean API response without serialization issues

## Advantages

### **1. Solves MultipleBagFetchException**
- No entity relationships to manage
- Simple JOINs don't create the same Hibernate limitations
- Works with any number of List collections

### **2. Better Performance**
- Single query instead of multiple queries
- No entity loading overhead
- Direct DTO creation from database results

### **3. Clean API Design**
- No @JsonIgnore needed
- No entity serialization issues
- Predictable, flat data structure

### **4. Simplified Code**
- No complex entity-to-DTO conversion
- No relationship management
- No transaction boundaries to worry about

### **5. Memory Efficiency**
- Only loads required fields
- No full entity objects in memory
- Smaller memory footprint

## Trade-offs

### **1. Denormalized Data**
- Customer and order information is repeated for each item
- Larger response size due to data duplication
- Less normalized than entity approach

### **2. Limited Flexibility**
- Fixed structure (can't easily change response format)
- Less suitable for complex hierarchical responses
- Requires client-side grouping for nested structures

### **3. Query Complexity**
- More complex JPQL for multiple joins
- Harder to add optional relationships
- May need multiple queries for different use cases

## Use Cases

### **Best For:**
- Simple dashboard data
- Reporting scenarios
- When you need flat data structure
- Avoiding entity serialization issues
- Performance-critical endpoints

### **Not Suitable For:**
- Complex hierarchical responses
- When you need to modify entities
- Scenarios requiring entity relationships
- When data normalization is critical

## Alternative: Client-Side Grouping

If you need hierarchical structure, you can group the denormalized data on the client side:

```java
@GetMapping("/customers/{customerId}/dashboard-grouped")
public ResponseEntity<CustomerOrdersItemsDetailsDto> getCustomerDashboardGrouped(
        @PathVariable Long customerId) {
    List<CustomerOrderItemDTO> flatData = customerService.getCustomerDashboardData(customerId);
    
    // Group by customer and orders
    CustomerOrdersItemsDetailsDto groupedData = groupCustomerData(flatData);
    return ResponseEntity.ok(groupedData);
}

private CustomerOrdersItemsDetailsDto groupCustomerData(List<CustomerOrderItemDTO> flatData) {
    if (flatData.isEmpty()) return null;
    
    CustomerOrderItemDTO first = flatData.get(0);
    CustomerOrdersItemsDetailsDto customer = new CustomerOrdersItemsDetailsDto(
        first.customerId(), first.customerName(), new ArrayList<>()
    );
    
    Map<Long, OrderItemsDto> orderMap = new HashMap<>();
    
    for (CustomerOrderItemDTO item : flatData) {
        orderMap.computeIfAbsent(item.orderId(), orderId -> 
            new OrderItemsDto(orderId, item.orderDate(), new ArrayList<>())
        ).items().add(new OrderItemDto(item.itemId(), item.productName(), item.quantity()));
    }
    
    customer.orders().addAll(orderMap.values());
    return customer;
}
```

## Best Practices

### **1. Use Records for DTOs**
- Immutable and concise
- Automatic equals/hashCode/toString
- Perfect for data transfer

### **2. Select Only Needed Fields**
- Don't select unnecessary columns
- Optimize for performance and memory usage

### **3. Consider Pagination**
- For large datasets, add pagination support
- Use `Pageable` parameter in repository methods

### **4. Add Error Handling**
```java
@GetMapping("/customers/{customerId}/dashboard-data")
public ResponseEntity<List<CustomerOrderItemDTO>> getCustomerDashboardData(
        @PathVariable Long customerId) {
    try {
        List<CustomerOrderItemDTO> data = customerService.getCustomerDashboardData(customerId);
        return ResponseEntity.ok(data);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                           .body(Collections.emptyList());
    }
}
```

## Conclusion

The denormalized DTO approach with constructor expressions is an excellent alternative to JOIN FETCH for specific use cases. It provides:

- **Performance benefits** through single queries
- **Simplicity** by avoiding entity relationship management
- **Clean API design** without serialization issues
- **Flexibility** for different response formats

This approach is particularly well-suited for dashboard data, reporting scenarios, and any situation where you need flat, denormalized data structures. It's a powerful tool in the Spring Data JPA toolkit that should be considered alongside JOIN FETCH and other fetch strategies. 