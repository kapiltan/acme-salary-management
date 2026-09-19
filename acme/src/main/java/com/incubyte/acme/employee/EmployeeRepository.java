package com.incubyte.acme.employee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmployeeCode(String employeeCode);

    Optional<Employee> findByEmail(String email);

    @Query("""
            SELECT e
            FROM Employee e
            WHERE
                (
                    :search = ''
                    OR LOWER(e.employeeCode) LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(e.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(e.email) LIKE LOWER(CONCAT('%', :search, '%'))
                )
                AND (
                    :country = ''
                    OR LOWER(e.country) = LOWER(:country)
                )
                AND (
                    :department = ''
                    OR LOWER(e.department) = LOWER(:department)
                )
            """)
    Page<Employee> search(
            @Param("search") String search,
            @Param("country") String country,
            @Param("department") String department,
            Pageable pageable);
}