# Conditional Bean Loading in Spring Boot

## 1. `@ConditionalOnProperty` for Conditional Bean Loading

### **What is it?**
- `@ConditionalOnProperty` is a Spring Boot annotation used to conditionally enable or disable bean registration based on the presence and value of a property in your configuration files (like `application.properties` or `application.yml`).

### **Why use it?**
- To load only the relevant implementation of a service based on configuration, avoiding conflicts and making your application flexible and environment-driven.

### **How to use it?**

```java
@Service
@ConditionalOnProperty(name = "payment.provider", havingValue = "stripe")
public class StripePaymentService implements PaymentService {
    // ...
}

@Service
@ConditionalOnProperty(name = "payment.provider", havingValue = "razorpay")
public class RazorPaymentService implements PaymentService {
    // ...
}
```

**In your `application.properties` or `application.yml`:**
```properties
payment.provider=stripe  # or razorpay
```

**Result:** Only the bean matching the property value is loaded and injected.

---

## 2. Clean Configuration Binding with `@ConfigurationProperties`

### **What is it?**
- `@ConfigurationProperties` is used to bind external configuration (from properties or YAML files) to a strongly-typed Java bean.

### **Why use it?**
- Keeps configuration clean, type-safe, and easy to manage.
- Avoids hardcoding values in your code.

### **How to use it?**

```java
@ConfigurationProperties(prefix = "payment")
public class PaymentConfig {
    private String provider;
    // getters and setters
}
```

**Enable it in your main application class:**
```java
@SpringBootApplication
@EnableConfigurationProperties(PaymentConfig.class)
public class BasicsDiApplication { ... }
```

**In your `application.properties`:**
```properties
payment.provider=razorpay
```

---

## 3. Key Takeaways
- Use `@ConditionalOnProperty` to control which beans are loaded based on configuration.
- Use `@ConfigurationProperties` for clean, type-safe configuration binding.
- This approach avoids `@Autowired` conflicts and makes your application flexible and easy to configure for different environments.
- Always ensure your beans implement the correct interfaces for dependency injection to work.

---

## 4. Example Flow
1. Set `payment.provider` in your properties file.
2. Only the matching payment service bean is loaded.
3. The controller injects the correct implementation without code changes.

---

**This pattern is widely used for feature toggles, environment-specific beans, and modular service design in Spring Boot.** 