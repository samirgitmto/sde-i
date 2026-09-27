# Abstract Factory Pattern in Java

## Overview
The Abstract Factory pattern provides an interface for creating families of related or dependent objects without specifying their concrete classes. It is useful when the system needs to be independent of how its objects are created, composed, and represented.

---

## Structure in This Project

### 1. Abstract Factory Interface
```java
public interface ReportFormatFactory {
    Formatter getReportFormatter();
}
```
- Declares a method for creating an abstract product (`Formatter`).

### 2. Product Interface
```java
public interface Formatter {
    String generateFormattedReport(Report report);
}
```
- Represents the product to be created by the factory.

### 3. Concrete Products
```java
public class CsvFormatter implements Formatter { /* ... */ }
public class PdfFormatter implements Formatter { /* ... */ }
public class HtmlFormatter implements Formatter { /* ... */ }
```
- Implement the `Formatter` interface, providing specific formatting logic.

### 4. Concrete Factories
```java
public class CsvReportFormatFactory implements ReportFormatFactory {
    public Formatter getReportFormatter() {
        return new CsvFormatter();
    }
}

public class PdfReportFormatFactory implements ReportFormatFactory {
    public Formatter getReportFormatter() {
        return new PdfFormatter();
    }
}

public class HtmlReportFormatFactory implements ReportFormatFactory {
    public Formatter getReportFormatter() {
        return new HtmlFormatter();
    }
}
```
- Each factory creates a specific type of `Formatter`.

### 5. Client Usage Example
```java
Report pwdExpiryReport = new PasswordExpiredReport();
ReportFormatFactory reportFormatFactory = new HtmlReportFormatFactory();
Formatter formatter = reportFormatFactory.getReportFormatter();
String result = formatter.generateFormattedReport(pwdExpiryReport);
System.out.println(result);
```
- The client uses only the abstract factory and product interfaces, not concrete classes directly.

---

## Benefits
- **Encapsulation of object creation:** The client code is decoupled from the concrete classes it needs to instantiate.
- **Consistency among products:** Ensures that products created by a factory are compatible.
- **Scalability:** Easy to introduce new product families by adding new factories and products.

---

## Summary Table
| Component                  | Implementation Example                       |
|----------------------------|----------------------------------------------|
| Abstract Factory Interface | `ReportFormatFactory`                        |
| Product Interface          | `Formatter`                                  |
| Concrete Products          | `CsvFormatter`, `PdfFormatter`, `HtmlFormatter` |
| Concrete Factories         | `CsvReportFormatFactory`, etc.               |
| Client Usage               | Uses only interfaces, not concrete classes   |

---

## References
- [GoF Design Patterns: Abstract Factory](https://en.wikipedia.org/wiki/Abstract_factory_pattern)
- [Oracle Java Documentation](https://docs.oracle.com/javase/tutorial/java/concepts/index.html) 

---

# Abstract Factory Pattern: Key Distinctions and Pseudocode Example

## Key Distinguishing Aspects

### 1. **Product Family Scope**
- **Current Implementation:**
  - The product family consists of `Formatter` and `Validator` for each format (PDF, CSV, HTML).
  - The factory creates a formatter and a validator for a given format.
- **Textbook Abstract Factory Pattern:**
  - The product family consists of all types of reports (e.g., `AuditReport`, `EnrollmentReport`, etc.) for each format.
  - The factory creates each type of report for a given format, ensuring all products belong to the same family (e.g., all PDF reports).

### 2. **Extensibility**
- **Adding a New Format:**
  - *Current:* Add a new factory and new formatter/validator classes.
  - *Textbook:* Add a new factory and implement all report creation methods for the new format.
- **Adding a New Report Type:**
  - *Current:* Add a new formatter/validator if needed, and update factories.
  - *Textbook:* Add a new method to the abstract factory and implement it in all concrete factories.

### 3. **Consistency Guarantee**
- Both approaches ensure that all products created by a factory are consistent (e.g., all PDF or all CSV), but the textbook version does this for a broader set of products (all report types).

---

## Pseudocode: Textbook Abstract Factory Pattern for Report Families

```java
// Abstract Product Interfaces
interface AuditReport { /* ... */ }
interface EnrollmentReport { /* ... */ }
// ... other report types

// Abstract Factory
interface ReportFormatFactory {
    AuditReport createAuditReport();
    EnrollmentReport createEnrollmentReport();
    // ... other report creation methods
}

// Concrete Factory for PDF
class PdfReportFactory implements ReportFormatFactory {
    AuditReport createAuditReport() { return new PdfAuditReport(); }
    EnrollmentReport createEnrollmentReport() { return new PdfEnrollmentReport(); }
    // ...
}

// Concrete Factory for CSV
class CsvReportFactory implements ReportFormatFactory {
    AuditReport createAuditReport() { return new CsvAuditReport(); }
    EnrollmentReport createEnrollmentReport() { return new CsvEnrollmentReport(); }
    // ...
}

// Usage Example
ReportFormatFactory factory = new PdfReportFactory();
AuditReport audit = factory.createAuditReport();
EnrollmentReport enroll = factory.createEnrollmentReport();
// All are PDF reports, guaranteed by the factory
```

---

**Summary:**
- The textbook Abstract Factory pattern is ideal when you need to create families of related products (e.g., all types of reports in a given format), not just a pair of related objects (like formatter/validator). This ensures consistency and scalability as your product family grows. 