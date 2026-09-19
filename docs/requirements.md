# ACME Salary Management System

## 1. Goal

Build a web-based salary management system for ACME's HR Manager to replace
Excel-based salary management for an organization of approximately 10,000
employees across multiple countries.

The system should allow HR to efficiently manage employee salary information,
maintain salary history, search and filter employees, and answer common
questions about how the organization pays its employees.

The application should provide a simple, reliable, and maintainable
alternative to spreadsheet-based salary management.

---

## 2. User Persona

### HR Manager

The primary user is an HR Manager who needs to:

- View employee salary information.
- Search for employees quickly.
- Filter employees by country, department, and other relevant attributes.
- Add or update employee salary information.
- View salary history.
- Understand salary distributions and organizational salary patterns.
- Answer questions such as:
  - What is the average salary by country?
  - What is the average salary by department?
  - What are the highest and lowest salaries?
  - How many employees are in each salary range?
  - How has an employee's salary changed over time?

---

## 3. Scope

### Employee Management

The system will support:

- Viewing employees.
- Searching employees by employee ID, name, or email.
- Filtering employees by country and department.
- Viewing employee details.
- Creating employees.
- Updating employee information.

### Salary Management

The system will support:

- Viewing an employee's current salary.
- Adding a salary record.
- Updating an employee's salary by creating a new salary-history record.
- Viewing complete salary history.
- Storing salary amount and currency.
- Tracking the effective date of each salary.

Salary history will be preserved rather than overwritten.

### Salary Analytics

The system will provide:

- Average salary by country.
- Average salary by department.
- Employee count by country.
- Employee count by department.
- Highest and lowest salaries.
- Salary distribution/ranges.
- Basic organization-level salary statistics.

For employees with different currencies, analytics will not perform
currency conversion. Aggregations will therefore be currency-aware.

### Data Seeding

The application will include a deterministic seed mechanism capable of
creating approximately 10,000 employees with salary data.

---

## 4. Non-Functional Requirements

The application should:

- Support approximately 10,000 employees.
- Provide paginated employee listing APIs.
- Validate API input.
- Return appropriate HTTP status codes.
- Handle invalid requests gracefully.
- Maintain salary history.
- Use database constraints to protect data integrity.
- Use database migrations for schema management.
- Include meaningful unit and integration tests.
- Keep tests fast and deterministic.
- Have clear separation between controller, service, repository, and
  persistence layers.
- Be containerizable and deployable.

---

## 5. Deliberately Out of Scope

The following features are intentionally excluded from the first version:

### Authentication and Authorization

A full authentication and role-based authorization system is excluded.

Reason:

The assessment focuses primarily on salary management, product design,
backend engineering, data modeling, testing, and UI implementation.
Authentication can be added later without changing the core domain model.

### Payroll Processing

The system will not calculate or process:

- Taxes
- Bonuses
- Deductions
- Payslips
- Payroll runs
- Employee benefits

Reason:

These are separate payroll-domain concerns and are not required to solve
the stated salary-management problem.

### Currency Conversion

The system will store salary amounts in their original currency but will
not convert between currencies.

Reason:

Currency conversion requires exchange-rate providers, historical exchange
rates, rounding rules, and additional product requirements. These are not
necessary for the initial salary-management workflow.

### Bulk Excel Import/Export

Excel import/export is excluded from the initial version.

Reason:

The goal is to replace spreadsheet-based management with a web application.
Import/export can be added as a migration or operational feature later.

### Advanced Reporting

Complex report builders, scheduled reports, PDF generation, and custom
business intelligence dashboards are excluded.

Reason:

The first version focuses on the core questions HR needs to answer.

### Employee Self-Service

Employees will not directly access or modify their salary information.

Reason:

The defined user persona for this assessment is the HR Manager.

---

## 6. Success Criteria

The solution will be considered successful when an HR Manager can:

1. Open the web application.
2. View a paginated list of employees.
3. Search and filter employees.
4. View an employee's current salary.
5. Add/update salary information while preserving salary history.
6. View previous salary records.
7. View salary analytics by country and department.
8. Answer common organizational salary questions without using Excel.
9. Manage approximately 10,000 seeded employees with acceptable response times.

The application should be fully functional, tested, documented, and
deployable.