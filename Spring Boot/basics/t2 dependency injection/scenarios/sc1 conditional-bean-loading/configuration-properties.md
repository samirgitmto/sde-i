# `@ConfigurationProperties` in Spring Boot

`@ConfigurationProperties` is a powerful annotation in Spring Boot that provides a type-safe way to bind external configuration properties (from `.properties` or `.yml` files) to Java objects.

## Key Features:

1. **Type-Safe Configuration**: Converts and validates configuration values into proper Java types
2. **Hierarchical Binding**: Matches nested property structures
3. **Relaxed Binding**: Supports different property naming conventions (kebab-case, camelCase, etc.)
4. **Validation**: Works with JSR-303 validation annotations

## Basic Usage:

```java
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String name;
    private int version;
    private List<String> servers = new ArrayList<>();
    
    // Standard getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    // ...
}
```

With this class, Spring will automatically bind properties like:

```yaml
app:
  name: MyApp
  version: 2
  servers:
    - server1.example.com
    - server2.example.com
```

## Enabling Configuration Properties

You need to enable it in one of these ways:

1. **On the class with `@EnableConfigurationProperties`**:
```java
@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class MyApp { ... }
```

2. **Or by annotating the class with `@Component`**:
```java
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties { ... }
```

## Why It's Useful in Your Payment Service Example

In your payment provider scenario, `@ConfigurationProperties`:

1. Provides clean access to the configured payment provider
2. Allows default values (like defaulting to "stripe")
3. Makes configuration changes type-safe and validated
4. Keeps configuration separate from business logic

It's particularly valuable when you have multiple related configuration properties that belong together conceptually.