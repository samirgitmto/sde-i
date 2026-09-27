Absolutely! Here are **two realistic Spring Data JPA scenarios** tailored for **SDE II interviews**, designed to test both conceptual understanding and hands-on experience:

---

## ✅ **Scenario 1: Custom Query Methods and JPQL**

**Problem:**

You're building a user management system. You need to:

* Fetch users by email address.
* Fetch all users who registered after a certain date.

Use **Spring Data JPA** repository method conventions and **JPQL** where necessary.

**Expected Concepts Tested:**

* `JpaRepository`
* Derived query methods
* `@Query` with JPQL
* `@Param` usage

**Sample Code Sketch:**

```java
// User.java
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String name;
    private LocalDate registrationDate;

    // constructors, getters, setters
}

// UserRepository.java
public interface UserRepository extends JpaRepository<User, Long> {

    // Derived query
    Optional<User> findByEmail(String email);

    // Custom JPQL query
    @Query("SELECT u FROM User u WHERE u.registrationDate > :date")
    List<User> findUsersRegisteredAfter(@Param("date") LocalDate date);
}
```

---

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

---

Would you like me to follow these scenarios with **unit test examples**, **transaction handling cases**, or a **query optimization discussion** for Spring Data JPA?
