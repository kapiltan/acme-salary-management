package com.incubyte.acme.analytics;

import com.incubyte.acme.analytics.dto.AnalyticsSummaryResponse;
import com.incubyte.acme.analytics.dto.SalaryByGroupProjection;
import com.incubyte.acme.analytics.dto.SalaryDistributionProjection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/summary")
    public AnalyticsSummaryResponse getSummary() {
        return new AnalyticsSummaryResponse(
                analyticsService.getEmployeeCount(),
                analyticsService.getAverageSalary(),
                analyticsService.getHighestSalary(),
                analyticsService.getLowestSalary());
    }

    @GetMapping("/salary-by-country")
    public List<SalaryByGroupProjection> getSalaryByCountry() {
        return analyticsService.getSalaryByCountry();
    }

    @GetMapping("/salary-by-department")
    public List<SalaryByGroupProjection> getSalaryByDepartment() {
        return analyticsService.getSalaryByDepartment();
    }

    @GetMapping("/salary-distribution")
    public List<SalaryDistributionProjection> getSalaryDistribution() {
        return analyticsService.getSalaryDistribution();
    }
}