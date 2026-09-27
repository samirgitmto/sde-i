# DTO Design for Complex Entity Relationships

## Problem Statement
When dealing with complex entity relationships like Customer → Order → OrderItem, creating DTOs becomes challenging due to:
- Multiple levels of nesting
- Circular references
- Performance considerations
- Different use cases requiring different data views

## Entity Relationship Structure
```
Customer (1) ←→ (N) Order (1) ←→ (N) OrderItem
```

## Approach 1: Nested DTO Classes (Recommended for Complex Scenarios)

### Structure
```java
public class CustomerOrdersItemsDetailsDto {
    // Customer level data
    private Long customerId;
    private String customerName;
    private String customerEmail;
    
    // Orders level data
    private List<OrderDetailsDto> orders;
    
    // Nested DTOs
    public static class OrderDetailsDto {
        private Long orderId;
        private LocalDate orderDate;
        private String orderStatus;
        private List<OrderItemDetailsDto> items;
        
        // Constructor, getters, setters
    }
    
    public static class OrderItemDetailsDto {
        private Long itemId;
        private String productName;
        private int quantity;
        private BigDecimal price;
        
        // Constructor, getters, setters
    }
}
```

### Advantages
- **Type Safety**: Compile-time checking
- **Encapsulation**: Related data grouped together
- **Flexibility**: Easy to modify structure
- **Clear Hierarchy**: Reflects entity relationships

### Disadvantages
- **Verbose**: More code to write
- **Memory**: Each nested object has overhead

## Approach 2: Flat DTO with Prefixes

### Structure
```java
public class CustomerOrdersItemsFlatDto {
    // Customer data
    private Long customerId;
    private String customerName;
    
    // Order data (for first order)
    private Long orderId;
    private LocalDate orderDate;
    
    // Item data (for first item)
    private Long itemId;
    private String productName;
    private int quantity;
    
    // Additional fields for multiple orders/items
    private List<Long> orderIds;
    private List<LocalDate> orderDates;
    private List<String> productNames;
}
```

### Advantages
- **Simple**: Easy to understand
- **Lightweight**: Less memory usage
- **Fast**: No nested object creation

### Disadvantages
- **Confusing**: Hard to maintain relationships
- **Limited**: Doesn't scale well for complex scenarios
- **Error-prone**: Easy to mix up data

## Approach 3: Map-Based DTO

### Structure
```java
public class CustomerOrdersItemsMapDto {
    private Map<String, Object> customerData;
    private List<Map<String, Object>> ordersData;
    private List<Map<String, Object>> itemsData;
    
    // Helper methods to access data
    public String getCustomerName() {
        return (String) customerData.get("name");
    }
}
```

### Advantages
- **Flexible**: Can handle dynamic data
- **Generic**: Reusable for different scenarios

### Disadvantages
- **Type Unsafe**: Runtime errors possible
- **Performance**: Boxing/unboxing overhead
- **Maintenance**: Hard to refactor

## Recommended Implementation Strategy

### Step 1: Define Use Cases
```java
// Use Case 1: Customer dashboard - need all orders with items
CustomerOrdersItemsDetailsDto getCustomerDashboard(Long customerId)

// Use Case 2: Order summary - need order with customer and items
OrderWithCustomerAndItemsDto getOrderSummary(Long orderId)

// Use Case 3: Customer summary - need aggregated data
CustomerSummaryDto getCustomerSummary(Long customerId)
```

### Step 2: Design DTOs Based on Use Cases

#### For Customer Dashboard (Complete Data)
```java
public class CustomerOrdersItemsDetailsDto {
    private Long customerId;
    private String customerName;
    private List<OrderDetailsDto> orders;
    
    public static class OrderDetailsDto {
        private Long orderId;
        private LocalDate orderDate;
        private List<OrderItemDetailsDto> items;
    }
    
    public static class OrderItemDetailsDto {
        private Long itemId;
        private String productName;
        private int quantity;
    }
}
```

#### For Order Summary (Focused Data)
```java
public class OrderWithCustomerAndItemsDto {
    private Long orderId;
    private LocalDate orderDate;
    private CustomerSummaryDto customer;
    private List<OrderItemSummaryDto> items;
    
    public static class CustomerSummaryDto {
        private Long customerId;
        private String customerName;
    }
    
    public static class OrderItemSummaryDto {
        private String productName;
        private int quantity;
    }
}
```

### Step 3: Implement Conversion Methods

#### Option A: Manual Conversion (Recommended for Complex Scenarios)
```java
@Service
public class CustomerService {
    
    public CustomerOrdersItemsDetailsDto getCustomerOrders(Long customerId) {
        Customer customer = customerRepository.findCustomerWithOrdersAndItems(customerId);
        return convertToCustomerOrdersItemsDetailsDto(customer);
    }
    
    private CustomerOrdersItemsDetailsDto convertToCustomerOrdersItemsDetailsDto(Customer customer) {
        CustomerOrdersItemsDetailsDto dto = new CustomerOrdersItemsDetailsDto();
        dto.setCustomerId(customer.getId());
        dto.setCustomerName(customer.getName());
        
        List<CustomerOrdersItemsDetailsDto.OrderDetailsDto> orderDtos = 
            customer.getOrders().stream()
                .map(this::convertToOrderDetailsDto)
                .collect(Collectors.toList());
        
        dto.setOrders(orderDtos);
        return dto;
    }
    
    private CustomerOrdersItemsDetailsDto.OrderDetailsDto convertToOrderDetailsDto(Order order) {
        CustomerOrdersItemsDetailsDto.OrderDetailsDto dto = 
            new CustomerOrdersItemsDetailsDto.OrderDetailsDto();
        dto.setOrderId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        
        List<CustomerOrdersItemsDetailsDto.OrderItemDetailsDto> itemDtos = 
            order.getItems().stream()
                .map(this::convertToOrderItemDetailsDto)
                .collect(Collectors.toList());
        
        dto.setItems(itemDtos);
        return dto;
    }
    
    private CustomerOrdersItemsDetailsDto.OrderItemDetailsDto convertToOrderItemDetailsDto(OrderItem item) {
        CustomerOrdersItemsDetailsDto.OrderItemDetailsDto dto = 
            new CustomerOrdersItemsDetailsDto.OrderItemDetailsDto();
        dto.setItemId(item.getId());
        dto.setProductName(item.getProductName());
        dto.setQuantity(item.getQuantity());
        return dto;
    }
}
```

#### Option B: Using ModelMapper (For Simple Scenarios)
```java
@Autowired
private ModelMapper modelMapper;

public CustomerOrdersItemsDetailsDto getCustomerOrders(Long customerId) {
    Customer customer = customerRepository.findCustomerWithOrdersAndItems(customerId);
    return modelMapper.map(customer, CustomerOrdersItemsDetailsDto.class);
}
```

### Step 4: Handle Performance Considerations

#### Use JOIN FETCH to Avoid N+1
```java
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    @Query("SELECT DISTINCT c FROM Customer c " +
           "LEFT JOIN FETCH c.orders o " +
           "LEFT JOIN FETCH o.items " +
           "WHERE c.id = :customerId")
    Optional<Customer> findCustomerWithOrdersAndItems(@Param("customerId") Long customerId);
}
```

#### Consider Pagination for Large Datasets
```java
public Page<CustomerOrdersItemsDetailsDto> getCustomerOrdersPaginated(
        Long customerId, Pageable pageable) {
    Page<Order> orders = orderRepository.findByCustomerId(customerId, pageable);
    return orders.map(this::convertToOrderDetailsDto);
}
```

## Best Practices

### 1. **Keep DTOs Focused**
- Each DTO should serve a specific use case
- Don't include unnecessary fields
- Consider creating multiple DTOs for different scenarios

### 2. **Use Immutable DTOs When Possible**
```java
public class CustomerOrdersItemsDetailsDto {
    private final Long customerId;
    private final String customerName;
    private final List<OrderDetailsDto> orders;
    
    // Constructor only, no setters
}
```

### 3. **Handle Null Values Gracefully**
```java
private CustomerOrdersItemsDetailsDto.OrderDetailsDto convertToOrderDetailsDto(Order order) {
    if (order == null) return null;
    
    CustomerOrdersItemsDetailsDto.OrderDetailsDto dto = 
        new CustomerOrdersItemsDetailsDto.OrderDetailsDto();
    dto.setOrderId(order.getId());
    dto.setOrderDate(order.getOrderDate());
    
    // Handle null items list
    List<CustomerOrdersItemsDetailsDto.OrderItemDetailsDto> itemDtos = 
        Optional.ofNullable(order.getItems())
            .orElse(Collections.emptyList())
            .stream()
            .map(this::convertToOrderItemDetailsDto)
            .collect(Collectors.toList());
    
    dto.setItems(itemDtos);
    return dto;
}
```

### 4. **Use Builder Pattern for Complex DTOs**
```java
public class CustomerOrdersItemsDetailsDto {
    // ... fields ...
    
    public static class Builder {
        private Long customerId;
        private String customerName;
        private List<OrderDetailsDto> orders = new ArrayList<>();
        
        public Builder customerId(Long customerId) {
            this.customerId = customerId;
            return this;
        }
        
        public Builder customerName(String customerName) {
            this.customerName = customerName;
            return this;
        }
        
        public Builder addOrder(OrderDetailsDto order) {
            this.orders.add(order);
            return this;
        }
        
        public CustomerOrdersItemsDetailsDto build() {
            return new CustomerOrdersItemsDetailsDto(customerId, customerName, orders);
        }
    }
}
```

### 5. **Consider Using Projections for Simple Cases**
```java
public interface CustomerOrderProjection {
    Long getCustomerId();
    String getCustomerName();
    Long getOrderId();
    LocalDate getOrderDate();
    String getProductName();
    int getQuantity();
}

@Query("SELECT c.id as customerId, c.name as customerName, " +
       "o.id as orderId, o.orderDate, oi.productName, oi.quantity " +
       "FROM Customer c JOIN c.orders o JOIN o.items oi " +
       "WHERE c.id = :customerId")
List<CustomerOrderProjection> findCustomerOrdersProjection(@Param("customerId") Long customerId);
```

## Summary

For complex entity relationships like Customer → Order → OrderItem:

1. **Use nested DTO classes** for type safety and clear structure
2. **Design DTOs based on specific use cases** rather than trying to create one universal DTO
3. **Implement manual conversion methods** for complex scenarios
4. **Use JOIN FETCH** to avoid N+1 problems
5. **Consider performance implications** and use pagination when needed
6. **Handle null values gracefully** to avoid runtime errors
7. **Keep DTOs focused and minimal** to reduce memory usage and improve performance

This approach ensures maintainable, performant, and type-safe DTOs for complex entity relationships. 