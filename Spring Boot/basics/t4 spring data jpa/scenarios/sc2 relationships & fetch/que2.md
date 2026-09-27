## ✅ **Scenario 2: Entity Relationships and Fetch Strategies**

**Problem:**

You're designing an e-commerce system. Each **Order** is placed by a **Customer** and contains multiple **OrderItems**. You need to:

* Map the relationships using JPA annotations.
* Optimize fetching to avoid the N+1 problem.

**Expected Concepts Tested:**

* OneToMany, ManyToOne mappings
* `fetch = FetchType.LAZY` vs `EAGER`
* Use of `@JoinColumn`, `@OneToMany(mappedBy=...)`
* Understanding of cascading and transaction boundaries

**Sample Code Sketch:**

```java
// Customer.java
@Entity
public class Customer {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();
}

// Order.java
@Entity
@Table(name = "orders") // avoid conflict with SQL reserved word
public class Order {
    @Id
    @GeneratedValue
    private Long id;

    private LocalDate orderDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();
}

// OrderItem.java
@Entity
public class OrderItem {
    @Id
    @GeneratedValue
    private Long id;

    private String productName;
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
}
```

**Follow-Up Interview Questions:**

* How would you fetch an order with its items and customer in a single query?
* What is the impact of using `FetchType.EAGER` vs `LAZY`?
* What is the N+1 select problem and how do you avoid it?