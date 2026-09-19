package com.incubyte.acme.salary;

import com.incubyte.acme.employee.Employee;
import com.incubyte.acme.employee.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalaryServiceTest {

    @Mock
    private SalaryHistoryRepository salaryHistoryRepository;

    @Mock
    private EmployeeService employeeService;

    private SalaryService salaryService;

    @BeforeEach
    void setUp() {
        salaryService = new SalaryService(
                salaryHistoryRepository,
                employeeService);
    }

    @Test
    void shouldReturnCurrentSalary() {
        Employee employee = new Employee();

        SalaryHistory salary = salary(
                new BigDecimal("120000"),
                "INR",
                LocalDate.of(2026, 9, 1));

        when(employeeService.getEmployee(1L))
                .thenReturn(employee);

        when(salaryHistoryRepository
                .findFirstByEmployeeIdAndEffectiveFromLessThanEqualOrderByEffectiveFromDesc(
                        eq(1L),
                        any(LocalDate.class)))
                .thenReturn(Optional.of(salary));

        SalaryHistory result = salaryService.getCurrentSalary(1L);

        assertEquals(
                new BigDecimal("120000"),
                result.getAmount());

        assertEquals("INR", result.getCurrency());
    }

    @Test
    void shouldThrowWhenCurrentSalaryDoesNotExist() {
        when(employeeService.getEmployee(1L))
                .thenReturn(new Employee());

        when(salaryHistoryRepository
                .findFirstByEmployeeIdAndEffectiveFromLessThanEqualOrderByEffectiveFromDesc(
                        eq(1L),
                        any(LocalDate.class)))
                .thenReturn(Optional.empty());

        assertThrows(
                SalaryNotFoundException.class,
                () -> salaryService.getCurrentSalary(1L));
    }

    @Test
    void shouldAddSalary() {
        Employee employee = new Employee();

        SalaryHistory salary = salary(
                new BigDecimal("120000"),
                "INR",
                LocalDate.of(2026, 9, 1));

        when(employeeService.getEmployee(1L))
                .thenReturn(employee);

        when(salaryHistoryRepository.existsByEmployeeIdAndEffectiveFrom(
                1L,
                salary.getEffectiveFrom())).thenReturn(false);

        when(salaryHistoryRepository.save(salary))
                .thenReturn(salary);

        SalaryHistory result = salaryService.addSalary(1L, salary);

        assertEquals(employee, result.getEmployee());

        verify(salaryHistoryRepository)
                .save(salary);
    }

    @Test
    void shouldRejectDuplicateSalaryEffectiveDate() {
        Employee employee = new Employee();

        SalaryHistory salary = salary(
                new BigDecimal("120000"),
                "INR",
                LocalDate.of(2026, 9, 1));

        when(employeeService.getEmployee(1L))
                .thenReturn(employee);

        when(salaryHistoryRepository.existsByEmployeeIdAndEffectiveFrom(
                1L,
                salary.getEffectiveFrom())).thenReturn(true);

        assertThrows(
                SalaryAlreadyExistsException.class,
                () -> salaryService.addSalary(1L, salary));

        verify(salaryHistoryRepository, never())
                .save(any(SalaryHistory.class));
    }

    private SalaryHistory salary(
            BigDecimal amount,
            String currency,
            LocalDate effectiveFrom) {
        SalaryHistory salary = new SalaryHistory();

        salary.setAmount(amount);
        salary.setCurrency(currency);
        salary.setEffectiveFrom(effectiveFrom);

        return salary;
    }
}