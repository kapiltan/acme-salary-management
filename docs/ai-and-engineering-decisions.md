# ACME Salary Management — AI Usage & Engineering Decisions

## 1. AI Usage

AI was used as an engineering assistant throughout the development process.

The developer remained responsible for:

- defining requirements
- selecting the application architecture
- reviewing generated code
- running the application
- executing tests
- validating API behavior
- validating database behavior
- debugging failures
- making final implementation decisions
- reviewing performance considerations

AI-generated suggestions were treated as proposals rather than automatically
accepted implementation decisions.

---

## 2. Example AI Prompts

### Requirements and Architecture

> Review the employee salary management requirements and propose a simple,
> maintainable architecture for a Java Spring Boot backend with React and
> PostgreSQL. Keep the solution appropriate for approximately 10,000 employees
> and avoid unnecessary microservices.

### Database Design

> Design a PostgreSQL schema for employees and salary history. Salary changes
> must preserve historical records, support future effective dates, and prevent
> duplicate salary records for the same employee and effective date.

### Salary Logic

> Design the service-layer logic for determining an employee's current salary
> when salary history can contain both past and future effective dates.

### API Design

> Review these Spring Boot employee and salary endpoints and identify validation,
> HTTP status code, error handling, pagination, and API design improvements.

### Testing

> Review the employee and salary services and propose unit and integration test
> cases covering successful operations, validation failures, duplicate records,
> missing employees, pagination, and future-dated salaries.

### Performance

> Review the employee search, salary history, and analytics queries for a
> dataset of approximately 10,000 employees. Identify appropriate indexes and
> explain how to verify query performance using PostgreSQL EXPLAIN ANALYZE.

### Frontend

> Review the React employee management UI and suggest a clean component
> structure for employee search, filtering, pagination, employee details,
> salary history, salary creation, and analytics.

### Debugging

> Analyze this Spring Boot/PostgreSQL error and identify the likely root cause.
> Suggest the smallest safe change and explain how to verify the fix.

---

## 3. Human Verification of AI Output

AI suggestions were validated through implementation and testing.

Examples of verification included:

- running the complete Maven test suite
- running frontend tests
- running the frontend production build
- starting PostgreSQL through Docker
- validating Flyway migrations
- validating REST APIs
- testing employee search and filtering
- testing pagination
- testing salary history
- testing future-dated salaries
- testing duplicate salary protection
- validating analytics
- running the complete application through Docker Compose

---

# Engineering Decisions

## 4. Modular Monolith

A modular monolith was selected instead of microservices.

Reason:

- the assessment scope is a single business domain
- the expected dataset is approximately 10,000 employees
- separate services would add deployment and operational complexity
- clear package boundaries provide maintainability without distributed-system
  overhead

Potential future modules/services can be introduced if the system grows.

---

## 5. PostgreSQL

PostgreSQL was selected as the relational database.

Reasons:

- strong relational integrity
- foreign keys and unique constraints
- support for NUMERIC salary values
- effective indexing
- aggregation capabilities
- compatibility with Spring Data JPA
- straightforward Docker deployment

---

## 6. Flyway for Schema Management

Flyway is used for database migrations.

Hibernate is configured with:

```yaml
ddl-auto: validate
```

This means Hibernate validates the schema rather than creating or modifying
database tables automatically.

Schema changes are therefore explicitly versioned through migrations.

## 7. Salary History Instead of Updating Salary In Place

Salary changes create new salary history records.

For example:

2026-01-01 → 51,860 INR
2026-09-20 → 60,000 INR
2026-10-01 → 70,000 INR

The current salary is determined using the latest salary record whose
effective date is less than or equal to the current date.

This preserves historical information and supports future salary changes.

## 8. BigDecimal for Salary

Salary amounts use Java BigDecimal and PostgreSQL NUMERIC.

This avoids floating-point precision problems when representing monetary
values.

## 9. Currency Handling

Currency is stored as a three-character uppercase ISO-style currency code.

Examples:

INR
USD
EUR

The system does not perform currency conversion.

Analytics therefore operate on the stored salary values and should be
interpreted with their currency context.

## 10. Database-Side Pagination and Filtering

Employee filtering and pagination are performed in the database.

The application does not load all employees into memory and filter them
inside Java.

Indexes support commonly used filters and salary-history lookups.

## 11. Database-Side Analytics

Analytics aggregation is performed by PostgreSQL.

The application requests aggregated results rather than loading all employee
salary records into Java and calculating statistics in application memory.

This reduces application memory usage and allows the database query planner
to optimize aggregation.

## 12. Deterministic Seed Data

The application provides deterministic seed data for approximately 10,000
employees.

This makes the demonstration reproducible and allows the same dataset to be
used for:

search demonstrations
pagination
salary history
analytics
performance checks

Seed data is kept separate from Flyway migrations.

## 13. Validation Strategy

Validation is performed at multiple layers.

### API validation

Jakarta Bean Validation handles client input validation.

Examples:

- required fields
- string lengths
- email format
- positive salary amount
- three-character uppercase currency
- Service validation

Business rules such as employee uniqueness and salary-history conflicts are
validated by the service layer.

### Database integrity

The database enforces durable integrity through:

- primary keys
- unique constraints
- foreign keys
- NOT NULL constraints
- salary amount checks
- currency format checks
## 14. Error Handling

The REST API uses centralized exception handling.

Current status mapping:

400 → validation failure
404 → resource not found
409 → duplicate/conflicting data
500 → unexpected server error

Unexpected errors return a safe generic message instead of exposing internal
implementation details.

## 15. Testing Strategy

Testing covers multiple layers.

### Backend
- service behavior
- repository behavior
- controller/API behavior
- validation
- duplicate handling
- salary history
- future salary behavior

### Frontend
- employee page behavior
- analytics page behavior
- salary form behavior
- API success/error handling

The application was also manually verified through the complete UI flow.

## 16. Performance Verification

Performance was evaluated using PostgreSQL EXPLAIN ANALYZE.

Representative checks included:

- employee filtering using country and department
- salary-history lookup by employee
- indexed salary-history access
- database-side analytics aggregation

The architecture favors database-side filtering, bounded pagination, indexes,
and aggregation rather than loading the complete dataset into application
memory.

## 17. Trade-offs
Chosen

Simple modular monolith.

Trade-off

Less independent scalability than microservices, but significantly lower
complexity for the current scope.

Chosen

PostgreSQL + JPA.

Trade-off

JPA simplifies domain persistence, but complex analytics may require carefully
designed JPQL/native queries and database indexes.

Chosen

No authentication/authorization in V1.

Trade-off

This keeps the assessment implementation focused, but production deployment
would require authentication, authorization, audit logging, and security
hardening.

Chosen

No currency conversion.

Trade-off

Analytics across different currencies cannot be interpreted as a single
normalized monetary value without an exchange-rate strategy.

## 18. Future Improvements

Potential production extensions include:

- authentication and role-based authorization
- audit logging
- currency conversion service
- employee import/export
- advanced reporting
- caching for frequently requested analytics
- asynchronous processing for larger datasets
- observability with metrics, logs, and tracing
- CI/CD deployment pipeline
- horizontal scaling