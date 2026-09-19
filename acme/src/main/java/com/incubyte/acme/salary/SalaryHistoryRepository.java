package com.incubyte.acme.salary;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SalaryHistoryRepository extends JpaRepository<SalaryHistory, Long> {

    List<SalaryHistory> findByEmployeeIdOrderByEffectiveFromDesc(Long employeeId);

    Optional<SalaryHistory> findFirstByEmployeeIdAndEffectiveFromLessThanEqualOrderByEffectiveFromDesc(
            Long employeeId,
            LocalDate date);

    boolean existsByEmployeeIdAndEffectiveFrom(
            Long employeeId,
            LocalDate effectiveFrom);
}