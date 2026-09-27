# Spring Boot Prep

Personal Spring Boot study repo for SDE prep — notes, scenarios, and small lab projects.

## Layout

```text
Spring Boot/
├── basics/                 # Fundamentals + lab code
├── advanced/               # Intermediate / advanced topic folders
├── sb-basics.md            # Basics topic checklist
├── sb-advanced.md          # Advanced topic checklist
└── scenarios1.md
```

## Basics (`basics/`)

| Topic | Contents in this repo |
|-------|------------------------|
| `t1 sb basics` | Topic folder (notes TBD) |
| `t2 dependency injection` | Notes + scenarios + `basics-di` lab |
| `t3 mvc basics` | Topic folder (notes TBD) |
| `t4 spring data jpa` | Notes + scenarios + `jpa-scenarios` lab |
| `t5 spring security` | Security notes, OAuth/Keycloak docs, BPMN notes, `security-demo` lab |
| `t6 Actuator` | Notes + scenarios + `health-monitor` lab |
| `t7 Configuration & profiles` | Topic folder (notes TBD) |
| `t8 Exception Handling & Validation` | Topic folder (notes TBD) |
| `t9 Microservices Concepts` | Topic folder (notes TBD) |
| `t10 messaging` | Topic folder (notes TBD) |
| `t11 Miscellaneous Topics` | Topic folder (notes TBD) |

### Labs included

- `basics/t2 dependency injection/basics-di` — DI, conditional beans, circular dependency scenarios
- `basics/t4 spring data jpa/jpa-scenarios` — custom query / JPQL, relationships & fetch
- `basics/t5 spring security/security-demo` — Spring Security basics lab
- `basics/t6 Actuator/health-monitor` — Actuator / custom health indicators

### Notes included under security

- `basics/t5 spring security/Docs/` — OAuth2, Keycloak, auth flow notes
- `basics/t5 spring security/bpmn-docs/` — BPMN / Flowable study notes

## Advanced (`advanced/`)

Topic folders (structure ready; deepen as you go):

1. Dependency injection  
2. MVC internals  
3. Spring Data JPA  
4. Spring Security  
5. Async processing & scheduling  
6. Caching  
7. Actuator & observability  
8. Performance & production concerns  
10. Miscellaneous topics  

See `sb-advanced.md` / `advanced/sb-advanced.md` for the full checklist.

## Run a lab

```bash
cd "basics/t2 dependency injection/basics-di"
./mvnw spring-boot:run
```

Same pattern for `jpa-scenarios`, `security-demo`, and `health-monitor`.

## Ignore policy

Build output, IDE files, secrets, archives, and the separate-repo folders above are excluded via `.gitignore`.
