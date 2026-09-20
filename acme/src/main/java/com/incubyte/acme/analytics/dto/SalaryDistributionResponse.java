package com.incubyte.acme.analytics.dto;

public record SalaryDistributionResponse(
        String range,
        long employeeCount) {
}