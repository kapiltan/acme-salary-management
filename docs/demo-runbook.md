# ACME Salary Management — Demo Runbook

## 1. Start the application

From the repository root:

```powershell
docker compose up --build
```

The application is available at:

Frontend: http://localhost:5173
Backend: http://localhost:8080
PostgreSQL: localhost:5432

## 2. Verify seeded data

Open:

http://localhost:5173

Open the Employees page.

Expected:

10,000 employees
20 employees per page
pagination available
## 3. Demonstrate search and filtering

Search for an employee using:

employee ID/code
employee name
email

Then demonstrate:

country filter
department filter
pagination

Expected:

results are filtered server-side
pagination remains functional
total employee count is displayed
## 4. Demonstrate employee details

Open an employee from the employee list.

Verify:

employee information
current salary
salary history
## 5. Demonstrate salary history

For the selected employee, add a salary record with an effective date in the future.

Example:

Amount: 70000
Currency: INR
Effective date: 2026-10-01

Verify:

the new salary appears in salary history
the current salary does not change before the effective date
## 6. Demonstrate effective salary change

Add another salary record with an effective date that is today or otherwise applicable.

Verify:

the applicable salary becomes the current salary
salary history retains previous records
## 7. Demonstrate duplicate protection

Attempt to add another salary record for the same employee and the same effective date.

Expected:

HTTP 409 Conflict

The UI should display an appropriate error message.

## 8. Demonstrate analytics

Open the Analytics page.

Demonstrate:

total employees
average salary
highest salary
lowest salary
salary by country
salary by department
salary distribution

Expected baseline with the seeded dataset should be captured before
demonstrating salary modifications.

## 9. Cleanup

After the demo, remove temporary salary records created specifically
for the demonstration.

The seeded employee dataset should remain unchanged.

## 10. Final verification

Verify:

backend starts successfully
Flyway migrations execute successfully
PostgreSQL is reachable
REST APIs respond successfully
React application loads successfully
employee search/filter works
pagination works
salary history works
salary creation works
future salary behavior works
analytics works