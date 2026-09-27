1. Spring Boot Basics
Auto-configuration

Spring Boot Starter dependencies

application.properties / application.yml

Main class and @SpringBootApplication annotation

Spring Boot DevTools

🔹 2. Dependency Injection & Core Annotations
@Component, @Service, @Repository, @Controller

@Autowired, constructor vs field injection

@Qualifier, @Primary

@Configuration and @Bean

🔹 3. Spring MVC (Web Layer)
REST APIs with @RestController, @RequestMapping, etc.

Path variables, request parameters

@RequestBody and @ResponseBody

Exception handling with @ControllerAdvice, @ExceptionHandler

Content negotiation (JSON/XML)

🔹 4. Spring Data JPA & Persistence
Entity mappings (@Entity, @Id, @GeneratedValue, etc.)

Repositories: CrudRepository, JpaRepository

Derived queries & custom JPQL

Transactions: @Transactional

Lazy vs eager loading

Projections and DTO mapping

🔹 5. Spring Boot Security
Basic authentication & JWT

Role-based access control (RBAC)

Security filter chain in Spring Security 6

Custom user details service

CSRF, CORS configuration


🔹 6. Spring Boot Actuator
Health checks, metrics, endpoints

Customizing and securing actuator endpoints

Integration with monitoring tools (like Prometheus/Grafana)

🔹 7. Configuration and Profiles
@Value, @ConfigurationProperties

Spring profiles (@Profile, spring.profiles.active)

Externalized configuration (Env variables, command line, etc.)

🔹 8. Exception Handling & Validation
Global exception handling

@Valid, @Validated, Bean Validation API (JSR-380)

Custom validation annotations

🔹 9. Microservices Concepts (Spring Cloud Basics – if applicable)
Service discovery (Eureka)

Load balancing (Ribbon)

API Gateway (Spring Cloud Gateway)

Circuit Breaker (Resilience4j or Hystrix)

Distributed tracing (Zipkin, Sleuth)

🔹 10. Messaging (if applicable to your job role)
RabbitMQ or Kafka integration

@KafkaListener, @RabbitListener

🔹 11. Miscellaneous but Interview-Favorite Topics
Caching with @Cacheable, @CacheEvict

Scheduling with @Scheduled

Asynchronous execution with @Async

Logging (SLF4J, Logback)


🔹 12. Spring Boot Testing
Unit testing with JUnit + Mockito

Integration testing with @SpringBootTest

MockMvc for controller testing

Test slices (@WebMvcTest, @DataJpaTest, etc.)