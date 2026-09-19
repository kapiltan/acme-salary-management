package com.incubyte.acme.salary.dto;

import com.incubyte.acme.salary.SalaryHistory;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SalaryResponse(
        Long id,
        BigDecimal amount,
        String currency,
        LocalDate effectiveFrom) {

    public static SalaryResponse from(SalaryHistory salary) {
        return new SalaryResponse(
                salary.getId(),
                salary.getAmount(),
                salary.getCurrency(),
                salary.getEffectiveFrom());
    }
}