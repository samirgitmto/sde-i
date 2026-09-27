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
