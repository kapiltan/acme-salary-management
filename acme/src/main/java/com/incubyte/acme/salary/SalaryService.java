package com.incubyte.acme.salary;

import com.incubyte.acme.employee.Employee;
import com.incubyte.acme.employee.EmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class SalaryService {

    private final SalaryHistoryRepository salaryHistoryRepository;
    private final EmployeeService employeeService;

    public SalaryService(
            SalaryHistoryRepository salaryHistoryRepository,
            EmployeeService employeeService) {
        this.salaryHistoryRepository = salaryHistoryRepository;
        this.employeeService = employeeService;
    }

    public List<SalaryHistory> getSalaryHistory(Long employeeId) {
        employeeService.getEmployee(employeeId);

        return salaryHistoryRepository
                .findByEmployeeIdOrderByEffectiveFromDesc(employeeId);
    }

    public SalaryHistory getCurrentSalary(Long employeeId) {
        employeeService.getEmployee(employeeId);

        return salaryHistoryRepository
                .findFirstByEmployeeIdAndEffectiveFromLessThanEqualOrderByEffectiveFromDesc(
                        employeeId,
                        LocalDate.now())
                .orElseThrow(() -> new SalaryNotFoundException(
                        "No salary found for employee: " + employeeId));
    }

    @Transactional
    public SalaryHistory addSalary(
            Long employeeId,
            SalaryHistory salaryHistory) {
        Employee employee = employeeService.getEmployee(employeeId);

        if (salaryHistoryRepository.existsByEmployeeIdAndEffectiveFrom(
                employeeId,
                salaryHistory.getEffectiveFrom())) {
            throw new SalaryAlreadyExistsException(
                    "Salary already exists for employee "
                            + employeeId
                            + " on effective date "
                            + salaryHistory.getEffectiveFrom());
        }

        salaryHistory.setEmployee(employee);

        return salaryHistoryRepository.save(salaryHistory);
    }
}