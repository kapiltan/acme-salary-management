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
    public Employee createEmployee(Employee employee) {
        validateUniqueEmployee(employee);

        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        Employee existing = getEmployee(id);

        existing.setFirstName(updatedEmployee.getFirstName());
        existing.setLastName(updatedEmployee.getLastName());
        existing.setEmail(updatedEmployee.getEmail());
        existing.setCountry(updatedEmployee.getCountry());
        existing.setDepartment(updatedEmployee.getDepartment());
        existing.setJobTitle(updatedEmployee.getJobTitle());

        return employeeRepository.save(existing);
    }

    private void validateUniqueEmployee(Employee employee) {
        employeeRepository.findByEmployeeCode(employee.getEmployeeCode())
                .ifPresent(existing -> {
                    throw new EmployeeAlreadyExistsException(
                            "Employee code already exists: "
                                    + employee.getEmployeeCode());
                });

        employeeRepository.findByEmail(employee.getEmail())
                .ifPresent(existing -> {
                    throw new EmployeeAlreadyExistsException(
                            "Email already exists: "
                                    + employee.getEmail());
                });
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}