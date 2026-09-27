# Efficient Fetching in JPA: Why Use JPQL with JOIN FETCH?

When working with JPA and entity relationships (such as `Customer` → `Order` → `OrderItem`), it's important to understand how data is loaded from the database and how to avoid performance pitfalls like the N+1 select problem.

## Default Fetch Types and Lazy Loading
- `@OneToMany` relationships (e.g., `Customer` to `Order`, `Order` to `OrderItem`) are **LAZY** by default.
- Fetching a `Customer` by ID only loads the customer data immediately. The `orders` list is a proxy and is not loaded until accessed.
- Accessing `orders` triggers a separate query. Accessing each order's `items` triggers yet another query per order.

## The N+1 Select Problem
Suppose a customer has N orders. If you fetch the customer and then access all orders and their items, JPA will execute:
- 1 query for the customer
- 1 query for the orders
- N queries for the order items (one per order)

This is called the **N+1 select problem** and can severely impact performance as data grows.

## Example: The N+1 Problem
```java
Customer customer = customerRepository.findById(id).get();
for (Order order : customer.getOrders()) {
    for (OrderItem item : order.getItems()) {
        // ...
    }
}
```
This code can result in 1 + 1 + N queries!

## Efficient Fetching with JPQL JOIN FETCH
To avoid this, use JPQL with `JOIN FETCH` to fetch all orders and their items in a single query:

```java
@Query("SELECT o FROM Order o JOIN FETCH o.items WHERE o.customer.id = :customerId")
List<Order> findOrdersWithItemsByCustomerId(@Param("customerId") Long customerId);
```

Or, using Spring Data JPA's `@EntityGraph`:
```java
@EntityGraph(attributePaths = "items")
List<Order> findByCustomerId(Long customerId);
```

## Comparison Table
| Approach                        | Number of Queries | Performance | Risk of N+1 Problem |
|----------------------------------|------------------|-------------|---------------------|
| Fetch Customer, access orders    | 1 + 1 + N        | Poor        | Yes                 |
| JPQL with JOIN FETCH (or EntityGraph) | 1                | Good        | No                  |

## Why Not Just Use FetchType.EAGER on @OneToMany?

While you can annotate your @OneToMany relationship with `fetch = FetchType.EAGER`, this is generally **not recommended** for several reasons:

- **EAGER fetching always loads the collection** whenever the parent entity is loaded, even if you don't need it.
- For large collections, this can cause significant performance issues and memory usage.
- If you have multiple EAGER relationships, Hibernate may join all related tables, leading to a **Cartesian product** (duplicate data, huge result sets).
- You lose control: you can't decide at query time whether you want the collection or not.
- Some JPA providers may still issue N+1 queries with EAGER, depending on the context.

### Real-World Example: The EAGER Fetching Problem

Suppose you have:
- 1,000 customers, each with 100 orders
- Each order has 10 order items

If you fetch all customers with EAGER fetching on orders and order items:
```java
@OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
private List<Order> orders;

@OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
private List<OrderItem> items;
```
And you run:
```java
List<Customer> customers = customerRepository.findAll();
```
Hibernate will try to join all related tables, resulting in:
- **1,000 x 100 x 10 = 1,000,000 rows** in the result set (with lots of duplicate data)
- Extremely slow queries and possible out-of-memory errors

**Best Practice:**
- Keep `@OneToMany` as LAZY (the default)
- Use `JOIN FETCH` or `@EntityGraph` in your queries when you actually need the related data

This approach gives you both performance and flexibility, and avoids the pitfalls of EAGER fetching on collections.

## Summary
- **Always use JOIN FETCH or EntityGraph** when you need to fetch related collections to avoid the N+1 select problem.
- This ensures all required data is loaded efficiently in a single query. 