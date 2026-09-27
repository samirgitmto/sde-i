# Simple Factory vs GoF Factory Method Pattern

## Simple Factory Pattern (a.k.a. Static Factory)
- **Intent:** Centralizes object creation logic in one place, usually a static method.
- **How it works:**
  - A single factory class has a method (often static) that returns instances of different classes based on input.
  - The client calls this method instead of using `new`.
- **Inheritance:** No inheritance required; the factory is usually a utility class.
- **Extensibility:** To add new product types, you must modify the factory method (violates Open/Closed Principle).

**Example:**
```java
class ReportFactory {
    public static Report getReport(String type) {
        if (type.equals("audit")) return new AuditReport();
        if (type.equals("enrollment")) return new EnrollmentReport();
        // ...
    }
}
```

---

## GoF Factory Method Pattern
- **Intent:**
  - Defines an interface for creating an object, but lets subclasses decide which class to instantiate.
  - Lets a class defer instantiation to subclasses.
- **How it works:**
  - An abstract class or interface declares a factory method.
  - Subclasses override this method to create specific product objects.
- **Inheritance:** Relies on inheritance and polymorphism.
- **Extensibility:** To add new product types, you create new subclasses (adheres to Open/Closed Principle).

**Example:**
```java
abstract class ReportFactory {
    public abstract Report createReport();
}
class AuditReportFactory extends ReportFactory {
    public Report createReport() { return new AuditReport(); }
}
class EnrollmentReportFactory extends ReportFactory {
    public Report createReport() { return new EnrollmentReport(); }
}
```

---

## Summary Table
| Aspect                | Simple Factory                | GoF Factory Method           |
|-----------------------|------------------------------|-----------------------------|
| Centralization        | One class, static method     | Hierarchy of factories      |
| Extensibility         | Modify factory method        | Add new subclasses          |
| Inheritance           | Not required                 | Required                    |
| GoF Pattern?          | No                           | Yes                         |
| Open/Closed Principle | Violated                     | Respected                   |

---

## In Short
- **Simple Factory:** One place, static method, not a GoF pattern, less extensible.
- **Factory Method (GoF):** Uses inheritance, extensible via subclassing, is a GoF pattern. 