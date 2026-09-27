Here's a comprehensive breakdown of the GraphQL concepts and Spring Boot annotations at play in your setup.

## 1. GraphQL Schema (SDL — Schema Definition Language)

```graphql
type User {
    id: ID!
    name: String!
    email: String!
}
```

- **`type`** defines an object type — the shape of data clients can query.
- **Scalar types**: `ID`, `String`, `Int`, `Float`, `Boolean` are GraphQL's built-in scalars. `ID` is serialized as a string but signals "this is an identifier" semantically.
- **`!` (non-null modifier)**: marks a field as required. `name: String!` means this field can never return `null` — if it does, GraphQL throws an execution error. Omitting `!` means the field is nullable.
- **`[User!]!`**: a non-null list of non-null `User` objects. Read these right to left — `[User!]!` = "a list that itself is never null, containing users that are never null."

## 2. Root Operation Types

```graphql
type Query { ... }
type Mutation { ... }
```

Every GraphQL schema has special root types:
- **`Query`** — defines all read operations (like GET in REST).
- **`Mutation`** — defines all write operations (create/update/delete, like POST/PUT/PATCH/DELETE in REST).
- There's also a `Subscription` type (not used here) for real-time/streaming updates over WebSockets.

Each field on `Query`/`Mutation` becomes a callable "operation" from the client side, e.g. `query { user(id: "1") { name } }`.

## 3. Field Arguments

```graphql
user(id: ID!): User
createUser(name: String!, email: String!): User
patchUser(id: ID!, name: String, email: String): User
```

Arguments are declared inline on the field, each with their own type and nullability. This is how you designed the **PUT vs. PATCH distinction**:
- `updateUser` — `name` and `email` are both `String!` (required) → forces a full replacement, mirroring PUT semantics.
- `patchUser` — `name` and `email` are nullable `String` → client can omit fields, and only supplied ones get applied, mirroring PATCH semantics.

This is a really clean way to express REST verb semantics in GraphQL, since GraphQL itself doesn't have verbs — everything is just a named field under `Mutation`.

## 4. Spring for GraphQL Annotations

| Annotation | Purpose |
|---|---|
| `@Controller` | Same Spring stereotype as MVC controllers — marks the class as a component holding request-handling methods, but here they're GraphQL data-fetchers instead of HTTP endpoints. |
| `@QueryMapping` | Maps a method to a field on the `Query` type. By default, matches the method name to the schema field name (`user` → `user`, `users` → `users`). |
| `@MutationMapping` | Same idea, but maps to a field on the `Mutation` type. |
| `@Argument` | Binds a GraphQL field argument to a Java method parameter, matching by name (`id`, `name`, `email`). Handles type coercion from GraphQL scalars to Java types (`ID` → `Long`, `String` → `String`, etc.). |

### How does `@QueryMapping` / `@MutationMapping` matching work?

At startup, Spring scans `@Controller` beans for these annotations and registers each method as a **data fetcher** for one schema field:

1. Read the SDL field names on `Query` / `Mutation` (from `schema.graphqls`).
2. For each annotated method, decide the GraphQL field name:
   - **Default (implicit):** use the **Java method name**.
   - **Explicit:** use `name` / `value` on the annotation, e.g. `@MutationMapping(name = "deleteUser")`.
3. Bind that method to the matching SDL field. Names must line up or the field has no resolver.

**Default mapping** (method name = schema field) — most of `graphql-demo`:

```java
@MutationMapping                    // looks for Mutation.createUser
public User createUser(...) { ... }
```

```text
SDL:  createUser(...)
Java: createUser(...)     ← same name → wired automatically
```

**Explicit mapping** (method name ≠ schema field) — `deleteUser` in this project:

```graphql
# schema.graphqls
deleteUser(id: ID!): Boolean!
```

```java
// UserGraphQLController — Java name is removeUser on purpose
@MutationMapping(name = "deleteUser")   // must say which SDL field
public Boolean removeUser(@Argument Long id) { ... }
```

```text
SDL:  deleteUser
Java: removeUser + name = "deleteUser"   ← explicit bridge

Without name = "deleteUser", Spring would search for Mutation.removeUser,
which does not exist in the SDL → binding fails / field unresolved.
```

Use default when names can stay identical (clearest). Use explicit when you want a Java-friendly method name (`removeUser`) but a GraphQL API name (`deleteUser`), or when renaming one side without renaming the other.

Try it in GraphiQL after restart:

```graphql
mutation {
  deleteUser(id: 2)
}
```

(Clients still call `deleteUser` — they never see the Java name `removeUser`.)

Under the hood, Spring for GraphQL uses `graphql-java` to build an executable schema by binding your SDL file to these annotated methods — this is the **schema-first** approach (as opposed to code-first, where the schema is generated from annotated Java classes).

### What does “SDL file” mean?

**SDL** = **Schema Definition Language**. It is the text language GraphQL uses to declare types, fields, arguments, and operations. An “SDL file” is simply a file (usually ending in `.graphqls` or `.graphql`) that contains that schema text — the contract clients and server agree on.

In `graphql-demo`, that file is:

```text
src/main/resources/graphql/schema.graphqls
```

Spring Boot auto-loads every `*.graphqls` / `*.graphql` file under `classpath:graphql/` at startup. That SDL is the **source of truth** for what operations exist. Example from this project:

| Declared in SDL (`schema.graphqls`) | Implemented in Java (`UserGraphQLController`) |
|---|---|
| `users: [User!]!` | `@QueryMapping List<User> users()` |
| `user(id: ID!): User` | `@QueryMapping User user(@Argument Long id)` |
| `createUser(name: String!, email: String!): User` | `@MutationMapping User createUser(...)` |
| `updateUser(...)` / `patchUser(...)` | matching `@MutationMapping` methods (default / same name) |
| `deleteUser(id: ID!): Boolean!` | `@MutationMapping(name = "deleteUser") Boolean removeUser(...)` — **explicit** |

`graphql-java` parses the SDL into a schema graph, then Spring wires each schema field to the matching annotated method by **name**. If you add `users` to the SDL but forget the Java method (or misspell the method name), startup or request execution fails — the schema promised a field that has no resolver.

### Schema-first vs code-first (with this project as the example)

```text
Schema-first (what graphql-demo uses)

  1. You write schema.graphqls          ← contract first
  2. You write @QueryMapping / @MutationMapping methods that match it
  3. Spring + graphql-java bind SDL fields → Java methods
  4. Result: an executable schema (can run queries/mutations)

Code-first (not used here)

  1. You annotate Java classes/methods as the schema
  2. A library generates the GraphQL schema from that Java
  3. No hand-written .graphqls file (or it is generated)
```

So when the paragraph says “binding your SDL file to these annotated methods,” it means: `schema.graphqls` defines *what* is allowed; `UserGraphQLController` defines *how* each field gets its data. Together they become the runnable GraphQL API at `POST /graphql`.

## 5. Resolver / Data Fetcher Pattern

Each `@QueryMapping`/`@MutationMapping` method is essentially a **resolver** (GraphQL terminology) or **data fetcher** (graphql-java terminology) — a function responsible for producing the value of one specific schema field. Notice:
- `user(id)` and `users()` resolve `Query` fields.
- `createUser`, `updateUser`, `patchUser` resolve `Mutation` fields.
- Each delegates straight to `UserRepository` (Spring Data), so the resolver is a thin adapter layer between GraphQL and your persistence layer — same role a `@RestController` method plays for REST, just without the HTTP verb/URL binding.

## 6. Null-handling pattern in `patchUser`

```java
if (name != null) { user.setName(name); }
if (email != null) { user.setEmail(email); }
```

This is the actual mechanism that makes `patchUser` behave like PATCH: since the argument types are nullable in the schema, the client can send a mutation omitting `email` entirely, it arrives as `null` in Java, and the field is left untouched. `updateUser`'s required arguments mean the client *must* send both, or the query itself is invalid before it even reaches your method (GraphQL validates required arguments before execution).

## 7. Key conceptual differences from REST worth noting

- **Single endpoint**: All of this — queries and mutations — is exposed over one HTTP endpoint (typically `POST /graphql`), unlike REST's multiple resource URLs.
- **Client-specified shape**: A REST `GET /users/1` always returns the full `User` representation; a GraphQL `query { user(id:1) { name } }` lets the client ask for only `name`, avoiding over-fetching.
- **Verbs become field names**: There's no PUT/PATCH/DELETE at the protocol level — semantics like yours (`updateUser` vs `patchUser`) are a *convention* you designed into the schema, not something GraphQL enforces natively.
- **Errors don't use HTTP status codes** the way REST does — GraphQL typically returns `200 OK` even on business-logic errors, with error details in a separate `errors` array in the response body.

If you want, I can also put together a short comparison table of REST vs GraphQL semantics (verbs, status codes, over/under-fetching, versioning) as a follow-up reference.