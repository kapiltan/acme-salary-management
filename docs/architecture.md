# ACME Salary Management System — Architecture & Design

## 1. Architecture Overview

The application will use a modular monolithic architecture.

The system consists of:

- ReactJS frontend
- Spring Boot REST API
- PostgreSQL relational database
- Flyway for database migrations

The initial version will run as a single deployable application rather
than multiple microservices.

### High-Level Architecture

```text
                    ┌──────────────────────┐
                    │      HR Manager      │
                    │      Web Browser     │
                    └──────────┬───────────┘
                               │
                               │ HTTP / JSON
                               ▼
                    ┌──────────────────────┐
                    │      ReactJS UI      │
                    └──────────┬───────────┘
                               │
                               │ REST API
                               ▼
              ┌───────────────────────────────┐
              │       Spring Boot Backend     │
              │                               │
              │  ┌──────────┐  ┌────────────┐ │
              │  │ Employee │  │   Salary   │ │
              │  │  Module  │  │   Module   │ │
              │  └────┬─────┘  └─────┬──────┘ │
              │       │              │        │
              │       └──────┬───────┘        │
              │              │                │
              │       ┌──────▼──────┐         │
              │       │ Analytics   │         │
              │       │   Module    │         │
              │       └──────┬──────┘         │
              │              │                │
              └──────────────┼────────────────┘
                             │
                             │ JPA / SQL
                             ▼
                    ┌──────────────────────┐
                    │     PostgreSQL       │
                    │                      │
                    │     employees        │
                    │     salary_history   │
                    └──────────────────────┘
```

## Performance Considerations

The application is designed to keep filtering, pagination, salary-history lookup, and analytics work at the database layer.

### Employee Search and Pagination

Employee search uses database-side filtering and Spring Data pagination rather than loading all employees into application memory.

The employee list API returns a paginated response containing:

- `content`
- `page`
- `size`
- `totalElements`
- `totalPages`

This keeps the API response bounded even with approximately 10,000 employees.

Indexes exist on:

- `employee_code` via unique constraint
- `email` via unique constraint
- `country`
- `department`

### Salary History

Salary history is indexed by:

```text
(employee_id, effective_from DESC)
```
This supports retrieving an employee's salary history and finding the latest
applicable salary efficiently.

A representative EXPLAIN ANALYZE for employee 186 used the
idx_salary_history_employee_effective index and completed in approximately
0.329 ms on the local PostgreSQL dataset.

### Combined Employee Filters

For a query filtering by country and department, PostgreSQL selected the
department index and applied the country condition as an additional filter.

On the local 10,000-employee dataset, the representative query completed in
approximately 3.5 ms.

A composite (country, department) index was therefore not added at this
stage. The current dataset does not demonstrate a need for the additional
index, and indexes should be added based on measured query patterns rather
than pre-emptively.

### Analytics

Analytics aggregation is performed in PostgreSQL rather than loading all
employees and salaries into Java memory.

Current analytics queries calculate:

- total employees
- average salary
- highest salary
- lowest salary
- salary by country
- salary by department
- salary distribution

Current-salary calculations consider only the latest salary record whose
effective_from is less than or equal to the current date.

### Seed Data

The development seed creates approximately 10,000 employees in batches of 500
rather than inserting all records individually in one large operation.

The seed is deterministic, making local testing and performance comparisons
repeatable.

### Future Scaling Considerations

If the employee population grows significantly beyond the assessment's
10,000-record target, the following could be evaluated based on profiling:

- composite indexes for frequently combined filters
- cursor/keyset pagination for very large datasets
- query-specific projections instead of loading full entities
- database query plan monitoring
- caching for relatively static analytics
- batch processing for large imports
