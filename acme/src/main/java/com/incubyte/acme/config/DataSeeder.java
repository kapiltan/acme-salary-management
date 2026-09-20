package com.incubyte.acme.config;

import com.incubyte.acme.employee.Employee;
import com.incubyte.acme.employee.EmployeeRepository;
import com.incubyte.acme.salary.SalaryHistory;
import com.incubyte.acme.salary.SalaryHistoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Profile("seed")
public class DataSeeder {

    private static final int EMPLOYEE_COUNT = 10_000;
    private static final int BATCH_SIZE = 500;

    private static final String[] FIRST_NAMES = {
            "Aarav",
            "Vihaan",
            "Aditya",
            "Arjun",
            "Rahul",
            "Rohan",
            "Vikram",
            "Neha",
            "Priya",
            "Ananya",
            "Sneha",
            "Kavya"
    };

    private static final String[] LAST_NAMES = {
            "Sharma",
            "Verma",
            "Singh",
            "Kumar",
            "Patel",
            "Gupta",
            "Mehta",
            "Tanwar",
            "Malhotra",
            "Iyer"
    };

    private static final String[] COUNTRIES = {
            "India",
            "United States",
            "Germany",
            "United Kingdom",
            "Singapore"
    };

    private static final String[] DEPARTMENTS = {
            "Engineering",
            "Finance",
            "Human Resources",
            "Sales",
            "Operations",
            "Marketing"
    };

    private static final String[] JOB_TITLES = {
            "Software Engineer",
            "Senior Software Engineer",
            "Product Manager",
            "Business Analyst",
            "Data Analyst",
            "Engineering Manager",
            "HR Specialist",
            "Financial Analyst"
    };

    @Bean
    CommandLineRunner seedData(
            EmployeeRepository employeeRepository,
            SalaryHistoryRepository salaryHistoryRepository) {
        return args -> seed(
                employeeRepository,
                salaryHistoryRepository);
    }

    @Transactional
    public void seed(
            EmployeeRepository employeeRepository,
            SalaryHistoryRepository salaryHistoryRepository) {
        long existingEmployees = employeeRepository.count();

        if (existingEmployees >= EMPLOYEE_COUNT) {
            System.out.println(
                    "Seed skipped. Employees already present: "
                            + existingEmployees);
            return;
        }

        System.out.println("Starting employee seed...");

        List<Employee> employees = new ArrayList<>(BATCH_SIZE);

        for (int i = 1; i <= EMPLOYEE_COUNT; i++) {

            String employeeCode = String.format(
                    "EMP%05d",
                    i);

            if (employeeRepository
                    .findByEmployeeCode(employeeCode)
                    .isPresent()) {
                continue;
            }

            Employee employee = new Employee();

            employee.setEmployeeCode(employeeCode);

            employee.setFirstName(
                    FIRST_NAMES[(i - 1) % FIRST_NAMES.length]);

            employee.setLastName(
                    LAST_NAMES[(i - 1) % LAST_NAMES.length]);

            employee.setEmail(
                    "employee" + i + "@acme.com");

            employee.setCountry(
                    COUNTRIES[(i - 1) % COUNTRIES.length]);

            employee.setDepartment(
                    DEPARTMENTS[(i - 1) % DEPARTMENTS.length]);

            employee.setJobTitle(
                    JOB_TITLES[(i - 1) % JOB_TITLES.length]);

            employees.add(employee);

            if (employees.size() == BATCH_SIZE) {
                employeeRepository.saveAll(employees);
                employeeRepository.flush();
                employees.clear();

                System.out.println(
                        "Seeded employees up to EMP"
                                + String.format("%05d", i));
            }
        }

        if (!employees.isEmpty()) {
            employeeRepository.saveAll(employees);
            employeeRepository.flush();
        }

        System.out.println("Employee seed completed.");

        seedSalaries(
                employeeRepository,
                salaryHistoryRepository);
    }

    private void seedSalaries(
            EmployeeRepository employeeRepository,
            SalaryHistoryRepository salaryHistoryRepository) {
        List<Employee> employees = employeeRepository.findAll();

        List<SalaryHistory> salaries = new ArrayList<>(BATCH_SIZE);

        LocalDate effectiveFrom = LocalDate.of(2026, 1, 1);

        for (Employee employee : employees) {

            if (salaryHistoryRepository
                    .findFirstByEmployeeIdAndEffectiveFromLessThanEqualOrderByEffectiveFromDesc(
                            employee.getId(),
                            effectiveFrom)
                    .isPresent()) {
                continue;
            }

            SalaryHistory salary = new SalaryHistory();

            salary.setEmployee(employee);

            BigDecimal amount = BigDecimal.valueOf(
                    50_000L + (employee.getId() % 10_000) * 10L);

            salary.setAmount(amount);
            salary.setCurrency("INR");
            salary.setEffectiveFrom(effectiveFrom);

            salaries.add(salary);

            if (salaries.size() == BATCH_SIZE) {
                salaryHistoryRepository.saveAll(salaries);
                salaryHistoryRepository.flush();
                salaries.clear();
            }
        }

        if (!salaries.isEmpty()) {
            salaryHistoryRepository.saveAll(salaries);
            salaryHistoryRepository.flush();
        }

        System.out.println("Salary seed completed.");
    }
}