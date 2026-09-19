package com.incubyte.acme.employee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeService(employeeRepository);
    }

    @Test
    void shouldReturnEmployeeWhenEmployeeExists() {
        Employee employee = employee("EMP00001");

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        Employee result = employeeService.getEmployee(1L);

        assertEquals("EMP00001", result.getEmployeeCode());
        verify(employeeRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenEmployeeDoesNotExist() {
        when(employeeRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.getEmployee(999L));
    }

    @Test
    void shouldCreateEmployee() {
        when(employeeRepository.findByEmployeeCode("EMP00001"))
                .thenReturn(Optional.empty());

        when(employeeRepository.findByEmail("kapil@example.com"))
                .thenReturn(Optional.empty());

        Employee saved = employee("EMP00001");

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(saved);

        Employee result = employeeService.createEmployee(
                "EMP00001",
                "Kapil",
                "Tanwar",
                "kapil@example.com",
                "India",
                "Engineering",
                "Software Engineer");

        assertEquals("EMP00001", result.getEmployeeCode());

        verify(employeeRepository)
                .save(any(Employee.class));
    }

    @Test
    void shouldRejectDuplicateEmployeeCode() {
        when(employeeRepository.findByEmployeeCode("EMP00001"))
                .thenReturn(Optional.of(employee("EMP00001")));

        assertThrows(
                EmployeeAlreadyExistsException.class,
                () -> employeeService.createEmployee(
                        "EMP00001",
                        "Kapil",
                        "Tanwar",
                        "kapil@example.com",
                        "India",
                        "Engineering",
                        "Software Engineer"));

        verify(employeeRepository, never())
                .save(any(Employee.class));
    }

    @Test
    void shouldSearchEmployees() {
        Employee employee = employee("EMP00001");

        Page<Employee> page = new PageImpl<>(
                List.of(employee));

        when(employeeRepository.search(
                "kapil",
                "",
                "",
                PageRequest.of(0, 20))).thenReturn(page);

        Page<Employee> result = employeeService.searchEmployees(
                "kapil",
                null,
                null,
                PageRequest.of(0, 20));

        assertEquals(1, result.getTotalElements());
        assertEquals(
                "EMP00001",
                result.getContent().get(0).getEmployeeCode());

        verify(employeeRepository).search(
                "kapil",
                "",
                "",
                PageRequest.of(0, 20));
    }

    private Employee employee(String employeeCode) {
        Employee employee = new Employee();

        employee.setId(1L);
        employee.setEmployeeCode(employeeCode);
        employee.setFirstName("Kapil");
        employee.setLastName("Tanwar");
        employee.setEmail("kapil@example.com");
        employee.setCountry("India");
        employee.setDepartment("Engineering");
        employee.setJobTitle("Software Engineer");

        return employee;
    }
}