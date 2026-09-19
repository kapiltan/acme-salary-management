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