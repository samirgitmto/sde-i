# **Comprehensive Note on JPQL vs. Native SQL Queries in JPA/Hibernate**

## **1. JPQL (Java Persistence Query Language)**
JPQL is an object-oriented query language used in JPA to interact with entities (classes) rather than directly with database tables.

### **Key Features:**
- **Object-Oriented**: Works with **entity classes and fields**, not database tables and columns.
- **Database-Agnostic**: Portable across different databases (Hibernate converts it to the appropriate SQL dialect).
- **Case-Sensitive**: Entity and field names must match exactly (case matters).
- **Supports Polymorphism**: Can query inheritance hierarchies (e.g., `SELECT e FROM Employee e` includes subclasses).
- **Uses `@Query` with `nativeQuery = false` (default)**.

### **Syntax Examples:**
```java
// Basic JPQL
@Query("SELECT u FROM User u WHERE u.email = :email")
User findByEmail(@Param("email") String email);

// JOIN in JPQL (uses entity relationships)
@Query("SELECT u FROM User u JOIN u.orders o WHERE o.status = 'COMPLETED'")
List<User> findUsersWithCompletedOrders();

// Pagination support (Spring Data JPA)
@Query("SELECT u FROM User u ORDER BY u.registrationDate DESC")
Page<User> findUsersSortedByRegistrationDate(Pageable pageable);
```

### **Pros:**
✅ **Type-safe** (avoids SQL injection).  
✅ **Portable** across databases.  
✅ **Works directly with entities** (no manual mapping needed).  
✅ **Supports pagination (`Pageable`)** easily.  

### **Cons:**
❌ **Limited to JPA features** (no DB-specific functions unless supported).  
❌ **No direct DDL operations** (e.g., `CREATE TABLE`).  
❌ **Sometimes less efficient** than optimized native SQL.  

---

## **2. Native SQL Queries**
Native queries allow direct execution of database-specific SQL.

### **Key Features:**
- **Database-Specific**: Uses raw SQL (must match the DB dialect).
- **Works with Tables/Columns**: References actual database structures.
- **Requires Manual Mapping**: Results may need `@SqlResultSetMapping` or DTO projections.
- **Uses `@Query` with `nativeQuery = true`**.

### **Syntax Examples:**
```java
// Basic native query
@Query(value = "SELECT * FROM user_table WHERE email = :email", nativeQuery = true)
User findByEmailNative(@Param("email") String email);

// Complex native query with JOIN
@Query(value = """
    SELECT u.* FROM user_table u 
    JOIN orders o ON u.id = o.user_id 
    WHERE o.status = 'COMPLETED'
    """, nativeQuery = true)
List<User> findUsersWithCompletedOrdersNative();

// Native query with pagination (requires countQuery)
@Query(
    value = "SELECT * FROM user_table ORDER BY registration_date DESC",
    countQuery = "SELECT COUNT(*) FROM user_table",
    nativeQuery = true
)
Page<User> findAllUsersSortedNative(Pageable pageable);
```

### **Pros:**
✅ **Full SQL Power**: Can use DB-specific features (e.g., `JSONB` in PostgreSQL).  
✅ **Better Performance** for complex queries.  
✅ **Supports Stored Procedures** and functions.  

### **Cons:**
❌ **Not portable** (tied to a specific database).  
❌ **Prone to SQL injection** if not using parameters.  
❌ **Requires manual mapping** (unless using `@Entity` result classes).  

---

## **3. When to Use JPQL vs. Native Queries**
| **Scenario**                | **JPQL** | **Native SQL** |
|----------------------------|---------|--------------|
| Simple CRUD operations      | ✅ Best | ❌ Overkill |
| Cross-database compatibility | ✅ Yes | ❌ No |
| Complex joins with entities | ✅ Good | ⚠ Possible |
| DB-specific optimizations   | ❌ No | ✅ Best |
| Calling stored procedures   | ❌ No | ✅ Yes |
| Need pagination (`Pageable`)| ✅ Easy | ⚠ Requires `countQuery` |
| Performance-critical queries| ⚠ Decent | ✅ Best |

---

## **4. Best Practices**
### **For JPQL:**
- Use **entity and field names** (not table/column names).
- Prefer **named parameters (`:param`)** over positional (`?1`).
- Use **constructor expressions** for DTO projections:
  ```java
  @Query("SELECT new com.example.UserDTO(u.id, u.name) FROM User u")
  List<UserDTO> findUserDTOs();
  ```

### **For Native SQL:**
- Always use **parameter binding** (`:param`) to prevent SQL injection.
- For complex results, use:
  - `@SqlResultSetMapping` + `@Entity` (if returning entities).
  - **DTO interfaces** (Spring Data JPA projections):
    ```java
    public interface UserSummary {
        String getName();
        String getEmail();
    }
    
    @Query(value = "SELECT name, email FROM user_table", nativeQuery = true)
    List<UserSummary> getUserSummaries();
    ```
- **Avoid `SELECT *`** (explicitly list columns for clarity).

---

## **5. Summary**
- **Use JPQL** for most cases (portable, type-safe, maintainable).  
- **Use Native SQL** when:  
  - You need **DB-specific features** (e.g., window functions).  
  - JPQL **performance is insufficient**.  
  - Working with **legacy databases** or stored procedures.  

Both approaches can coexist in the same repository, allowing flexibility where needed.