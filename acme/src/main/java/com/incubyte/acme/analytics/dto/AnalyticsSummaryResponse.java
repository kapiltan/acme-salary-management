package com.incubyte.acme.analytics.dto;

import java.math.BigDecimal;

public record AnalyticsSummaryResponse(
        long totalEmployees,
        BigDecimal averageSalary,
        BigDecimal highestSalary,
        BigDecimal lowestSalary) {
}