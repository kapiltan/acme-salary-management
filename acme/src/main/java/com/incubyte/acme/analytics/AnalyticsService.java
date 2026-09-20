package com.incubyte.acme.analytics;

import com.incubyte.acme.analytics.dto.SalaryByGroupProjection;
import com.incubyte.acme.analytics.dto.SalaryDistributionProjection;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AnalyticsService {

    private final AnalyticsRepository analyticsRepository;

    public AnalyticsService(AnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }

    public long getEmployeeCount() {
        return analyticsRepository.countEmployees();
    }

    public BigDecimal getAverageSalary() {
        return analyticsRepository.findAverageSalary();
    }

    public BigDecimal getHighestSalary() {
        return analyticsRepository.findHighestSalary();
    }

    public BigDecimal getLowestSalary() {
        return analyticsRepository.findLowestSalary();
    }

    public List<SalaryByGroupProjection> getSalaryByCountry() {
        return analyticsRepository.findSalaryByCountry();
    }

    public List<SalaryByGroupProjection> getSalaryByDepartment() {
        return analyticsRepository.findSalaryByDepartment();
    }

    public List<SalaryDistributionProjection> getSalaryDistribution() {
        return analyticsRepository.findSalaryDistribution();
    }
}