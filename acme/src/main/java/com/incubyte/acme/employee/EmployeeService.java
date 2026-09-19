package com.incubyte.acme.employee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Page<Employee> searchEmployees(
            String search,
            String country,
            String department,
            Pageable pageable) {
        return employeeRepository.search(
                normalize(search),
                normalize(country),
                normalize(department),
                pageable);
    }

    public Employee getEmployee(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(
                        "Employee not found with id: " + id));
    }

    @Transactional
    public Employee updateEmployee(
            Long id,
            String firstName,
            String lastName,
            String email,
            String country,
            String department,
            String jobTitle) {
        Employee existing = getEmployee(id);

        if (!existing.getEmail().equalsIgnoreCase(email)) {
            employeeRepository.findByEmail(email)
                    .ifPresent(employee -> {
                        throw new EmployeeAlreadyExistsException(
                                "Email already exists: " + email);
                    });
        }

        existing.setFirstName(firstName);
        existing.setLastName(lastName);
        existing.setEmail(email);
        existing.setCountry(country);
        existing.setDepartment(department);
        existing.setJobTitle(jobTitle);

        return employeeRepository.save(existing);
    }

    private void validateUniqueEmployee(
            String employeeCode,
            String email) {
        employeeRepository.findByEmployeeCode(employeeCode)
                .ifPresent(existing -> {
                    throw new EmployeeAlreadyExistsException(
                            "Employee code already exists: " + employeeCode);
                });

        employeeRepository.findByEmail(email)
                .ifPresent(existing -> {
                    throw new EmployeeAlreadyExistsException(
                            "Email already exists: " + email);
                });
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }

        return value.trim();
    }

    @Transactional
    public Employee createEmployee(
            String employeeCode,
            String firstName,
            String lastName,
            String email,
            String country,
            String department,
            String jobTitle) {
        validateUniqueEmployee(employeeCode, email);

        Employee employee = new Employee();

        employee.setEmployeeCode(employeeCode);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmail(email);
        employee.setCountry(country);
        employee.setDepartment(department);
        employee.setJobTitle(jobTitle);

        return employeeRepository.save(employee);
    }
}