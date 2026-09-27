Intermediate to Advanced Spring Boot Topics for SDE II Interviews
🔹 1. Advanced Dependency Injection & Configuration
Conditional bean creation:

@Conditional, @ConditionalOnProperty, @ConditionalOnMissingBean

Advanced @ConfigurationProperties:

Nested property binding

Custom validators

Reloading configuration with Spring Cloud (if relevant)

Lazy initialization (@Lazy)

Using Environment and ApplicationContext directly

Creating and registering beans dynamically (e.g., via BeanDefinitionRegistry)

🔹 2. Spring MVC Internals
Request lifecycle: DispatcherServlet → HandlerMapping → HandlerAdapter → ViewResolver

Difference between @RestControllerAdvice and @ControllerAdvice

Custom exception structure using ProblemDetails (RFC 7807)

Advanced argument resolvers (HandlerMethodArgumentResolver)

ResponseEntity vs @ResponseBody: control over HTTP response

Writing reusable interceptors and filters for logging, tracing, or rate-limiting

🔹 3. Spring Data JPA – Advanced
Entity lifecycle callbacks (@PrePersist, @PostLoad, etc.)

Bulk update queries and their implications (flush, clear, sync)

Auditing with @CreatedDate, @LastModifiedDate via @EnableJpaAuditing

Soft deletes implementation strategies

Pagination and sorting: Pageable, Slice, and performance tips

Handling bidirectional relationships properly (infinite recursion, @JsonManagedReference)

🔹 4. Spring Security – In-Depth
Creating custom AuthenticationProvider

Stateless authentication using JWT: full setup with filters, token validation, refresh tokens

Building login endpoints with password verification logic

Setting up method-level security:

@PreAuthorize, SpEL in access control

Security context propagation in async or reactive flows

Role hierarchies and permission-based access control

Setting up multiple security filter chains (e.g., public and private APIs)

CSRF tuning and securing APIs for frontend integrations (SPA)

🔹 5. Asynchronous Processing & Scheduling
Thread pool tuning with @EnableAsync and TaskExecutor

Retry mechanisms with @Retryable, @Recover from Spring Retry

Handling exceptions in async flows

Fixed delay vs fixed rate vs cron expressions

Coordinating stateful scheduled jobs using persistent stores (e.g., Quartz or DB-backed locks)

🔹 6. Caching – Intermediate to Advanced
Custom KeyGenerator implementations

Cache eviction triggers: time, size, manual

Conditional caching: condition, unless in @Cacheable

Abstracting caching logic to a separate layer

Using CacheManager and multiple cache providers

Distributed caching using Redis: configuration, serialization (JSON vs binary)

🔹 7. Spring Boot Actuator & Observability
Exposing and securing actuator endpoints (management.endpoints.web.exposure.include)

Custom health indicators (HealthIndicator)

Creating custom metrics with Micrometer (MeterRegistry)

Integrating with Prometheus / Grafana

HTTP tracing via Spring Cloud Sleuth

Log correlation with trace ID and span ID

🔹 8. Performance & Production Concerns
Tuning HikariCP (maxPoolSize, idleTimeout, etc.)

Lazy init strategies for startup performance

Monitoring GC and memory via Actuator

Hibernate optimization:

Disabling second-level cache where not needed

Fetch joins vs @EntityGraph to avoid N+1 queries

Creating efficient APIs:

Limit fields (GraphQL-style patterns or Jackson Views)

Partial updates (PATCH handling, DTO diff strategies)

🔹 9. Testing in Spring Boot (Advanced)
Test Slicing:

@WebMvcTest, @DataJpaTest, @JsonTest

Testcontainers:

Spinning up real Postgres, Redis, Kafka containers for integration tests

MockMvc with custom filters/security

Mocking with @MockBean vs Mockito manually

Handling authentication in tests (mocking security context, injecting users)

SQL scripts for test DB setup and teardown (@Sql, @SqlGroup)

Context caching, test method ordering, and isolation concerns

🔹 10. Miscellaneous Topics
Mapping strategies:

MapStruct (compile-time safety)

Using Projections instead of Entities in APIs

DTO vs Entity separation best practices

Swagger/OpenAPI 3 integration with SpringDoc

Pagination best practices (Pageable, Slice, HATEOAS)

Error handling consistency (custom error format, error codes, RFC 7807)

Validation:

Grouped validation sequences

Cross-field and custom constraint validators

🧠 Keep in the Back Pocket (Last-Minute Revision)
These basics are fast to review but may still appear in interviews:

Lifecycle of Spring beans (@PostConstruct, @PreDestroy)

@SpringBootApplication → what annotations it includes

Difference: @ComponentScan vs @Import

@Transactional: propagation types, rollback conditions

REST API basics: status codes, content negotiation, idempotency

