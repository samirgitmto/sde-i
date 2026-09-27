# JSON Serialization Issues in Spring Boot Applications

## Problem: HTTP 406 Not Acceptable Error

### Error Message
```
This application has no explicit mapping for /error, so you are seeing this as a fallback.
There was an unexpected error (type=Not Acceptable, status=406).
Acceptable representations: [application/json, application/*+json].
```

### Root Cause
The 406 Not Acceptable error occurs when Spring Boot cannot serialize your response object to JSON. This typically happens due to:

1. **Missing Getters/Setters** in DTOs
2. **Missing Default Constructors** in DTOs
3. **Improper Error Handling** in controllers
4. **LazyInitializationException** when accessing lazy-loaded relationships

## Common Causes and Solutions

### 1. Missing Getters and Setters

#### Problem
```java
public class CustomerOrdersDto {
    private Long customerId;
    private String customerName;
    private List<OrderDto> orders;
    
    // Constructor only - NO GETTERS/SETTERS!
    public CustomerOrdersDto(Long customerId, String customerName, List<OrderDto> orders) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.orders = orders;
    }
}
```

#### Solution
```java
public class CustomerOrdersDto {
    private Long customerId;
    private String customerName;
    private List<OrderDto> orders;
    
    // Default constructor for JSON deserialization
    public CustomerOrdersDto() {
    }
    
    // Parameterized constructor
    public CustomerOrdersDto(Long customerId, String customerName, List<OrderDto> orders) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.orders = orders;
    }
    
    // GETTERS AND SETTERS - REQUIRED FOR JSON SERIALIZATION
    public Long getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public List<OrderDto> getOrders() {
        return orders;
    }
    
    public void setOrders(List<OrderDto> orders) {
        this.orders = orders;
    }
}
```

### 2. Missing Default Constructor

#### Problem
```java
public class OrderItemsDto {
    private Long id;
    private String productName;
    private int quantity;
    
    // Only parameterized constructor - NO DEFAULT CONSTRUCTOR!
    public OrderItemsDto(Long id, String productName, int quantity) {
        this.id = id;
        this.productName = productName;
        this.quantity = quantity;
    }
}
```

#### Solution
```java
public class OrderItemsDto {
    private Long id;
    private String productName;
    private int quantity;
    
    // DEFAULT CONSTRUCTOR - REQUIRED FOR JSON DESERIALIZATION
    public OrderItemsDto() {
    }
    
    // Parameterized constructor
    public OrderItemsDto(Long id, String productName, int quantity) {
        this.id = id;
        this.productName = productName;
        this.quantity = quantity;
    }
    
    // Getters and setters...
}
```

### 3. Improper Error Handling in Controllers

#### Problem
```java
@GetMapping("/customers/{customerId}")
public ResponseEntity<CustomerOrdersDto> getCustomerOrders(@PathVariable Long customerId) {
    CustomerOrdersDto result = customerService.getCustomerOrders(customerId);
    // This will throw NullPointerException if customer not found
    return ResponseEntity.ok(result);
}
```

#### Solution
```java
@GetMapping("/customers/{customerId}")
public ResponseEntity<CustomerOrdersDto> getCustomerOrders(@PathVariable Long customerId) {
    Optional<CustomerOrdersDto> result = customerService.getCustomerOrders(customerId);
    return result.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
}
```

### 4. LazyInitializationException

#### Problem
```java
// Order.items is LAZY loaded
@OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
private List<OrderItem> items;

// Accessing items outside transaction
public CustomerOrdersDto getCustomerOrders(Long customerId) {
    Customer customer = customerRepository.findById(customerId).orElse(null);
    // This will throw LazyInitializationException when accessing order.getItems()
    return convertToDto(customer);
}
```

#### Solution
```java
// Use JOIN FETCH to load relationships eagerly
@Query("SELECT DISTINCT c FROM Customer c " +
       "LEFT JOIN FETCH c.orders o " +
       "LEFT JOIN FETCH o.items " +
       "WHERE c.id = :customerId")
Optional<Customer> findCustomerWithOrdersAndItems(@Param("customerId") Long customerId);

// Or use @Transactional
@Transactional
public CustomerOrdersDto getCustomerOrders(Long customerId) {
    Customer customer = customerRepository.findById(customerId).orElse(null);
    return convertToDto(customer);
}
```

## Best Practices for DTO Design

### 1. Always Include Default Constructor
```java
public class MyDto {
    // ALWAYS include default constructor
    public MyDto() {
    }
    
    // Parameterized constructor
    public MyDto(String name) {
        this.name = name;
    }
}
```

### 2. Always Include Getters and Setters
```java
public class MyDto {
    private String name;
    
    // GETTERS AND SETTERS - REQUIRED
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
```

### 3. Use Lombok to Reduce Boilerplate
```java
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyDto {
    private String name;
    private int age;
}
```

### 4. Handle Null Values Gracefully
```java
public class MyDto {
    private List<String> items = new ArrayList<>(); // Initialize to empty list
    
    public void setItems(List<String> items) {
        this.items = items != null ? items : new ArrayList<>();
    }
}
```

## Debugging Serialization Issues

### 1. Enable Debug Logging
```properties
# application.properties
logging.level.org.springframework.web=DEBUG
logging.level.com.fasterxml.jackson=DEBUG
```

### 2. Check Jackson Configuration
```java
@Configuration
public class JacksonConfig {
    
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return mapper;
    }
}
```

### 3. Use @JsonIgnore for Problematic Fields
```java
public class MyDto {
    private String name;
    
    @JsonIgnore // Exclude from JSON serialization
    private String sensitiveData;
}
```

## Common Patterns for DTOs

### 1. Nested DTOs
```java
public class CustomerOrdersDto {
    private Long customerId;
    private String customerName;
    private List<OrderDto> orders;
    
    public static class OrderDto {
        private Long orderId;
        private LocalDate orderDate;
        private List<OrderItemDto> items;
        
        // Default constructor and getters/setters
    }
    
    public static class OrderItemDto {
        private Long itemId;
        private String productName;
        private int quantity;
        
        // Default constructor and getters/setters
    }
}
```

### 2. Builder Pattern
```java
public class MyDto {
    private String name;
    private int age;
    
    public static class Builder {
        private String name;
        private int age;
        
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        public Builder age(int age) {
            this.age = age;
            return this;
        }
        
        public MyDto build() {
            return new MyDto(name, age);
        }
    }
}
```

## Summary

To avoid 406 Not Acceptable errors:

1. **Always include default constructors** in DTOs
2. **Always include getters and setters** in DTOs
3. **Handle null values gracefully** in controllers
4. **Use JOIN FETCH** for lazy-loaded relationships
5. **Use Optional** for proper error handling
6. **Consider using Lombok** to reduce boilerplate
7. **Enable debug logging** to troubleshoot issues

Remember: JSON serialization in Spring Boot requires proper JavaBean conventions (default constructor + getters/setters) to work correctly.
