## ✅ **Scenario 1: Custom Health Indicator for External Dependency**

**Problem:**

You have a Spring Boot application that depends on an external REST service (e.g., a third-party payment API). The default `/actuator/health` endpoint always shows "UP", but you want it to reflect whether this external service is reachable.

Create a **custom health indicator** that checks the availability of the external service.

**Expected Concepts Tested:**

* Custom `HealthIndicator` implementation
* Integration with `/actuator/health`
* `Health.up()` / `Health.down()` usage

**Sample Code Sketch:**

```java
@Component
public class ExternalServiceHealthIndicator implements HealthIndicator {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Health health() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity("https://api.thirdparty.com/status", String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return Health.up().withDetail("ThirdPartyAPI", "Available").build();
            }
        } catch (Exception ex) {
            return Health.down()
                         .withDetail("ThirdPartyAPI", "Unavailable")
                         .withException(ex)
                         .build();
        }
        return Health.unknown().build();
    }
}
```

**Follow-Up Interview Questions:**

* How do you secure actuator endpoints in production?
* How would you expose this health info to a monitoring system like Prometheus?

---
