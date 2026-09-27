That's a solid use case — dashboards are actually one of the textbook scenarios where GraphQL earns its complexity over REST. A few thoughts specific to your Active Directory context:

## Why it fits well

- **Nested, relational data** — AD objects aren't flat. A user has group memberships, an OU, manager/reports hierarchy, login history, permissions, etc. A dashboard widget showing "user + their groups + their last 5 logins" would be multiple REST round-trips (or a bloated custom endpoint); in GraphQL it's one query.
- **Different widgets, different shapes** — a dashboard usually has many small panels (user count by OU, recently locked accounts, expiring passwords, group membership charts). Each wants a different slice of the same underlying AD data. Instead of building a REST endpoint per widget, each panel just sends its own GraphQL query against the same schema.
- **Avoiding over-fetching at scale** — with thousands of users, a REST `GET /users` returning full objects (every AD attribute) for a dashboard that only needs `name` + `status` is wasteful. GraphQL lets each query request exactly the fields needed.

## Things worth watching for at AD scale specifically

- **Pagination is essential** — for `users: [User!]!` returning thousands of records, you'll want cursor-based pagination (Relay-style `edges`/`pageInfo`) rather than returning everything, or offset-based at minimum.
- **N+1 query problem** — if a query fetches `users { groups { name } }`, a naive resolver fires one query per user to get groups. This gets painful fast at AD scale. Spring for GraphQL supports **`@BatchMapping`** (or `DataLoader` pattern) specifically to batch these into one query — worth adopting early rather than retrofitting.
- **AD calls are often LDAP-based, not DB-based** — if your repository layer is hitting LDAP under the hood rather than a SQL store, complex filtering (e.g., filter by OU + last login + group) can be slower than a DB query would be. Worth checking whether you're caching/indexing frequently-dashboarded attributes rather than hitting LDAP live per query.
- **Query complexity/depth limiting** — since clients can shape arbitrarily nested queries, it's worth capping query depth or complexity (Spring for GraphQL supports instrumentation for this) so a dashboard panel can't accidentally request a deeply nested, expensive tree across thousands of users.

Given you're coming from Spring MVC/REST, this sounds like a genuinely good fit rather than GraphQL-for-its-own-sake — the "1000s of users + complex nested querying" description is close to the canonical case it was designed for.