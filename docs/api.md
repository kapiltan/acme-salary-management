# ACME Salary Management — API Documentation

## Base URL

```text
http://localhost:8080/api
```

## Employees
### List Employees
GET /employees

Query parameters:

Parameter	Required	Description
search	No	Searches employee code, name, or email
country	No	Filters by country
department	No	Filters by department
page	No	Zero-based page number
size	No	Number of records per page
sort	No	Spring Data sort expression

Example:

GET /employees?search=employee1&country=India&page=0&size=20

Response:

{
  "content": [],
  "page": 0,
  "size": 20,
  "totalElements": 10000,
  "totalPages": 500
}

### Get Employee
GET /employees/{id}

Example:

GET /employees/186

Returns employee details.

### Create Employee
POST /employees

Request:

{
  "employeeCode": "EMP10001",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@acme.com",
  "country": "India",
  "department": "Engineering",
  "jobTitle": "Software Engineer"
}

Returns 201 Created.

Duplicate employee code/email returns 409 Conflict.

### Update Employee
PUT /employees/{id}

Request:

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@acme.com",
  "country": "India",
  "department": "Engineering",
  "jobTitle": "Senior Software Engineer"
}

Returns the updated employee.

## Salary
### Get Current Salary
GET /employees/{employeeId}/salary

Returns the salary record whose effective date is the latest date less than
or equal to the current date.

### Get Salary History
GET /employees/{employeeId}/salary/history

Returns salary records ordered by effective date descending.

### Add Salary
POST /employees/{employeeId}/salary

Request:

{
  "amount": 70000,
  "currency": "INR",
  "effectiveFrom": "2026-10-01"
}

Returns 201 Created.

A duplicate effective date for the same employee returns 409 Conflict.

Future-dated salary records are stored in salary history but do not become
the current salary until their effective date.

## Analytics
### Summary
GET /analytics/summary

Returns:

- total employees
- average salary
- highest salary
- lowest salary

### Salary by Country
GET /analytics/salary-by-country

Returns employee count and average salary grouped by country.

### Salary by Department
GET /analytics/salary-by-department

Returns employee count and average salary grouped by department.

### Salary Distribution
GET /analytics/salary-distribution

Returns employee counts grouped into salary ranges.

## Error Handling
HTTP Status	Meaning
400	Validation failure
404	Employee or salary record not found
409	Duplicate/conflicting data
500	Unexpected server error

Validation errors include field-level messages where applicable.