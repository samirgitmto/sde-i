Here’s the approach to achieve **conditional bean loading using `@Profile`** in Spring Boot, instead of `@ConditionalOnProperty`:

---

## **Approach: Profile-Based Bean Loading with `@Profile`**

### **1. What is `@Profile`?**
- `@Profile` is a Spring annotation that marks a bean to be registered only when a specific profile is active.
- Profiles are typically used to define different beans/configurations for different environments (e.g., `dev`, `prod`, `test`), but you can use them for any logical grouping, such as payment providers.

---

### **2. Steps to Implement Profile-Based Loading**

#### **Step 1: Define Profiles for Each Implementation**
- Annotate each payment service implementation with a unique profile.
    - Example: `@Profile("stripe")` for Stripe, `@Profile("razorpay")` for Razorpay.

#### **Step 2: Activate the Desired Profile**
- Set the active profile in your `application.properties`, `application.yml`, or via command line:
    - `spring.profiles.active=stripe` or `spring.profiles.active=razorpay`

#### **Step 3: Remove `@ConditionalOnProperty`**
- You do not need `@ConditionalOnProperty` anymore; just use `@Profile`.

#### **Step 4: Keep the Interface and Controller Unchanged**
- The controller will inject `PaymentService` as before. Only the bean for the active profile will be created and injected.

#### **Step 5: (Optional) Use Profile-Specific Properties Files**
- You can have `application-stripe.properties` and `application-razorpay.properties` for provider-specific settings.

---

### **3. Example Structure**

**Stripe Implementation:**
```java
@Service
@Profile("stripe")
public class StripePaymentService implements PaymentService {
    // ...
}
```

**Razorpay Implementation:**
```java
@Service
@Profile("razorpay")
public class RazorPaymentService implements PaymentService {
    // ...
}
```

**Activate Profile:**
```properties
# In application.properties
spring.profiles.active=stripe
# or
spring.profiles.active=razorpay
```

---

### **4. Key Points & Best Practices**
- Only one profile should be active at a time for this use case.
- If no profile is active, no `PaymentService` bean will be created, and the application will fail to start (which is desired to avoid ambiguity).
- You can use profile-specific configuration files for further customization.
- This approach is clean, easy to understand, and leverages Spring’s built-in environment abstraction.

---

### **5. When to Use `@Profile` vs `@ConditionalOnProperty`**
- Use `@Profile` when you want to switch entire sets of beans/configurations based on the environment or logical grouping.
- Use `@ConditionalOnProperty` when you want more fine-grained, property-driven control.

---

**What if both profiles are active?**

**No, it's not mandatory to use `@Qualifier` when you have `@Primary` annotation.**
Both StripePaymentService and RazorPaymentService beans will be created, causing a conflict when Spring tries to inject PaymentService into your controller.

## **How `@Primary` Works**

### **`@Primary` Behavior:**
- When multiple beans of the same type exist, Spring will **automatically inject the bean marked with `@Primary`**
- You can still use `@Autowired` without `@Qualifier` - Spring will choose the primary bean

### **Example:**

```java
@Service
@Profile("stripe")
@Primary  // This bean gets priority
public class StripePaymentService implements PaymentService { ... }

@Service
@Profile("razorpay")
public class RazorPaymentService implements PaymentService { ... }

@RestController
public class PaymentController {
    
    @Autowired  // ✅ Works fine - will inject StripePaymentService
    private PaymentService paymentService;
    
    // No @Qualifier needed!
}
```

---

## **When to Use Each Approach**

### **Use `@Primary` when:**
- You want a **default bean** that should be used most of the time
- You want to avoid using `@Qualifier` everywhere
- You have a clear "preferred" implementation

### **Use `@Qualifier` when:**
- You want **explicit control** over which bean is injected
- You don't want to rely on a default
- You want to make the dependency injection **more explicit and clear**

### **Use both when:**
- You have a primary bean but want to **override it in specific cases**

---

## **Comparison Examples**

### **Scenario 1: Only `@Primary`**
```java
@Service
@Profile("stripe")
@Primary
public class StripePaymentService implements PaymentService { ... }

@Service
@Profile("razorpay")
public class RazorPaymentService implements PaymentService { ... }

@RestController
public class PaymentController {
    @Autowired  // Injects StripePaymentService (primary)
    private PaymentService paymentService;
}
```

### **Scenario 2: Only `@Qualifier`**
```java
@Service
@Profile("stripe")
public class StripePaymentService implements PaymentService { ... }

@Service
@Profile("razorpay")
public class RazorPaymentService implements PaymentService { ... }

@RestController
public class PaymentController {
    @Autowired
    @Qualifier("stripePaymentService")  // Explicitly choose StripePaymentService
    private PaymentService paymentService;
}
```

### **Scenario 3: Both `@Primary` and `@Qualifier`**
```java
@Service
@Profile("stripe")
@Primary
public class StripePaymentService implements PaymentService { ... }

@Service
@Profile("razorpay")
public class RazorPaymentService implements PaymentService { ... }

@RestController
public class PaymentController {
    @Autowired
    @Qualifier("razorPaymentService")  // Overrides primary, injects RazorPaymentService
    private PaymentService paymentService;
}
```

---

## **Best Practices**

### **For Your Payment Service Use Case:**

**Option 1: Use `@Primary` (Recommended for simplicity)**
```java
@Service
@Profile("stripe")
@Primary
public class StripePaymentService implements PaymentService { ... }

// Controller can use simple @Autowired
@Autowired
private PaymentService paymentService;
```

**Option 2: Use `@Qualifier` (More explicit)**
```java
// No @Primary needed
@Service
@Profile("stripe")
public class StripePaymentService implements PaymentService { ... }

// Controller must specify which bean
@Autowired
@Qualifier("stripePaymentService")
private PaymentService paymentService;
```

---

## **Summary**

- **`@Primary`** = Automatic default choice, no `@Qualifier` needed
- **`@Qualifier`** = Explicit choice, always needed when multiple beans exist
- **Both** = `@Qualifier` overrides `@Primary`

For your payment service scenario, **`@Primary` alone is sufficient** and keeps your code cleaner!