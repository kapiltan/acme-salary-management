package com.incubyte.acme.analytics;

import com.incubyte.acme.analytics.dto.SalaryByGroupProjection;
import com.incubyte.acme.analytics.dto.SalaryDistributionProjection;
import com.incubyte.acme.analytics.dto.SalaryDistributionResponse;
import com.incubyte.acme.employee.Employee;

import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.repository.Repository;

public interface AnalyticsRepository extends Repository<Employee, Long> {

    @Query("""
            SELECT
                e.country AS groupName,
                COUNT(DISTINCT e.id) AS employeeCount,
                AVG(s.amount) AS averageSalary
            FROM Employee e
            JOIN e.salaryHistory s
            WHERE s.effectiveFrom = (
                SELECT MAX(s2.effectiveFrom)
                FROM SalaryHistory s2
                WHERE s2.employee.id = e.id
                  AND s2.effectiveFrom <= CURRENT_DATE
            )
            GROUP BY e.country
            ORDER BY e.country
            """)
    List<SalaryByGroupProjection> findSalaryByCountry();

    @Query("""
            SELECT
                e.department AS groupName,
                COUNT(DISTINCT e.id) AS employeeCount,
                AVG(s.amount) AS averageSalary
            FROM Employee e
            JOIN e.salaryHistory s
            WHERE s.effectiveFrom = (
                SELECT MAX(s2.effectiveFrom)
                FROM SalaryHistory s2
                WHERE s2.employee.id = e.id
                  AND s2.effectiveFrom <= CURRENT_DATE
            )
            GROUP BY e.department
            ORDER BY e.department
            """)
    List<SalaryByGroupProjection> findSalaryByDepartment();

    @Query("""
            SELECT COUNT(e)
            FROM Employee e
            """)
    long countEmployees();

    @Query("""
            SELECT AVG(s.amount)
            FROM SalaryHistory s
            WHERE s.effectiveFrom = (
                SELECT MAX(s2.effectiveFrom)
                FROM SalaryHistory s2
                WHERE s2.employee.id = s.employee.id
                  AND s2.effectiveFrom <= CURRENT_DATE
            )
            """)
    BigDecimal findAverageSalary();

    @Query("""
            SELECT MAX(s.amount)
            FROM SalaryHistory s
            WHERE s.effectiveFrom = (
                SELECT MAX(s2.effectiveFrom)
                FROM SalaryHistory s2
                WHERE s2.employee.id = s.employee.id
                  AND s2.effectiveFrom <= CURRENT_DATE
            )
            """)
    BigDecimal findHighestSalary();

    @Query("""
            SELECT MIN(s.amount)
            FROM SalaryHistory s
            WHERE s.effectiveFrom = (
                SELECT MAX(s2.effectiveFrom)
                FROM SalaryHistory s2
                WHERE s2.employee.id = s.employee.id
                  AND s2.effectiveFrom <= CURRENT_DATE
            )
            """)
    BigDecimal findLowestSalary();

    @Query("""
            SELECT
                CASE
                    WHEN s.amount < 75000 THEN '0-75K'
                    WHEN s.amount < 100000 THEN '75K-100K'
                    WHEN s.amount < 125000 THEN '100K-125K'
                    ELSE '125K+'
                END AS salaryRange,
                COUNT(DISTINCT s.employee.id) AS employeeCount
            FROM SalaryHistory s
            WHERE s.effectiveFrom = (
                SELECT MAX(s2.effectiveFrom)
                FROM SalaryHistory s2
                WHERE s2.employee.id = s.employee.id
                  AND s2.effectiveFrom <= CURRENT_DATE
            )
            GROUP BY
                CASE
                    WHEN s.amount < 75000 THEN '0-75K'
                    WHEN s.amount < 100000 THEN '75K-100K'
                    WHEN s.amount < 125000 THEN '100K-125K'
                    ELSE '125K+'
                END
            ORDER BY
                CASE
                    WHEN MIN(s.amount) < 75000 THEN 1
                    WHEN MIN(s.amount) < 100000 THEN 2
                    WHEN MIN(s.amount) < 125000 THEN 3
                    ELSE 4
                END
            """)
    List<SalaryDistributionProjection> findSalaryDistribution();
}